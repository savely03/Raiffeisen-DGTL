package com.savely.socksapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SockDto {


    private Long id;

    @NotBlank(message = "Не должно быть NotBlank")
    private String color;

    @Min(value = 0, message = "Должно быть >= 0")
    @Max(value = 100, message = "Должно быть <= 0")
    private int cottonPart;

    @Min(value = 1, message = "Должно быть > 0")
    private int quantity;
}
