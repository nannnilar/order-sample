package com.example.demo.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductDTO;

@Service
public class ExcelExportService {
    public void exportProductsToExcel(List<ProductDTO> products, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            // Create header row
            Row headerRow = sheet.createRow(0);
//            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Price");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Description");
            headerRow.createCell(4).setCellValue("Category ID");
            headerRow.createCell(5).setCellValue("Image");
            headerRow.createCell(6).setCellValue("File Path");
            headerRow.createCell(7).setCellValue("Status");

            // Create data rows
            int rowNum = 1;
            for (ProductDTO product : products) {
                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(product.getId());
                row.createCell(0).setCellValue(product.getName());
                row.createCell(1).setCellValue(product.getPrice());
                row.createCell(2).setCellValue(product.getQuantity());
                row.createCell(3).setCellValue(product.getDescription());
                row.createCell(4).setCellValue(product.getCategoryId());
                row.createCell(5).setCellValue(product.getImageName());
                row.createCell(6).setCellValue(product.getFilePath());
                row.createCell(7).setCellValue(product.getStatus());
            }

            workbook.write(outputStream);
        }
    }
    
}
