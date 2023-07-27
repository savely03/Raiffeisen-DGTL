package com.savely.socksapp.mapper;

import com.savely.socksapp.dto.UserDto;
import com.savely.socksapp.dto.UserRegisterDto;
import com.savely.socksapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends GeneralMapper<UserDto, User> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "isActive", constant = "true")
    User toEntity(UserRegisterDto userRegisterDto);
}
