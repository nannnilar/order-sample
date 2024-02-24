package com.example.demo.dto;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Product;
import com.example.demo.repo.CategoryRepo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {
	 private Long id;
	 private String name;
	 private double price;
	 private int quantity;
	 private String description;
	 private Long categoryId;
	 private String filePath;
//	 private byte[] imageData;
	 private String imageName;
//	 private MultipartFile imageFile;
	 
	 private String status = "active";
	 
public ProductDTO(Long id, String name, double price, int quantity, Long categoryId) {
	super();
	this.id = id;
	this.name = name;
	this.price = price;
	this.quantity = quantity;
	this.categoryId = categoryId;
}


public ProductDTO(String name, double price, int quantity, Long categoryId) {
	super();
	this.name = name;
	this.price = price;
	this.quantity = quantity;
	this.categoryId = categoryId;
}


public ProductDTO(Long id, String name, double price, int quantity, String description, Long categoryId,
		String filePath, String imageName) {
	super();
	this.id = id;
	this.name = name;
	this.price = price;
	this.quantity = quantity;
	this.description = description;
	this.categoryId = categoryId;
	this.filePath = filePath;
	this.imageName = imageName;
	
}

















	
	
	 
	 

}
