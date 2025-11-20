package com.youtube.channelmanager.mapper.dto;

import com.youtube.channelmanager.dto.CategoryDTO;
import com.youtube.channelmanager.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryDTOMapper {

    CategoryDTOMapper INSTANCE = Mappers.getMapper(CategoryDTOMapper.class);

    CategoryDTO toDTO(Category category);

    List<CategoryDTO> toDTOList(List<Category> categories);

    Category toEntity(CategoryDTO categoryDTO);
}
