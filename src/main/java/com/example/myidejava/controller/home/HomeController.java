package com.example.myidejava.controller.home;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "홈", description = "")
public class HomeController {

    @GetMapping("/")
    @ApiResponse(responseCode = "204", description = "Hello Home")
    public ResponseEntity<String> home() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @ApiResponse(responseCode = "200", description = "Health Check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok().body("Pong");
    }
}
