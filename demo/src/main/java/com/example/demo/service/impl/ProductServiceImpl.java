package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AddProductForm;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.UpdateProductForm;
import com.example.demo.entity.Product;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.service.ProductService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
		
		@Autowired
	 	private final ProductRepo productRepository;
		@Autowired
	    private final CategoryRepo categoryRepository;
		
		@Value("${file.upload-dir}") 
	    private String uploadDir;

	    @Override
	    public List<ProductDTO> getAllProducts() {
	        return productRepository.findAll().stream()
	                .map(this::mapToDTO)
	                .collect(Collectors.toList());
	    }
	    
	    public Page<ProductDTO> getAllProductsByPage(int page, int size) {
	        PageRequest pageRequest = PageRequest.of(page, size);
	        return productRepository.findAll(pageRequest)
	                .map(this::mapToDTO);
	    }

	    @Override
	    public ProductDTO getProductById(Long id) {
	        return productRepository.findById(id)
	                .map(this::mapToDTO)
	                .orElse(null);
	    }

	    @Override
	    public ProductDTO createProduct(ProductDTO productDTO) throws IOException {
	        Product product = mapToEntity(productDTO);
	        return mapToDTO(productRepository.save(product));
	    }

	    @Override
	    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws IOException {
	        if (!productRepository.existsById(id)) {
	            return null;
	        }

	        Product updatedProduct = mapToEntity(productDTO);
	        updatedProduct.setId(id);

	        return mapToDTO(productRepository.save(updatedProduct));
	    }

	    @Override
	    public void deleteProduct(Long id) {
	        productRepository.deleteById(id);
	    }

	    private ProductDTO mapToDTO(Product product) {
	        ProductDTO productDTO = new ProductDTO();
	        productDTO.setId(product.getId());
	        productDTO.setName(product.getName());
	        productDTO.setPrice(product.getPrice());
	        productDTO.setQuantity(product.getQuantity());
	        productDTO.setDescription(product.getDescription());
	        productDTO.setCategoryId(product.getCategory().getId()); 
	        productDTO.setFilePath(product.getFilePath());
	        productDTO.setImageName(product.getImageName());
	        return productDTO;
	    }

	    
		private Product mapToEntity(ProductDTO productDTO)  {
	        Product product = new Product();
	        product.setName(productDTO.getName());
	        product.setPrice(productDTO.getPrice());
	        product.setQuantity(productDTO.getQuantity());
	        product.setDescription(productDTO.getDescription());
	        product.setCategory(categoryRepository.getReferenceById(productDTO.getCategoryId()));
	        product.setFilePath(productDTO.getFilePath());
	        product.setImageName(productDTO.getImageName());
	        return product;
	    }
		

		public ProductDTO create(MultipartFile file, ProductDTO productDTO) throws IOException {
			if (file == null || file.isEmpty()) {
	            throw new IllegalArgumentException("File is null or empty");
	        }
	        String fileName = file.getOriginalFilename();
//	        String filePath = uploadDir + "/" + fileName;
	        String filePath = "images/" + fileName;
	        
	        // Create the directory if it doesn't exist
	        Path directoryPath = Paths.get(uploadDir);
	        if (!Files.exists(directoryPath)) {
	            Files.createDirectories(directoryPath);
	        }
	        Path targetLocation = Paths.get(filePath);
	        Files.copy(file.getInputStream(), targetLocation);

	        productDTO.setFilePath(filePath);
	        productDTO.setImageName(fileName);

	        Product product = mapToEntity(productDTO);

	        Product savedProduct = productRepository.save(product);

	        return mapToDTO(savedProduct);
	    }
		
		public ProductDTO update(Long id, MultipartFile file, ProductDTO updatedProductDTO) throws IOException {
		    Optional<Product> optionalProduct = productRepository.findById(id);
		    if (optionalProduct.isEmpty()) {
		        throw new NoSuchElementException("Product not found with ID: " + id);
		    }

		    Product existingProduct = optionalProduct.get();
		   
		    existingProduct.setName(updatedProductDTO.getName());
		    existingProduct.setPrice(updatedProductDTO.getPrice());
		    existingProduct.setQuantity(updatedProductDTO.getQuantity());
		    existingProduct.setDescription(updatedProductDTO.getDescription());
		    existingProduct.setCategory(categoryRepository.getReferenceById(updatedProductDTO.getCategoryId()));

		    if (file != null && !file.isEmpty()) {
		        String fileName = file.getOriginalFilename();
		        String filePath = "images/" + fileName;

		        Path targetLocation = Paths.get(uploadDir + "/" + fileName);
		        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		        existingProduct.setFilePath(filePath);
		        existingProduct.setImageName(fileName);
		    }

		    Product savedProduct = productRepository.save(existingProduct);
		    return mapToDTO(savedProduct);
		}

		public ProductDTO createV1(@Valid AddProductForm form) throws IOException{
			if (form.file() == null || form.file().isEmpty()) {
	            throw new IllegalArgumentException("File is null or empty");
	        }
	        String fileName = form.file().getOriginalFilename();
	        String filePath = "images/" + fileName;
	        
	        Path directoryPath = Paths.get(uploadDir);
	        if (!Files.exists(directoryPath)) {
	            Files.createDirectories(directoryPath);
	        }
	        Path targetLocation = Paths.get(filePath);
	        Files.copy(form.file().getInputStream(), targetLocation);
//			String mainFilePath = uploadDir + form.file().getOriginalFilename();
//			Files.copy(form.file().getInputStream(), Paths.get(mainFilePath),StandardCopyOption.REPLACE_EXISTING);
			Product product = new Product();
			product.setName(form.name());
			product.setDescription(form.description());
			product.setImageName(fileName);
			product.setFilePath(filePath);
			product.setPrice(form.price());
			product.setQuantity(form.quantity());
	        product.setCategory(categoryRepository.getReferenceById(form.categoryId()));
			
	        Product savedProduct = productRepository.save(product);

	        return mapToDTO(savedProduct);
		}

		public ProductDTO updateV1(@Valid AddProductForm form, long id) throws IOException{
			Optional<Product> optionalProduct = productRepository.findById(id);
		    if (optionalProduct.isEmpty()) {
		        throw new NoSuchElementException("Product not found with ID: " + id);
		    }

		    Product existingProduct = optionalProduct.get();
		   
		    existingProduct.setName(form.name());
		    existingProduct.setPrice(form.price());
		    existingProduct.setQuantity(form.quantity());
		    existingProduct.setDescription(form.description());
		    existingProduct.setCategory(categoryRepository.getReferenceById(form.categoryId()));

		    if (form.file() != null && !form.file().isEmpty()) {
		        String fileName = form.file().getOriginalFilename();
		        String filePath = "images/" + fileName;

		        Path targetLocation = Paths.get(uploadDir + "/" + fileName);
		        Files.copy(form.file().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		        existingProduct.setFilePath(filePath);
		        existingProduct.setImageName(fileName);
		    }

		    Product savedProduct = productRepository.save(existingProduct);
		    return mapToDTO(savedProduct);
		}

		public void deleteProductV1(Long productId) {
	        Optional<Product> optionalProduct = productRepository.findById(productId);
	        if (optionalProduct.isPresent()) {
	            Product product = optionalProduct.get();
	            product.setStatus("deleted");
	            productRepository.save(product);
	        } 
	    }
}
		

	

