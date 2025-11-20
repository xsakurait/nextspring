'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import VideoCard from '@/components/VideoCard';
import ChannelCard from '@/components/ChannelCard';
import { videoApi, channelApi, categoryApi } from '@/lib/api';
import { useVideoStore, useChannelStore, useCategoryStore } from '@/store';
import type { Video, Channel, Category } from '@/types';

export default function Dashboard() {
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'videos' | 'channels'>('videos');
  const [syncing, setSyncing] = useState(false);

  const { videos, setVideos, sortBy, setSortBy, days, setDays } =
    useVideoStore();
  const { channels, setChannels, selectedCategory, setSelectedCategory } =
    useChannelStore();
  const { categories, setCategories } = useCategoryStore();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      router.push('/login');
      return;
    }

    loadData();
  }, []);

  useEffect(() => {
    if (!loading) {
      loadVideos();
    }
  }, [sortBy, days, selectedCategory]);

  const loadData = async () => {
    try {
      const [videosData, channelsData, categoriesData] = await Promise.all([
        videoApi.getRecent(days, 50),
        channelApi.getAll(),
        categoryApi.getAll(),
      ]);

      setVideos(videosData);
      setChannels(channelsData);
      setCategories(categoriesData);
    } catch (error) {
      console.error('Failed to load data:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadVideos = async () => {
    try {
      let videosData: Video[];

      if (selectedCategory) {
        videosData = await videoApi.getRecentByCategory(
          selectedCategory,
          days,
          50
        );
      } else {
        videosData = await videoApi.getRecentSorted(days, sortBy, 50);
      }

      setVideos(videosData);
    } catch (error) {
      console.error('Failed to load videos:', error);
    }
  };

  const handleSync = async () => {
    setSyncing(true);
    try {
      await videoApi.sync();
      setTimeout(() => {
        loadVideos();
        setSyncing(false);
      }, 3000);
    } catch (error) {
      console.error('Failed to sync videos:', error);
      setSyncing(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    router.push('/login');
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-900 flex items-center justify-center">
        <div className="text-white text-xl">読み込み中...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-900">
      {/* ヘッダー */}
      <header className="bg-gray-800 border-b border-gray-700 sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <h1 className="text-2xl font-bold text-white">
              YouTube チャンネルマネージャー
            </h1>

            <div className="flex items-center gap-4">
              <button
                onClick={handleSync}
                disabled={syncing}
                className="px-4 py-2 bg-blue-600 hover:bg-blue-700 disabled:bg-gray-600 text-white rounded-lg transition-colors"
              >
                {syncing ? '同期中...' : '動画を同期'}
              </button>

              <button
                onClick={handleLogout}
                className="px-4 py-2 bg-gray-700 hover:bg-gray-600 text-white rounded-lg transition-colors"
              >
                ログアウト
              </button>
            </div>
          </div>

          {/* タブ */}
          <div className="flex gap-4 mt-4">
            <button
              onClick={() => setActiveTab('videos')}
              className={`px-4 py-2 rounded-lg transition-colors ${
                activeTab === 'videos'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
              }`}
            >
              新着動画
            </button>
            <button
              onClick={() => setActiveTab('channels')}
              className={`px-4 py-2 rounded-lg transition-colors ${
                activeTab === 'channels'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
              }`}
            >
              チャンネル
            </button>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex gap-8">
          {/* サイドバー */}
          <aside className="w-64 flex-shrink-0">
            <div className="bg-gray-800 rounded-lg p-4 sticky top-24">
              <h2 className="text-white font-semibold mb-4">カテゴリ</h2>

              <button
                onClick={() => setSelectedCategory(null)}
                className={`w-full text-left px-3 py-2 rounded-lg mb-2 transition-colors ${
                  selectedCategory === null
                    ? 'bg-blue-600 text-white'
                    : 'text-gray-300 hover:bg-gray-700'
                }`}
              >
                すべて
              </button>

              {categories.map((category) => (
                <button
                  key={category.id}
                  onClick={() => setSelectedCategory(category.id)}
                  className={`w-full text-left px-3 py-2 rounded-lg mb-2 transition-colors ${
                    selectedCategory === category.id
                      ? 'bg-blue-600 text-white'
                      : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  <div className="flex items-center gap-2">
                    <div
                      className="w-3 h-3 rounded-full"
                      style={{ backgroundColor: category.color }}
                    />
                    <span>{category.name}</span>
                  </div>
                </button>
              ))}
            </div>
          </aside>

          {/* メインコンテンツ */}
          <main className="flex-1">
            {activeTab === 'videos' && (
              <div>
                {/* フィルター */}
                <div className="bg-gray-800 rounded-lg p-4 mb-6">
                  <div className="flex items-center gap-4">
                    <div>
                      <label className="text-gray-400 text-sm block mb-2">
                        期間
                      </label>
                      <select
                        value={days}
                        onChange={(e) => setDays(Number(e.target.value))}
                        className="bg-gray-700 text-white px-4 py-2 rounded-lg"
                      >
                        <option value={1}>1日</option>
                        <option value={3}>3日</option>
                        <option value={7}>7日</option>
                        <option value={14}>14日</option>
                        <option value={30}>30日</option>
                      </select>
                    </div>

                    <div>
                      <label className="text-gray-400 text-sm block mb-2">
                        並び替え
                      </label>
                      <select
                        value={sortBy}
                        onChange={(e) =>
                          setSortBy(
                            e.target.value as
                              | 'publishedAt'
                              | 'viewCount'
                              | 'likeCount'
                          )
                        }
                        className="bg-gray-700 text-white px-4 py-2 rounded-lg"
                      >
                        <option value="publishedAt">公開日</option>
                        <option value="viewCount">再生回数</option>
                        <option value="likeCount">高評価数</option>
                      </select>
                    </div>
                  </div>
                </div>

                {/* 動画グリッド */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {videos.map((video) => (
                    <VideoCard key={video.id} video={video} />
                  ))}
                </div>

                {videos.length === 0 && (
                  <div className="text-center text-gray-400 py-12">
                    新着動画がありません
                  </div>
                )}
              </div>
            )}

            {activeTab === 'channels' && (
              <div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {channels
                    .filter(
                      (channel) =>
                        !selectedCategory ||
                        channel.category?.id === selectedCategory
                    )
                    .map((channel) => (
                      <ChannelCard key={channel.id} channel={channel} />
                    ))}
                </div>

                {channels.length === 0 && (
                  <div className="text-center text-gray-400 py-12">
                    購読チャンネルがありません
                  </div>
                )}
              </div>
            )}
          </main>
        </div>
      </div>
    </div>
  );
}
