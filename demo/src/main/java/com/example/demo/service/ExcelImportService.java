package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProductDTO;
import com.example.demo.repo.ProductRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelImportService {
	
	@Autowired
	private ProductService service;
	
	@Transactional
	public List<ProductDTO> readProductsFromExcelFile(MultipartFile file) throws IOException {
        List<ProductDTO> products = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row currentRow : sheet) {
                if (currentRow.getRowNum() == 0) {
                    // Skip header row
                    continue;
                }

                ProductDTO product = new ProductDTO();

//                product.setId((long) currentRow.getCell(0).getNumericCellValue());
                product.setName(currentRow.getCell(0).getStringCellValue());
                product.setPrice(currentRow.getCell(1).getNumericCellValue());
                product.setQuantity((int) currentRow.getCell(2).getNumericCellValue());
                product.setDescription(currentRow.getCell(3).getStringCellValue());
                product.setCategoryId((long) currentRow.getCell(4).getNumericCellValue());
                product.setImageName(currentRow.getCell(5).getStringCellValue());
                product.setFilePath(currentRow.getCell(6).getStringCellValue());
                product.setStatus(currentRow.getCell(7).getStringCellValue());

                products.add(product);
                service.createProduct(product);
            }

            workbook.close();
        }

        return products;
    }
	
}
