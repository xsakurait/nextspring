package com.youtube.channelmanager.repository;

import com.youtube.channelmanager.entity.YoutubeChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {

    Optional<YoutubeChannel> findByChannelId(String channelId);

    Boolean existsByChannelId(String channelId);
}
