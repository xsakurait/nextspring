package com.youtube.channelmanager.mapper;

import com.youtube.channelmanager.entity.UserChannelSubscription;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserChannelSubscriptionMapper {

    @Select("SELECT ucs.*, " +
            "yc.channel_id as channel_channel_id, " +
            "yc.title as channel_title, " +
            "yc.description as channel_description, " +
            "yc.thumbnail_url as channel_thumbnail_url, " +
            "yc.subscriber_count as channel_subscriber_count, " +
            "c.name as category_name, " +
            "c.color as category_color " +
            "FROM user_channel_subscriptions ucs " +
            "INNER JOIN youtube_channels yc ON ucs.channel_id = yc.id " +
            "LEFT JOIN categories c ON ucs.category_id = c.id " +
            "WHERE ucs.user_id = #{userId} " +
            "ORDER BY ucs.subscribed_at DESC")
    @Results(id = "subscriptionWithDetails", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "channelId", column = "channel_id"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "customName", column = "custom_name"),
        @Result(property = "notes", column = "notes"),
        @Result(property = "subscribedAt", column = "subscribed_at"),
        @Result(property = "channel.channelId", column = "channel_channel_id"),
        @Result(property = "channel.title", column = "channel_title"),
        @Result(property = "channel.description", column = "channel_description"),
        @Result(property = "channel.thumbnailUrl", column = "channel_thumbnail_url"),
        @Result(property = "channel.subscriberCount", column = "channel_subscriber_count"),
        @Result(property = "category.name", column = "category_name"),
        @Result(property = "category.color", column = "category_color")
    })
    List<UserChannelSubscription> findByUserIdWithDetails(@Param("userId") Long userId);

    @Select("SELECT ucs.*, " +
            "yc.channel_id as channel_channel_id, " +
            "yc.title as channel_title, " +
            "yc.thumbnail_url as channel_thumbnail_url, " +
            "yc.subscriber_count as channel_subscriber_count " +
            "FROM user_channel_subscriptions ucs " +
            "INNER JOIN youtube_channels yc ON ucs.channel_id = yc.id " +
            "WHERE ucs.user_id = #{userId} AND ucs.category_id = #{categoryId} " +
            "ORDER BY yc.subscriber_count DESC")
    @ResultMap("subscriptionWithDetails")
    List<UserChannelSubscription> findByUserIdAndCategoryId(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId
    );

    @Insert("INSERT INTO user_channel_subscriptions " +
            "(user_id, channel_id, category_id, custom_name, notes) " +
            "VALUES (#{userId}, #{channelId}, #{categoryId}, #{customName}, #{notes})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserChannelSubscription subscription);

    @Update("UPDATE user_channel_subscriptions SET " +
            "category_id = #{categoryId}, " +
            "custom_name = #{customName}, " +
            "notes = #{notes} " +
            "WHERE id = #{id}")
    int update(UserChannelSubscription subscription);

    @Delete("DELETE FROM user_channel_subscriptions WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Delete("DELETE FROM user_channel_subscriptions WHERE user_id = #{userId} AND channel_id = #{channelId}")
    int deleteByUserIdAndChannelId(@Param("userId") Long userId, @Param("channelId") Long channelId);
}
