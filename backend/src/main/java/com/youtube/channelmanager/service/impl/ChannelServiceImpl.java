package com.youtube.channelmanager.service.impl;

import com.youtube.channelmanager.dto.ChannelDTO;
import com.youtube.channelmanager.entity.UserChannelSubscription;
import com.youtube.channelmanager.entity.YoutubeChannel;
import com.youtube.channelmanager.mapper.UserChannelSubscriptionMapper;
import com.youtube.channelmanager.mapper.dto.ChannelDTOMapper;
import com.youtube.channelmanager.repository.UserChannelSubscriptionRepository;
import com.youtube.channelmanager.repository.YoutubeChannelRepository;
import com.youtube.channelmanager.service.ChannelService;
import com.youtube.channelmanager.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    private final YoutubeChannelRepository channelRepository;
    private final UserChannelSubscriptionRepository subscriptionRepository;
    private final UserChannelSubscriptionMapper subscriptionMapper;
    private final ChannelDTOMapper channelDTOMapper;
    private final YouTubeService youTubeService;

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDTO> getUserChannels(Long userId) {
        List<UserChannelSubscription> subscriptions = subscriptionMapper.findByUserIdWithDetails(userId);

        return subscriptions.stream()
            .map(sub -> {
                ChannelDTO dto = channelDTOMapper.toDTO(sub.getChannel());
                dto.setCustomName(sub.getCustomName());
                if (sub.getCategory() != null) {
                    dto.setCategory(com.youtube.channelmanager.dto.CategoryDTO.builder()
                        .id(sub.getCategoryId())
                        .name(sub.getCategory().getName())
                        .color(sub.getCategory().getColor())
                        .build());
                }
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDTO> getChannelsByCategory(Long userId, Long categoryId) {
        List<UserChannelSubscription> subscriptions = subscriptionMapper
            .findByUserIdAndCategoryId(userId, categoryId);

        return subscriptions.stream()
            .map(sub -> channelDTOMapper.toDTO(sub.getChannel()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChannelDTO subscribeChannel(Long userId, String youtubeChannelId, Long categoryId) {
        // YouTube APIからチャンネル情報を取得
        ChannelDTO channelInfo = youTubeService.getChannelInfo(youtubeChannelId);

        if (channelInfo == null) {
            throw new RuntimeException("Channel not found: " + youtubeChannelId);
        }

        // チャンネルがDBに存在するか確認
        YoutubeChannel channel = channelRepository.findByChannelId(youtubeChannelId)
            .orElseGet(() -> {
                // 新規チャンネルを保存
                YoutubeChannel newChannel = YoutubeChannel.builder()
                    .channelId(youtubeChannelId)
                    .title(channelInfo.getTitle())
                    .description(channelInfo.getDescription())
                    .thumbnailUrl(channelInfo.getThumbnailUrl())
                    .subscriberCount(channelInfo.getSubscriberCount())
                    .videoCount(channelInfo.getVideoCount())
                    .viewCount(channelInfo.getViewCount())
                    .build();
                return channelRepository.save(newChannel);
            });

        // 既に購読しているか確認
        if (subscriptionRepository.findByUserIdAndChannelId(userId, channel.getId()).isPresent()) {
            throw new RuntimeException("Already subscribed to this channel");
        }

        // 購読を作成
        UserChannelSubscription subscription = UserChannelSubscription.builder()
            .userId(userId)
            .channelId(channel.getId())
            .categoryId(categoryId)
            .build();

        subscriptionRepository.save(subscription);

        return channelDTOMapper.toDTO(channel);
    }

    @Override
    @Transactional
    public void unsubscribeChannel(Long userId, Long channelId) {
        subscriptionRepository.deleteByUserIdAndChannelId(userId, channelId);
    }

    @Override
    @Transactional
    public void updateChannelCategory(Long userId, Long channelId, Long categoryId) {
        UserChannelSubscription subscription = subscriptionRepository
            .findByUserIdAndChannelId(userId, channelId)
            .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setCategoryId(categoryId);
        subscriptionRepository.save(subscription);
    }
}
