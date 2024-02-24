package com.example.demo.dto;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public record UpdateProductForm(
		
		String name,
		double price,
		int quantity,
		String description,
		Optional<MultipartFile> file,
		Long categoryId
		) {

}