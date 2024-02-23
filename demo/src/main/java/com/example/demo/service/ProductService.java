package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import com.example.demo.dto.ProductDTO;

public interface ProductService {
	
	 List<ProductDTO> getAllProducts();

	 ProductDTO getProductById(Long id);

	 ProductDTO createProduct(ProductDTO productDTO) throws IOException;

	 ProductDTO updateProduct(Long id, ProductDTO productDTO) throws IOException;

	    void deleteProduct(Long id);

}
