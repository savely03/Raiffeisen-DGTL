package com.savely.socksapp.service;

import com.savely.socksapp.dto.UserRegisterDto;

public interface AuthService {

    void login(String username, String password);

    void register(UserRegisterDto userDto);
}
