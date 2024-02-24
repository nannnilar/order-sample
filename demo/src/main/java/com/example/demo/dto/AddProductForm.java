package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

public record AddProductForm(
		
		String name,
		double price,
		int quantity,
		String description,
		MultipartFile file,
		Long categoryId
		) {

	

}
