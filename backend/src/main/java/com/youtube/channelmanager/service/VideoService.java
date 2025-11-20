package com.youtube.channelmanager.service;

import com.youtube.channelmanager.dto.VideoDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface VideoService {

    /**
     * ユーザーの購読チャンネルの新着動画を取得
     */
    List<VideoDTO> getRecentVideosForUser(Long userId, int days, int limit);

    /**
     * カテゴリ別の新着動画を取得
     */
    List<VideoDTO> getRecentVideosByCategory(Long userId, Long categoryId, int days, int limit);

    /**
     * 新着動画をソートして取得
     */
    List<VideoDTO> getRecentVideosWithSort(Long userId, int days, String sortBy, int limit);

    /**
     * YouTube APIから最新動画を同期
     */
    void syncLatestVideos(Long userId);

    /**
     * 動画情報を更新
     */
    VideoDTO updateVideoStats(String videoId);
}
