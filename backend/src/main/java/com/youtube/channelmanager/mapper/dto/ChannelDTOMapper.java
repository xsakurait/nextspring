package com.youtube.channelmanager.mapper.dto;

import com.youtube.channelmanager.dto.ChannelDTO;
import com.youtube.channelmanager.entity.YoutubeChannel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryDTOMapper.class})
public interface ChannelDTOMapper {

    ChannelDTOMapper INSTANCE = Mappers.getMapper(ChannelDTOMapper.class);

    ChannelDTO toDTO(YoutubeChannel channel);

    List<ChannelDTO> toDTOList(List<YoutubeChannel> channels);

    YoutubeChannel toEntity(ChannelDTO channelDTO);
}
