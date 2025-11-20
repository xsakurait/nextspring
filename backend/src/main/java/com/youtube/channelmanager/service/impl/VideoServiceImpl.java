package com.youtube.channelmanager.service.impl;

import com.youtube.channelmanager.dto.ChannelDTO;
import com.youtube.channelmanager.dto.VideoDTO;
import com.youtube.channelmanager.entity.UserChannelSubscription;
import com.youtube.channelmanager.entity.Video;
import com.youtube.channelmanager.entity.YoutubeChannel;
import com.youtube.channelmanager.mapper.VideoMapper;
import com.youtube.channelmanager.mapper.dto.VideoDTOMapper;
import com.youtube.channelmanager.repository.UserChannelSubscriptionRepository;
import com.youtube.channelmanager.repository.VideoRepository;
import com.youtube.channelmanager.repository.YoutubeChannelRepository;
import com.youtube.channelmanager.service.VideoService;
import com.youtube.channelmanager.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final VideoDTOMapper videoDTOMapper;
    private final UserChannelSubscriptionRepository subscriptionRepository;
    private final YoutubeChannelRepository channelRepository;
    private final YouTubeService youTubeService;

    @Override
    @Transactional(readOnly = true)
    public List<VideoDTO> getRecentVideosForUser(Long userId, int days, int limit) {
        LocalDateTime publishedAfter = LocalDateTime.now().minusDays(days);
        List<Video> videos = videoMapper.findRecentVideosByUserId(userId, publishedAfter, limit);
        return videoDTOMapper.toDTOList(videos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoDTO> getRecentVideosByCategory(Long userId, Long categoryId, int days, int limit) {
        LocalDateTime publishedAfter = LocalDateTime.now().minusDays(days);
        List<Video> videos = videoMapper.findRecentVideosByUserIdAndCategoryId(
            userId, categoryId, publishedAfter, limit
        );
        return videoDTOMapper.toDTOList(videos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoDTO> getRecentVideosWithSort(Long userId, int days, String sortBy, int limit) {
        LocalDateTime publishedAfter = LocalDateTime.now().minusDays(days);

        // ユーザーの購読チャンネルを取得
        List<UserChannelSubscription> subscriptions = subscriptionRepository.findByUserId(userId);
        List<Long> channelIds = subscriptions.stream()
            .map(UserChannelSubscription::getChannelId)
            .collect(Collectors.toList());

        if (channelIds.isEmpty()) {
            return List.of();
        }

        List<Video> videos = videoMapper.findVideosByChannelIdsWithSort(
            channelIds, publishedAfter, sortBy
        );

        return videos.stream()
            .limit(limit)
            .map(videoDTOMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void syncLatestVideos(Long userId) {
        log.info("Syncing latest videos for user: {}", userId);

        // ユーザーの購読チャンネルを取得
        List<UserChannelSubscription> subscriptions = subscriptionRepository
            .findByUserIdWithChannel(userId);

        for (UserChannelSubscription subscription : subscriptions) {
            try {
                YoutubeChannel channel = subscription.getChannel();

                // YouTube APIから最新動画を取得
                List<VideoDTO> latestVideos = youTubeService.getLatestVideos(
                    channel.getChannelId(), 10
                );

                // データベースに保存
                for (VideoDTO videoDTO : latestVideos) {
                    Video existingVideo = videoRepository.findByVideoId(videoDTO.getVideoId())
                        .orElse(null);

                    if (existingVideo == null) {
                        // 新規動画を保存
                        Video newVideo = Video.builder()
                            .videoId(videoDTO.getVideoId())
                            .channelId(channel.getId())
                            .title(videoDTO.getTitle())
                            .description(videoDTO.getDescription())
                            .thumbnailUrl(videoDTO.getThumbnailUrl())
                            .publishedAt(videoDTO.getPublishedAt())
                            .duration(videoDTO.getDuration())
                            .viewCount(videoDTO.getViewCount())
                            .likeCount(videoDTO.getLikeCount())
                            .commentCount(videoDTO.getCommentCount())
                            .build();

                        videoRepository.save(newVideo);
                        log.debug("Saved new video: {}", videoDTO.getTitle());
                    } else {
                        // 既存動画の統計を更新
                        existingVideo.setViewCount(videoDTO.getViewCount());
                        existingVideo.setLikeCount(videoDTO.getLikeCount());
                        existingVideo.setCommentCount(videoDTO.getCommentCount());
                        videoRepository.save(existingVideo);
                    }
                }

                // チャンネル情報も更新
                ChannelDTO channelInfo = youTubeService.getChannelInfo(channel.getChannelId());
                if (channelInfo != null) {
                    channel.setSubscriberCount(channelInfo.getSubscriberCount());
                    channel.setVideoCount(channelInfo.getVideoCount());
                    channel.setViewCount(channelInfo.getViewCount());
                    channelRepository.save(channel);
                }

            } catch (Exception e) {
                log.error("Error syncing videos for channel: {}",
                    subscription.getChannel().getTitle(), e);
            }
        }

        log.info("Finished syncing videos for user: {}", userId);
    }

    @Override
    @Transactional
    public VideoDTO updateVideoStats(String videoId) {
        VideoDTO videoDTO = youTubeService.getVideoDetails(videoId);

        if (videoDTO == null) {
            return null;
        }

        Video video = videoRepository.findByVideoId(videoId).orElse(null);

        if (video != null) {
            video.setViewCount(videoDTO.getViewCount());
            video.setLikeCount(videoDTO.getLikeCount());
            video.setCommentCount(videoDTO.getCommentCount());
            videoRepository.save(video);
        }

        return videoDTO;
    }
}
