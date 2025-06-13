package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.ImageData;
import com.example.service.ImageDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/image-data")
public class ImageDataController {

	private final ImageDataService imageDataService;

    public ImageDataController(ImageDataService imageDataService) {
        this.imageDataService = imageDataService;
    }
    
    @Operation(summary = "Lấy tất cả frame metadata theo itemId", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ImageData> getImageDataByItemId(@PathVariable Integer itemId) {
    	ImageData list = imageDataService.getByItemId(itemId);
        return ResponseEntity.ok(list);
    }
}
