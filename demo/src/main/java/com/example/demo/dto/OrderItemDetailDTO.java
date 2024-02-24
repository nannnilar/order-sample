package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetailDTO {
	
	private long productId; 
	private String productName;
	private int quantity;
	private double productPrice;
	
	
	
}
