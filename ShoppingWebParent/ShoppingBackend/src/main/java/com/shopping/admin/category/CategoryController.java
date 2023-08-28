package com.shopping.admin.category;


import com.shopping.admin.FileUploadUtil;
import com.shopping.library.entity.Category;
import com.shopping.library.exception.CategoryNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
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
    public String listFirstPage(String sortDir, Model model) {
        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/categories/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") int pageNumber,
                             String sortDirection,
                             String keyword,
                             Model model) {

        if (sortDirection == null || sortDirection.isEmpty()) {
            sortDirection = "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();

        String reversedSortDir = sortDirection.equals("asc") ? "desc" : "asc";



        model.addAttribute("categories", categoryService.listByPage(pageInfo, pageNumber, sortDirection, keyword));

        long startCount = (long) (pageNumber - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE  - 1;
        if (endCount > pageInfo.getTotalElements())
            endCount = pageInfo.getTotalElements();


        model.addAttribute("reversedSortDir", reversedSortDir);

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalElements", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/categories");

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
    public String updateCategoryEnabledStatus(@PathVariable Integer id,
                                              @PathVariable boolean enabled,
                                              RedirectAttributes redirectAttributes) {

        categoryService.updateCategoryEnabledStatus(id, enabled);
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

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(listCategories, response);
    }
}
