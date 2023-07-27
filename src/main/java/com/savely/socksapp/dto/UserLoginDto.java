package com.savely.socksapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDto {
    @NotBlank(message = "fill in username field")
    private String username;
    @NotBlank(message = "fill in password field")
    private String password;
}
