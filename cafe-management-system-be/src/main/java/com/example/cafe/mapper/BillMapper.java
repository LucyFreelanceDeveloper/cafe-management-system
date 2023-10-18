package com.example.cafe.mapper;

import com.example.cafe.model.dto.BillDto;
import com.example.cafe.model.entity.BillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    BillDto billEntityToBillDto(BillEntity billEntity);

    BillEntity billDtoToBillEntity(BillDto billDto);
}