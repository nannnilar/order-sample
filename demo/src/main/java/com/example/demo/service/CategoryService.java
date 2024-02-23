package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CategoryDTO;

public interface CategoryService {
	
	List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

}
