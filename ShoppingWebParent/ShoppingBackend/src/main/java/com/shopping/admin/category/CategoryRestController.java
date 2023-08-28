package com.shopping.admin.category;

import com.shopping.library.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories/check_unique")
    public String checkUnique(Integer id, String name, String alias) {
        return categoryService.checkUnique(id, name, alias);
    }

    /** Call that function to set allParentIds to the categories*/
    @GetMapping("/categories/migrate-all-parent-ids")
    public void migrateAllParentIds() {
        categoryService.getAllCategories().forEach(category -> categoryService.save(category));
    }
}
