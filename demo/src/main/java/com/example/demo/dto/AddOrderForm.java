package com.example.demo.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddOrderForm {
	
	private String customerName;
	private String customerPhone;
	private String address;
	private List<OrderItemDTO> orderItems;

}
