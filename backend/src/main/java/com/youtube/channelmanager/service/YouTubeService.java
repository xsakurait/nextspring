package com.youtube.channelmanager.service;

import com.youtube.channelmanager.dto.ChannelDTO;
import com.youtube.channelmanager.dto.VideoDTO;
import java.util.List;

public interface YouTubeService {

    /**
     * YouTube APIからチャンネル情報を取得
     */
    ChannelDTO getChannelInfo(String channelId);

    /**
     * YouTube APIから指定チャンネルの最新動画を取得
     */
    List<VideoDTO> getLatestVideos(String channelId, int maxResults);

    /**
     * YouTube APIから動画詳細を取得
     */
    VideoDTO getVideoDetails(String videoId);

    /**
     * 複数チャンネルの最新動画を取得
     */
    List<VideoDTO> getLatestVideosFromMultipleChannels(List<String> channelIds, int maxResults);
}
