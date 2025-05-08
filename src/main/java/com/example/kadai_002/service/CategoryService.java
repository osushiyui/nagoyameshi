package com.example.kadai_002.service;

import org.springframework.stereotype.Service;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.form.CategoryEditForm;
import com.example.kadai_002.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
    private static CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public static Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
    }

    @Transactional
    public static void update(Long id, CategoryEditForm categoryEditForm) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        category.setName(categoryEditForm.getName());
        categoryRepository.save(category);
    }
}
