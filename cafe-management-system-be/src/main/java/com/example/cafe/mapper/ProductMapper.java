package com.example.cafe.mapper;

import com.example.cafe.model.dto.ProductDto;
import com.example.cafe.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto productEntityToProductDto(ProductEntity productEntity);

    ProductEntity productDtoToProductEntity(ProductDto productDto);

}