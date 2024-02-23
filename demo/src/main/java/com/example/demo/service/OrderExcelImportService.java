package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AddOrderForm;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderItemDetailDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.service.impl.OrderServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderExcelImportService {
	
	@Autowired
	private final OrderServiceImpl impl;
	private final OrderRepo repo;
	@Autowired
	private final ProductRepo productRepo;
	
	 @Transactional
    public void importOrdersFromExcel(MultipartFile file) throws IOException {
        List<OrderDTO> orders = readOrdersFromExcelFile(file);
        for (OrderDTO orderDTO : orders) {
        	 calculateTotalPrice(orderDTO);
             AddOrderForm orderForm = convertToOrderForm(orderDTO);
             impl.createV1(orderForm);
        }
    }

	 @Transactional
    public List<OrderDTO> readOrdersFromExcelFile(MultipartFile file) throws IOException {
        List<OrderDTO> orders = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row currentRow : sheet) {
                if (currentRow.getRowNum() == 0) {
                    continue;
                }

                OrderDTO order = new OrderDTO();

                order.setOrderCode((long) currentRow.getCell(0).getNumericCellValue());
                order.setCustomerName(currentRow.getCell(1).getStringCellValue());
                order.setCustomerPhone(currentRow.getCell(2).getStringCellValue());
                order.setAddress(currentRow.getCell(3).getStringCellValue());
               

                OrderItemDTO orderItem = new OrderItemDTO();
                orderItem.setProductId((long)currentRow.getCell(4).getNumericCellValue());
                orderItem.setQuantity((int) currentRow.getCell(6).getNumericCellValue());
//                orderItem.setProductPrice(currentRow.getCell(6).getNumericCellValue());
                
//                calculateTotalPrice(order);
                order.setTotalPrice((double)currentRow.getCell(8).getNumericCellValue());
                order.getOrderList().add(orderItem);

                orders.add(order);
                
                
            }

            workbook.close();
        }

        return orders;
    }

    private AddOrderForm convertToOrderForm(OrderDTO orderDTO) {
        AddOrderForm orderForm = new AddOrderForm();
        orderForm.setCustomerName(orderDTO.getCustomerName());
        orderForm.setCustomerPhone(orderDTO.getCustomerPhone());
        orderForm.setAddress(orderDTO.getAddress());
        orderForm.setOrderItems(orderDTO.getOrderList());
        
        return orderForm;
    }
    
    private void calculateTotalPrice(OrderDTO orderDTO) {
        double totalOrderPrice = 0.0;
        for (OrderItemDetailDTO item : orderDTO.getOrderItems()) {
            double itemTotalPrice = item.getProductPrice() * item.getQuantity();
            totalOrderPrice += itemTotalPrice;
        }
        orderDTO.setTotalPrice(totalOrderPrice);
        System.out.println("Total Price calculated for order: " + orderDTO.getOrderCode() + " is " + totalOrderPrice);
    }
}
