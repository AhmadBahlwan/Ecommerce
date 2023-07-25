package com.shopping.admin.category;


import com.shopping.admin.FileUploadUtil;
import com.shopping.library.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String getAll(@Param("sortDir") String sortDir, Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        model.addAttribute("categories", categoryService.getAll(sortDir));
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String createCategory(Model model) {
        List<Category> categories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDirectory = "category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDirectory(uploadDirectory);
            FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
        } else {
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("category", categoryService.get(id));
            model.addAttribute("categories", categoryService.listCategoriesUsedInForm());
            model.addAttribute("pageTitle", "Edit Category (ID:" + id + ")");

            return "categories/category_form";
        }catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{enabled}")
    public String updateUserEnabledStatus(@PathVariable Integer id,
                                          @PathVariable boolean enabled,
                                          RedirectAttributes redirectAttributes) {

        categoryService.updateUserEnabledStatus(id, enabled);
        String status =  enabled ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been " + status + " successfully";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";

    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try{
            categoryService.delete(id);
            String categoryDirectory = "category-images" + id;
            FileUploadUtil.removeDirectory(categoryDirectory);
            redirectAttributes.addFlashAttribute("message", "The category with ID "
                    + id + "has been deleted successfully");
        }catch (CategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/categories";
    }
}
