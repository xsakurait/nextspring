package com.youtube.channelmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {
    private Long id;
    private String videoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;
    private String duration;
    private Long viewCount;
    private Long likeCount;
    private Integer commentCount;
    private ChannelDTO channel;
}
