package com.savely.socksapp.service;

import com.savely.socksapp.dto.SockDto;

import java.util.List;

public interface SockService {
    void income(SockDto sockDto);

    void outcome(SockDto sockDto);

    List<SockDto> getSocks(String color, String operation, int cottonPart);

    List<SockDto> getSocks(int page, int size);

    SockDto getSockEqual(String color, int cottonPart);
}
