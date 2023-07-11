package com.savely.socksapp.mapper;

public interface GeneralMapper<D, E> {

    D toDto(E entity);

    E toEntity(D dto);
}
