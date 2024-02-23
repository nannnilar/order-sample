package com.example.demo.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderItemDetailDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.service.impl.OrderServiceImpl;

@Service
public class OrderExcelExportService {
	
	public void exportOrdersToExcel(List<OrderDTO> orders, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Order Code", "Customer Name", "Phone", "Address","Product Id", "Product Name", "Quantity", "Price", "Total Price"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Create data rows
            int rowNum = 1;
            for (OrderDTO order : orders) {
                for (OrderItemDetailDTO orderItem : order.getOrderItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(order.getOrderCode());
                    row.createCell(1).setCellValue(order.getCustomerName());
                    row.createCell(2).setCellValue(order.getCustomerPhone());
                    row.createCell(3).setCellValue(order.getAddress());
                    row.createCell(4).setCellValue(orderItem.getProductId());
                    row.createCell(5).setCellValue(orderItem.getProductName()); 
                    row.createCell(6).setCellValue(orderItem.getQuantity()); 
                    row.createCell(7).setCellValue(orderItem.getProductPrice());
                    row.createCell(8).setCellValue(order.getTotalPrice());
                    System.out.println(" "+ row);
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        }
    }
//	public void exportOrdersToExcel(List<OrderDTO> orders, OutputStream outputStream) throws IOException {
//		try(Workbook workbook = new XSSFWorkbook()) {
//			Sheet sheet = workbook.createSheet("Orders");
//			
//			Row headerRow = sheet.createRow(0);
//			headerRow.createCell(0).setCellValue("Order Code");
//			headerRow.createCell(1).setCellValue("Customer Name");
//            headerRow.createCell(2).setCellValue("Phone");
//            headerRow.createCell(3).setCellValue("Address");
//            headerRow.createCell(4).setCellValue("Product Id");
//            headerRow.createCell(5).setCellValue("Quantity");
////            headerRow.createCell(6).setCellValue("Product Price");
//            headerRow.createCell(6).setCellValue("Total Price");
//            
//            int rowNum = 1;
//            for (OrderDTO order : orders) {
//                for (OrderItemDTO orderItem : order.getOrderList()) {
//                    Row row = sheet.createRow(rowNum++);
//                    row.createCell(0).setCellValue(order.getOrderCode());
//                    row.createCell(1).setCellValue(order.getCustomerName());
//                    row.createCell(2).setCellValue(order.getCustomerPhone());
//                    row.createCell(3).setCellValue(order.getAddress());
//                    
//                    row.createCell(4).setCellValue(orderItem.getProductId());
//                    row.createCell(5).setCellValue(orderItem.getQuantity());
////                    row.createCell(6).setCellValue(orderItem.getProductPrice());
//                    row.createCell(6).setCellValue(order.getTotalPrice());
//                }
//            }
//
//            workbook.write(outputStream);
//		}
//	}
//	

}
