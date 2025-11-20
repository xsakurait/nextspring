'use client';

import { useState } from 'react';
import Image from 'next/image';
import type { Channel } from '@/types';

interface ChannelCardProps {
  channel: Channel;
  onCategoryChange?: (channelId: number, categoryId: number) => void;
  onUnsubscribe?: (channelId: number) => void;
}

export default function ChannelCard({
  channel,
  onCategoryChange,
  onUnsubscribe,
}: ChannelCardProps) {
  const [showPopup, setShowPopup] = useState(false);

  const formatNumber = (num: number): string => {
    if (num >= 1000000) {
      return `${(num / 1000000).toFixed(1)}M`;
    } else if (num >= 1000) {
      return `${(num / 1000).toFixed(1)}K`;
    }
    return num.toString();
  };

  return (
    <div
      className="relative group"
      onMouseEnter={() => setShowPopup(true)}
      onMouseLeave={() => setShowPopup(false)}
    >
      <div className="bg-gray-800 rounded-lg p-4 transition-all duration-300 hover:scale-105 hover:shadow-xl cursor-pointer">
        <div className="flex items-center gap-4">
          <Image
            src={channel.thumbnailUrl}
            alt={channel.title}
            width={64}
            height={64}
            className="rounded-full"
          />

          <div className="flex-1 min-w-0">
            <h3 className="text-white font-semibold truncate">
              {channel.customName || channel.title}
            </h3>
            <p className="text-gray-400 text-sm">
              {formatNumber(channel.subscriberCount)} 登録者
            </p>
            {channel.category && (
              <div
                className="inline-block mt-1 px-2 py-1 rounded text-xs text-white"
                style={{ backgroundColor: channel.category.color }}
              >
                {channel.category.name}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* ホバーポップアップ */}
      {showPopup && (
        <div className="absolute z-50 top-0 left-full ml-4 w-80 bg-gray-900 rounded-lg shadow-2xl p-6 animate-scale-in">
          <div className="space-y-4">
            <div className="flex items-center gap-4">
              <Image
                src={channel.thumbnailUrl}
                alt={channel.title}
                width={80}
                height={80}
                className="rounded-full"
              />
              <div>
                <h3 className="text-white font-bold text-lg">
                  {channel.title}
                </h3>
                <p className="text-gray-400">
                  {formatNumber(channel.subscriberCount)} 登録者
                </p>
              </div>
            </div>

            <p className="text-gray-300 text-sm line-clamp-3">
              {channel.description}
            </p>

            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <p className="text-gray-400">動画数</p>
                <p className="text-white font-semibold">
                  {formatNumber(channel.videoCount)}
                </p>
              </div>
              <div>
                <p className="text-gray-400">総再生回数</p>
                <p className="text-white font-semibold">
                  {formatNumber(channel.viewCount)}
                </p>
              </div>
            </div>

            <div className="flex gap-2">
              <a
                href={`https://www.youtube.com/channel/${channel.channelId}`}
                target="_blank"
                rel="noopener noreferrer"
                className="flex-1 bg-red-600 hover:bg-red-700 text-white text-center py-2 rounded-lg transition-colors text-sm"
              >
                チャンネルを開く
              </a>

              {onUnsubscribe && (
                <button
                  onClick={() => onUnsubscribe(channel.id)}
                  className="px-4 bg-gray-700 hover:bg-gray-600 text-white rounded-lg transition-colors text-sm"
                >
                  購読解除
                </button>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
