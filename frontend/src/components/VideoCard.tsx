'use client';

import { useState } from 'react';
import Image from 'next/image';
import type { Video } from '@/types';
import { formatDistanceToNow } from 'date-fns';
import { ja } from 'date-fns/locale';

interface VideoCardProps {
  video: Video;
}

export default function VideoCard({ video }: VideoCardProps) {
  const [showPopup, setShowPopup] = useState(false);

  const formatNumber = (num: number): string => {
    if (num >= 1000000) {
      return `${(num / 1000000).toFixed(1)}M`;
    } else if (num >= 1000) {
      return `${(num / 1000).toFixed(1)}K`;
    }
    return num.toString();
  };

  const formatDuration = (duration: string): string => {
    const match = duration.match(/PT(\d+H)?(\d+M)?(\d+S)?/);
    if (!match) return '';

    const hours = match[1] ? parseInt(match[1]) : 0;
    const minutes = match[2] ? parseInt(match[2]) : 0;
    const seconds = match[3] ? parseInt(match[3]) : 0;

    if (hours > 0) {
      return `${hours}:${minutes.toString().padStart(2, '0')}:${seconds
        .toString()
        .padStart(2, '0')}`;
    }
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  return (
    <div
      className="relative group cursor-pointer"
      onMouseEnter={() => setShowPopup(true)}
      onMouseLeave={() => setShowPopup(false)}
    >
      <div className="bg-gray-800 rounded-lg overflow-hidden transition-all duration-300 hover:scale-105 hover:shadow-2xl">
        {/* サムネイル */}
        <div className="relative aspect-video">
          <Image
            src={video.thumbnailUrl}
            alt={video.title}
            fill
            className="object-cover"
          />
          {/* 再生時間 */}
          <div className="absolute bottom-2 right-2 bg-black bg-opacity-80 text-white text-xs px-2 py-1 rounded">
            {formatDuration(video.duration)}
          </div>
        </div>

        {/* 動画情報 */}
        <div className="p-4">
          <h3 className="text-white font-semibold line-clamp-2 mb-2">
            {video.title}
          </h3>

          {video.channel && (
            <p className="text-gray-400 text-sm mb-2">{video.channel.title}</p>
          )}

          <div className="flex items-center gap-4 text-gray-400 text-sm">
            <span>{formatNumber(video.viewCount)} 回視聴</span>
            <span>
              {formatDistanceToNow(new Date(video.publishedAt), {
                addSuffix: true,
                locale: ja,
              })}
            </span>
          </div>

          <div className="flex items-center gap-4 mt-2 text-gray-400 text-sm">
            <span>👍 {formatNumber(video.likeCount)}</span>
            <span>💬 {formatNumber(video.commentCount)}</span>
          </div>
        </div>
      </div>

      {/* ホバーポップアップ */}
      {showPopup && (
        <div className="absolute z-50 top-0 left-full ml-4 w-96 bg-gray-900 rounded-lg shadow-2xl p-6 animate-scale-in">
          <div className="space-y-4">
            <h3 className="text-white font-bold text-lg">{video.title}</h3>

            {video.channel && (
              <div className="flex items-center gap-3">
                {video.channel.thumbnailUrl && (
                  <Image
                    src={video.channel.thumbnailUrl}
                    alt={video.channel.title}
                    width={48}
                    height={48}
                    className="rounded-full"
                  />
                )}
                <div>
                  <p className="text-white font-semibold">
                    {video.channel.title}
                  </p>
                  <p className="text-gray-400 text-sm">
                    {formatNumber(video.channel.subscriberCount)} 登録者
                  </p>
                </div>
              </div>
            )}

            <p className="text-gray-300 text-sm line-clamp-4">
              {video.description}
            </p>

            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <p className="text-gray-400">視聴回数</p>
                <p className="text-white font-semibold">
                  {formatNumber(video.viewCount)}
                </p>
              </div>
              <div>
                <p className="text-gray-400">高評価</p>
                <p className="text-white font-semibold">
                  {formatNumber(video.likeCount)}
                </p>
              </div>
              <div>
                <p className="text-gray-400">コメント</p>
                <p className="text-white font-semibold">
                  {formatNumber(video.commentCount)}
                </p>
              </div>
              <div>
                <p className="text-gray-400">公開日</p>
                <p className="text-white font-semibold">
                  {new Date(video.publishedAt).toLocaleDateString('ja-JP')}
                </p>
              </div>
            </div>

            <a
              href={`https://www.youtube.com/watch?v=${video.videoId}`}
              target="_blank"
              rel="noopener noreferrer"
              className="block w-full bg-red-600 hover:bg-red-700 text-white text-center py-2 rounded-lg transition-colors"
            >
              YouTubeで視聴
            </a>
          </div>
        </div>
      )}
    </div>
  );
}
