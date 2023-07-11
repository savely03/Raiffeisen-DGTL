package com.savely.socksapp.controller;

import com.savely.socksapp.dto.SockDto;
import com.savely.socksapp.service.SockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socks")
@RequiredArgsConstructor
public class SockController {

    private final SockService sockService;

    @PostMapping("/income")
    public ResponseEntity<Void> income(@Valid @RequestBody SockDto sockDto) {
        sockService.income(sockDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/outcome")
    public ResponseEntity<Void> outcome(@Valid @RequestBody SockDto sockDto) {
        sockService.outcome(sockDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<SockDto>> getSocks(@RequestParam String color,
                                                  @RequestParam String operation,
                                                  @RequestParam int cottonPart) {
        return ResponseEntity.ok(sockService.getSocks(color, operation, cottonPart));
    }


    @GetMapping("/equal")
    public ResponseEntity<SockDto> getSocksEqual(@RequestParam String color, @RequestParam int cottonPart) {
        return ResponseEntity.ok(sockService.getSockEqual(color, cottonPart));
    }

    @GetMapping
    public ResponseEntity<List<SockDto>> getSocks(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(sockService.getSocks(page, size));
    }

}
