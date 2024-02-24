package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Order;
import com.example.demo.service.ExcelExportService;
import com.example.demo.service.ExcelImportService;
import com.example.demo.service.OrderExcelExportService;
import com.example.demo.service.OrderExcelImportService;
import com.example.demo.service.impl.OrderServiceImpl;
import com.example.demo.service.impl.ProductServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {
	
	 @Autowired
	 private final ExcelExportService excelExportService;
	 @Autowired
	 private final ExcelImportService excelImportService;
	 
	 @Autowired
	 private final OrderExcelExportService oExportService;
	 
	 private final OrderExcelImportService oImportService;

	 @Autowired
	 private final ProductServiceImpl serviceImpl;
	 
	 @Autowired
	 private final OrderServiceImpl oServiceImpl;

	    @GetMapping("/export/products")
	    public void exportProductsToExcel(HttpServletResponse response) {
	        try {
	        	String filename = "products.xlsx";
	            List<ProductDTO> products = serviceImpl.getAllProducts();
	            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//	            response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");
	            response.setHeader("Content-Disposition", "attachment; filename=" +filename);

	            excelExportService.exportProductsToExcel(products, response.getOutputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } 
	    
	    @GetMapping("/export/orders")
	    public void exportOrdersToExcel(HttpServletResponse response) {
	        try {
	        	String filename = "orders.xlsx";
	            List<OrderDTO> orders = oServiceImpl.getAllOrders();
	            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//	            response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");
	            response.setHeader("Content-Disposition", "attachment; filename=" +filename);

	            oExportService.exportOrdersToExcel(orders, response.getOutputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } 

	    @PostMapping("/import/products")
	    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
	        try {
	            List<ProductDTO> products = excelImportService.readProductsFromExcelFile(file);
	            return ResponseEntity.ok("Products imported successfully");
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to import products: " + e.getMessage());
	        }
	    }

	    @PostMapping("/import/orders")
	    public ResponseEntity<String> importOrders(@RequestParam("file") MultipartFile file) {
	        try {
	        	oImportService.importOrdersFromExcel(file);
	            return ResponseEntity.ok("Orders imported successfully");
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to import orders: " + e.getMessage());
	        }
	    }
}
