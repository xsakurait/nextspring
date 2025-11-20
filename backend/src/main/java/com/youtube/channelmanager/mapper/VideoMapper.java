package com.youtube.channelmanager.mapper;

import com.youtube.channelmanager.entity.Video;
import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VideoMapper {

    @Select("SELECT * FROM videos WHERE video_id = #{videoId}")
    Video findByVideoId(@Param("videoId") String videoId);

    @Select("SELECT * FROM videos WHERE channel_id = #{channelId} ORDER BY published_at DESC")
    List<Video> findByChannelId(@Param("channelId") Long channelId);

    @Select("SELECT v.* FROM videos v " +
            "INNER JOIN user_channel_subscriptions ucs ON v.channel_id = ucs.channel_id " +
            "WHERE ucs.user_id = #{userId} " +
            "AND v.published_at > #{publishedAfter} " +
            "ORDER BY v.published_at DESC " +
            "LIMIT #{limit}")
    List<Video> findRecentVideosByUserId(
        @Param("userId") Long userId,
        @Param("publishedAfter") LocalDateTime publishedAfter,
        @Param("limit") Integer limit
    );

    @Select("SELECT v.* FROM videos v " +
            "INNER JOIN user_channel_subscriptions ucs ON v.channel_id = ucs.channel_id " +
            "WHERE ucs.user_id = #{userId} " +
            "AND ucs.category_id = #{categoryId} " +
            "AND v.published_at > #{publishedAfter} " +
            "ORDER BY v.published_at DESC " +
            "LIMIT #{limit}")
    List<Video> findRecentVideosByUserIdAndCategoryId(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("publishedAfter") LocalDateTime publishedAfter,
        @Param("limit") Integer limit
    );

    @Select("<script>" +
            "SELECT v.* FROM videos v " +
            "WHERE v.channel_id IN " +
            "<foreach item='channelId' collection='channelIds' open='(' separator=',' close=')'>" +
            "#{channelId}" +
            "</foreach> " +
            "AND v.published_at > #{publishedAfter} " +
            "ORDER BY " +
            "<choose>" +
            "<when test='sortBy == \"viewCount\"'>v.view_count DESC</when>" +
            "<when test='sortBy == \"likeCount\"'>v.like_count DESC</when>" +
            "<otherwise>v.published_at DESC</otherwise>" +
            "</choose>" +
            "</script>")
    List<Video> findVideosByChannelIdsWithSort(
        @Param("channelIds") List<Long> channelIds,
        @Param("publishedAfter") LocalDateTime publishedAfter,
        @Param("sortBy") String sortBy
    );

    @Insert("INSERT INTO videos (video_id, channel_id, title, description, thumbnail_url, " +
            "published_at, duration, view_count, like_count, comment_count) " +
            "VALUES (#{videoId}, #{channelId}, #{title}, #{description}, #{thumbnailUrl}, " +
            "#{publishedAt}, #{duration}, #{viewCount}, #{likeCount}, #{commentCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Video video);

    @Update("UPDATE videos SET " +
            "title = #{title}, " +
            "description = #{description}, " +
            "view_count = #{viewCount}, " +
            "like_count = #{likeCount}, " +
            "comment_count = #{commentCount}, " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(Video video);

    @Delete("DELETE FROM videos WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
