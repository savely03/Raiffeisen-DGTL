package com.savely.socksapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import static com.savely.socksapp.constants.Regex.EMAIL;

@Data
@Builder
public class UserRegisterDto {
    @NotBlank(message = "fill in username field")
    private String username;
    @NotBlank(message = "fill in password field")
    private String password;
    @Email(regexp = EMAIL, message = "fill in email field")
    private String email;
}
