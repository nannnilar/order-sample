package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CategoryDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private String name;
	 private double price;
	 private int quantity;
	 private String description;
//	 private byte[] imageData;
//	 private MultipartFile imageFile;
	 
	private String imageName;
	private String filePath;
	private String status = "active";
	 

	 @ManyToOne
	 @JoinColumn(name = "category_id")
	    private Category category;
	 
	public Product(Long id, String name, double price, int quantity) {
	super();
	this.id = id;
	this.name = name;
	this.price = price;
	this.quantity = quantity;
}

	public Product(Long id, String name, double price, int quantity, String description) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.description = description;
	}

	
	
	 

}
