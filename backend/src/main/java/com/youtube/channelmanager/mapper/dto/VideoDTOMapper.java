package com.youtube.channelmanager.mapper.dto;

import com.youtube.channelmanager.dto.VideoDTO;
import com.youtube.channelmanager.entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ChannelDTOMapper.class})
public interface VideoDTOMapper {

    VideoDTOMapper INSTANCE = Mappers.getMapper(VideoDTOMapper.class);

    @Mapping(source = "channel", target = "channel")
    VideoDTO toDTO(Video video);

    List<VideoDTO> toDTOList(List<Video> videos);

    Video toEntity(VideoDTO videoDTO);
}
