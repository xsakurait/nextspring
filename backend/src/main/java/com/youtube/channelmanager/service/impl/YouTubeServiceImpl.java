package com.youtube.channelmanager.service.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.youtube.channelmanager.dto.ChannelDTO;
import com.youtube.channelmanager.dto.VideoDTO;
import com.youtube.channelmanager.service.YouTubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class YouTubeServiceImpl implements YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.application-name}")
    private String applicationName;

    private YouTube getYouTubeService() throws Exception {
        return new YouTube.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            null
        )
        .setApplicationName(applicationName)
        .build();
    }

    @Override
    public ChannelDTO getChannelInfo(String channelId) {
        try {
            YouTube youtube = getYouTubeService();
            YouTube.Channels.List request = youtube.channels()
                .list(List.of("snippet", "statistics"))
                .setKey(apiKey)
                .setId(List.of(channelId));

            ChannelListResponse response = request.execute();

            if (response.getItems() == null || response.getItems().isEmpty()) {
                log.warn("Channel not found: {}", channelId);
                return null;
            }

            Channel channel = response.getItems().get(0);
            ChannelSnippet snippet = channel.getSnippet();
            ChannelStatistics stats = channel.getStatistics();

            return ChannelDTO.builder()
                .channelId(channelId)
                .title(snippet.getTitle())
                .description(snippet.getDescription())
                .thumbnailUrl(snippet.getThumbnails().getHigh().getUrl())
                .subscriberCount(stats.getSubscriberCount() != null ? stats.getSubscriberCount().longValue() : 0L)
                .videoCount(stats.getVideoCount() != null ? stats.getVideoCount().intValue() : 0)
                .viewCount(stats.getViewCount() != null ? stats.getViewCount().longValue() : 0L)
                .build();

        } catch (Exception e) {
            log.error("Error fetching channel info for: {}", channelId, e);
            return null;
        }
    }

    @Override
    public List<VideoDTO> getLatestVideos(String channelId, int maxResults) {
        try {
            YouTube youtube = getYouTubeService();

            // チャンネルの動画を検索
            YouTube.Search.List searchRequest = youtube.search()
                .list(List.of("id", "snippet"))
                .setKey(apiKey)
                .setChannelId(channelId)
                .setOrder("date")
                .setType(List.of("video"))
                .setMaxResults((long) maxResults);

            SearchListResponse searchResponse = searchRequest.execute();

            if (searchResponse.getItems() == null || searchResponse.getItems().isEmpty()) {
                return new ArrayList<>();
            }

            // 動画IDのリストを取得
            List<String> videoIds = searchResponse.getItems().stream()
                .map(item -> item.getId().getVideoId())
                .collect(Collectors.toList());

            // 動画の詳細情報を取得
            return getVideoDetailsByIds(videoIds);

        } catch (Exception e) {
            log.error("Error fetching latest videos for channel: {}", channelId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public VideoDTO getVideoDetails(String videoId) {
        List<VideoDTO> videos = getVideoDetailsByIds(List.of(videoId));
        return videos.isEmpty() ? null : videos.get(0);
    }

    @Override
    public List<VideoDTO> getLatestVideosFromMultipleChannels(List<String> channelIds, int maxResults) {
        List<VideoDTO> allVideos = new ArrayList<>();

        for (String channelId : channelIds) {
            List<VideoDTO> channelVideos = getLatestVideos(channelId, maxResults);
            allVideos.addAll(channelVideos);
        }

        // 公開日時でソート
        allVideos.sort((v1, v2) -> v2.getPublishedAt().compareTo(v1.getPublishedAt()));

        return allVideos;
    }

    private List<VideoDTO> getVideoDetailsByIds(List<String> videoIds) {
        try {
            YouTube youtube = getYouTubeService();

            YouTube.Videos.List videoRequest = youtube.videos()
                .list(List.of("snippet", "statistics", "contentDetails"))
                .setKey(apiKey)
                .setId(videoIds);

            VideoListResponse videoResponse = videoRequest.execute();

            if (videoResponse.getItems() == null) {
                return new ArrayList<>();
            }

            return videoResponse.getItems().stream()
                .map(this::convertToVideoDTO)
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching video details", e);
            return new ArrayList<>();
        }
    }

    private VideoDTO convertToVideoDTO(Video video) {
        VideoSnippet snippet = video.getSnippet();
        VideoStatistics stats = video.getStatistics();
        VideoContentDetails contentDetails = video.getContentDetails();

        LocalDateTime publishedAt = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(snippet.getPublishedAt().getValue()),
            ZoneId.systemDefault()
        );

        return VideoDTO.builder()
            .videoId(video.getId())
            .title(snippet.getTitle())
            .description(snippet.getDescription())
            .thumbnailUrl(snippet.getThumbnails().getHigh().getUrl())
            .publishedAt(publishedAt)
            .duration(contentDetails.getDuration())
            .viewCount(stats.getViewCount() != null ? stats.getViewCount().longValue() : 0L)
            .likeCount(stats.getLikeCount() != null ? stats.getLikeCount().longValue() : 0L)
            .commentCount(stats.getCommentCount() != null ? stats.getCommentCount().intValue() : 0)
            .build();
    }
}
