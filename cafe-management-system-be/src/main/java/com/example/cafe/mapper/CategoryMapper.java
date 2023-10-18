package com.example.cafe.mapper;


import com.example.cafe.model.dto.CategoryDto;
import com.example.cafe.model.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto categoryEntityToCategoryDto(CategoryEntity categoryEntity);

    CategoryEntity categoryDtoToCategoryEntity(CategoryDto categoryDto);
}
