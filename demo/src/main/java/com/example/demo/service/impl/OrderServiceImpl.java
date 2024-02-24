package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AddOrderForm;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderItemDetailDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.ProductRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {
	@Autowired
	private final OrderRepo orderRepo;
	@Autowired
	private final ProductRepo productRepo;
	
	public List<Order> findAll() {
		return orderRepo.findAll();
	}
	
	public Order create(AddOrderForm orderRequest) {
		int min = 1;
		int max = 1000;
		Random random = new Random();
		Order order = new Order();
		order.setOrderCode(random.nextInt(max - min + 1)+ min);
		order.setCustomerName(orderRequest.getCustomerName());
		order.setCustomerPhone(orderRequest.getCustomerPhone());
		order.setAddress(orderRequest.getAddress());
		
		double totalOrderPrice = 0.0;
		for (OrderItemDTO itemRequest : orderRequest.getOrderItems()) {
			Product product = productRepo.findById(itemRequest.getProductId()).orElse(null);
			System.out.println(product.getName());
			if (product != null) {
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			orderItem.setQuantity(itemRequest.getQuantity());
			order.getOrderItems().add(orderItem);
			totalOrderPrice += product.getPrice() * itemRequest.getQuantity();
			}
		}
		order.setTotalPrice(totalOrderPrice);
		return orderRepo.save(order);
	}
	
	
	public Order createV1(AddOrderForm orderRequest) {
	    int min = 1;
	    int max = 1000;
	    Random random = new Random();
	    Order order = new Order();
	    order.setOrderCode(random.nextInt(max - min + 1) + min);
	    order.setCustomerName(orderRequest.getCustomerName());
	    order.setCustomerPhone(orderRequest.getCustomerPhone());
	    order.setAddress(orderRequest.getAddress());

	    double totalOrderPrice = 0.0;
	    List<OrderItemDTO> orderItems = orderRequest.getOrderItems(); // Get the order items
	    if (orderItems != null) { 
	        for (OrderItemDTO itemRequest : orderItems) { // Iterate over order items
	            Product product = productRepo.findById(itemRequest.getProductId()).orElse(null);
	            if (product != null) {
	                OrderItem orderItem = new OrderItem();
	                orderItem.setProduct(product);
	                orderItem.setQuantity(itemRequest.getQuantity());
	                order.getOrderItems().add(orderItem);
	                totalOrderPrice += product.getPrice() * itemRequest.getQuantity();
	            }
	        }
	    }
	    order.setTotalPrice(totalOrderPrice);
	    return orderRepo.save(order);
	}

	public Order updateOrder(Long orderId, AddOrderForm orderRequest) {
		Optional<Order> order = orderRepo.findById(orderId);
		if (order.isPresent()) {
			Order existingOrder = order.get();
			existingOrder.setCustomerName(orderRequest.getCustomerName());
			existingOrder.setCustomerPhone(orderRequest.getCustomerPhone());
			existingOrder.setAddress(orderRequest.getAddress());
			
			 List<OrderItem> existingOrderItems = existingOrder.getOrderItems();
		      existingOrderItems.clear();

			for(OrderItemDTO itemRequest : orderRequest.getOrderItems()) {
				Product product = productRepo.findById(itemRequest.getProductId()).orElse(null);
				if(product != null) {
					OrderItem existingOrderItem = findExistingOrderItem(existingOrderItems, itemRequest.getProductId());
					
					if (existingOrderItem != null) {
	                    existingOrderItem.setQuantity(itemRequest.getQuantity());
	                } else {
	                    OrderItem orderItem = new OrderItem();
	                    orderItem.setProduct(product);
	                    orderItem.setQuantity(itemRequest.getQuantity());
	                    existingOrderItems.add(orderItem);
	                }
				}
			}
			double totalOrderPrice = calculateTotalOrderPrice(existingOrder);
			existingOrder.setTotalPrice(totalOrderPrice);
			return orderRepo.save(existingOrder);
		}else {
			return null;
		}
		
		
	}

	private OrderItem findExistingOrderItem(List<OrderItem> existingOrderItems, Long productId) {
	    for (OrderItem orderItem : existingOrderItems) {
	        if (orderItem.getProduct().getId()==(productId)) {
	            return orderItem;
	        }
	    }
	    return null;
	}
	
	public static double calculateTotalOrderPrice(Order order) {
		double totalOrderPrice = 0.0;
		for(OrderItem orderItem : order.getOrderItems()) {
			double productPrice = orderItem.getProduct().getPrice();
			int quantity = orderItem.getQuantity();
			totalOrderPrice += productPrice * quantity;
		}
		
		return totalOrderPrice;
	}
	
	public void deleteOrder(Long orderId) {
		Optional<Order> optionalOrder = orderRepo.findById(orderId);
		if(optionalOrder.isPresent()) {
			Order order = optionalOrder.get();
			List<OrderItem> existingOrderItems = order.getOrderItems();
		      existingOrderItems.clear();
			orderRepo.delete(order);
		}
		 
	}
	
	public Page<OrderDTO> getAllOrdersByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return orderRepo.findAll(pageRequest)
                .map(OrderServiceImpl::mapOrderToOrderDto);
    }
	
	public static OrderDTO mapOrderToOrderDto(Order order) {
		List<OrderItemDetailDTO> orderItemDetailsList = new ArrayList<OrderItemDetailDTO>();
		for(OrderItem orderItem : order.getOrderItems()) {
			orderItemDetailsList.add(new OrderItemDetailDTO(
					orderItem.getProduct().getId(),
					orderItem.getProduct().getName(),
					orderItem.getQuantity(),
					orderItem.getProduct().getPrice()
					));
		}
		
		return new OrderDTO(
				order.getId(),
				order.getOrderCode(),
				order.getCustomerName(),
				order.getCustomerPhone(),
				order.getAddress(),
				order.getTotalPrice(),
				orderItemDetailsList
				
				);
	}

	public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(OrderServiceImpl::mapOrderToOrderDto)
                .collect(Collectors.toList());
    }
	
	
	public OrderDTO getOrderById(Long id) {
        return orderRepo.findById(id)
        		.map(OrderServiceImpl::mapOrderToOrderDto)
                .orElse(null);
    }
	

}
