package com.example.kadai_002.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.form.CategoryEditForm;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    public AdminCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 一覧 + 検索
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Category> categories = (keyword != null && !keyword.isEmpty())
            ? categoryRepository.findByNameContaining(keyword)
            : categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        return "admin/categories/index";
    }

    // 登録
    @PostMapping
    public String create(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }
    
 // 編集画面を表示する
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Category category = CategoryService.findById(id);
        CategoryEditForm categoryEditForm = new CategoryEditForm();
        categoryEditForm.setId(category.getId());
        categoryEditForm.setName(category.getName());
        
        model.addAttribute("categoryEditForm", categoryEditForm);
        return "admin/categories/edit"; // 編集ページのテンプレート
    }

    // 編集内容を保存する
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, CategoryEditForm categoryEditForm, Model model) {
        CategoryService.update(id, categoryEditForm);
        model.addAttribute("successMessage", "カテゴリが更新されました");
        return "redirect:/admin/categories"; 
    }
    
}

