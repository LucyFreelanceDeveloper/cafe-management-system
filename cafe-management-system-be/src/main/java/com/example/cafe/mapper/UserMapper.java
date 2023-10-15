package com.example.cafe.mapper;

import com.example.cafe.model.dto.UserDto;
import com.example.cafe.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userEntityToUserDto(UserEntity userEntity);
}
