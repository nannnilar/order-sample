package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddOrderForm;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Order;
import com.example.demo.service.impl.OrderServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	
	@Autowired
	private final OrderServiceImpl impl;
	
	@GetMapping("/all")
	public ResponseEntity<List<Order>> findAll() {
	    List<Order> orders = impl.findAll();
	    return ResponseEntity.ok(orders);
	}
	@GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = impl.getAllOrders();
        return ResponseEntity.ok(orders);
    }
	
	@PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody AddOrderForm orderRequest) {
        try {
            Order createdOrder = impl.createV1(orderRequest);
            return new ResponseEntity<>("Order created with ID: " + createdOrder.getOrderCode(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@PutMapping("/update/{orderId}")
	public ResponseEntity<String> updateOrder(@PathVariable Long orderId, @RequestBody AddOrderForm orderRequest) {
		try {
			Order updateOrder = impl.updateOrder(orderId, orderRequest);
			return new ResponseEntity<>("Order updated with ID: " + updateOrder.getOrderCode(), HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>("Order not found with ID: " + orderId, HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<>("Failed to update order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{orderId}")
	public void deleteOrder(@PathVariable Long orderId) {
		impl.deleteOrder(orderId);
	}
	
	 @GetMapping("/{id}")
	    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
	        OrderDTO order = impl.getOrderById(id);
	        return order != null
	                ? ResponseEntity.ok(order)
	                : ResponseEntity.notFound().build();
	    }
	
	@GetMapping("byPage")
    public ResponseEntity<Page<OrderDTO>> getAllOrdersByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OrderDTO> orderPages = impl.getAllOrdersByPage(page, size);
        return ResponseEntity.ok(orderPages);
    }

}
