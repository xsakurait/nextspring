package com.youtube.channelmanager.repository;

import com.youtube.channelmanager.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByVideoId(String videoId);

    List<Video> findByChannelIdOrderByPublishedAtDesc(Long channelId);

    List<Video> findByChannelIdAndPublishedAtAfterOrderByPublishedAtDesc(
        Long channelId, LocalDateTime publishedAfter);

    @Query("SELECT v FROM Video v WHERE v.channelId IN :channelIds ORDER BY v.publishedAt DESC")
    List<Video> findByChannelIdsOrderByPublishedAtDesc(@Param("channelIds") List<Long> channelIds);

    @Query("SELECT v FROM Video v WHERE v.channelId IN :channelIds AND v.publishedAt > :publishedAfter ORDER BY v.publishedAt DESC")
    List<Video> findRecentVideosByChannelIds(
        @Param("channelIds") List<Long> channelIds,
        @Param("publishedAfter") LocalDateTime publishedAfter);
}
