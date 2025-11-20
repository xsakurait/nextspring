package com.youtube.channelmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {
    private Long id;
    private String channelId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long subscriberCount;
    private Integer videoCount;
    private Long viewCount;
    private String customName;
    private CategoryDTO category;
}
