package com.savely.socksapp.mapper;

import com.savely.socksapp.dto.SockDto;
import com.savely.socksapp.entity.Sock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SockMapper extends GeneralMapper<SockDto, Sock> {
}
