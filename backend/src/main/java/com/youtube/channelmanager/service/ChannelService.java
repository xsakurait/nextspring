package com.youtube.channelmanager.service;

import com.youtube.channelmanager.dto.ChannelDTO;
import java.util.List;

public interface ChannelService {

    /**
     * ユーザーの購読チャンネル一覧を取得
     */
    List<ChannelDTO> getUserChannels(Long userId);

    /**
     * カテゴリ別のチャンネル一覧を取得
     */
    List<ChannelDTO> getChannelsByCategory(Long userId, Long categoryId);

    /**
     * チャンネルを購読
     */
    ChannelDTO subscribeChannel(Long userId, String channelId, Long categoryId);

    /**
     * チャンネルの購読を解除
     */
    void unsubscribeChannel(Long userId, Long channelId);

    /**
     * チャンネルのカテゴリを変更
     */
    void updateChannelCategory(Long userId, Long channelId, Long categoryId);
}
