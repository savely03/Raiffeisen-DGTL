package com.savely.socksapp.service.impl;

import com.savely.socksapp.dto.UserRegisterDto;
import com.savely.socksapp.entity.User;
import com.savely.socksapp.exception.UserAlreadyExistsException;
import com.savely.socksapp.mapper.UserMapper;
import com.savely.socksapp.repository.UserRepository;
import com.savely.socksapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userRegisterMapper;

    @Override
    public void login(String username, String password) {
        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
    }

    @Override
    public void register(UserRegisterDto userDto) {
        if (userRepository.existsByUsernameOrEmail(userDto.getUsername(), userDto.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        User user = userRegisterMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
