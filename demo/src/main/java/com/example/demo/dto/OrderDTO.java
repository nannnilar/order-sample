package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	
	private Long id;
	private long orderCode;
	private String customerName;
	private String customerPhone;
	private String address;
	private double totalPrice;
	private List<OrderItemDTO> orderList = new ArrayList<>();
	private List<OrderItemDetailDTO> orderItems = new ArrayList<>();
	
	public static OrderDTO mapToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomerName(order.getCustomerName());
        orderDTO.setCustomerPhone(order.getCustomerPhone());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setOrderCode(order.getOrderCode());
        orderItemList(order.getOrderItems());
//       orderDTO.setOrderList(orderItemListV1(order.getOrderItems()));
        return orderDTO;
    }
	
	public static List<OrderItemDetailDTO> orderItemList (List<OrderItem> orderItems) {
		List<OrderItemDetailDTO> orderItemDetailLists = new ArrayList<OrderItemDetailDTO>();
		for (OrderItem orderItemData : orderItems) {
			orderItemDetailLists.add(new OrderItemDetailDTO(
					orderItemData.getProduct().getId(),
					orderItemData.getProduct().getName(),
					orderItemData.getQuantity(),
					orderItemData.getProduct().getPrice()));
		}
		return orderItemDetailLists;
	}
	
	public static List<OrderItemDTO> orderItemListV1 (List<OrderItem> orderItems) {
		List<OrderItemDTO> orderItemLists = new ArrayList<OrderItemDTO>();
		for (OrderItem orderItemData : orderItems) {
			orderItemLists.add(new OrderItemDTO(
					orderItemData.getProduct().getId(),
					orderItemData.getQuantity())
				);	
		}
		return orderItemLists;
	}
	

	public OrderDTO(Long id, long orderCode, String customerName, String customerPhone, String address,
			double totalPrice, List<OrderItemDetailDTO> orderItems) {
		super();
		this.id = id;
		this.orderCode = orderCode;
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.address = address;
		this.totalPrice = totalPrice;
		this.orderItems = orderItems;
	}
	
	

}
