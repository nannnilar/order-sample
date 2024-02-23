package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AddProductForm;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;
import com.example.demo.service.impl.ProductServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private final ProductService productService;
	@Autowired
	private final ProductServiceImpl impl;
	
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("byPage")
    public ResponseEntity<Page<ProductDTO>> getAllProductsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductDTO> productsPages = impl.getAllProductsByPage(page, size);
        return ResponseEntity.ok(productsPages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return product != null
                ? ResponseEntity.ok(product)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct( @RequestBody ProductDTO productDTO) throws IOException {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        System.out.println(createdProduct);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) throws IOException {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);

        return updatedProduct != null
                ? ResponseEntity.ok(updatedProduct)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        impl.deleteProductV1(id);
        return ResponseEntity.noContent().build();
    }
    
    @Value("${file.upload-dir}") 
    private String uploadDir;
//    @GetMapping("image/{imageName:.+}")
//    public ResponseEntity<Resource> getImageV1(@PathVariable String imageName) throws IOException {
//        Resource imageResource = resourceLoader.getResource("file:C:/Users/DELL-322-15/eclipse-workspace/demo/images/" + imageName);
//        if (imageResource.exists() && imageResource.isReadable()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .body(imageResource);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
    @GetMapping("image/{imageName:.+}")	
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get(uploadDir).resolve(imageName);
        Resource imageResource;
        try {
            imageResource = new UrlResource(imagePath.toUri());
            if (imageResource.exists() && imageResource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageResource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage());
        }
    }
    
    @PostMapping(value = "/create")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("file") MultipartFile file,
            @RequestPart("productDTO") ProductDTO productDTO) {
        try {
        	HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "multipart/form-data");
            headers.set("Content-Type", "application/json");
            
            ProductDTO createdProduct = impl.create(file, productDTO);
//            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .headers(headers)
                    .body(createdProduct);
        } catch (IOException e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @PutMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestPart("updatedProductDTO") ProductDTO updatedProductDTO) {
        try {
            ProductDTO updatedProduct = impl.update(id, file, updatedProductDTO);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
    
    @PostMapping(path = "saveProduct",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProductDTO createV1(@RequestParam("name")String name,@RequestParam("price")double price,
							@RequestParam("quantity")int quantity, @RequestParam("description")String description,
							@RequestParam("image")MultipartFile file, @RequestParam("categoryId") long categoryId)throws IOException {
		AddProductForm form = new AddProductForm(name, price, quantity, description, file , categoryId);
		return impl.createV1(form);
	}

    @PatchMapping(path = "updateProduct/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProductDTO updateV1(@RequestParam("name")String name,@RequestParam("price")double price,
			@RequestParam("quantity")int quantity, @RequestParam("description")String description,
			@RequestParam("image")MultipartFile file, @RequestParam("categoryId") long categoryId,
			 @PathVariable long id)throws IOException {
		AddProductForm form = new AddProductForm(name, price, quantity,description,file, categoryId);
		return impl.updateV1(form,id);
	}
	
}
