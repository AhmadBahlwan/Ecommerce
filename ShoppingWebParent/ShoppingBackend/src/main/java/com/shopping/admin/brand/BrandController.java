package com.shopping.admin.brand;

import com.shopping.admin.category.CategoryService;
import com.shopping.library.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/brands")
    public String listAll(Model model) {
        List<Brand> brands = brandService.listAll();
        model.addAttribute("brands", brands);

        return "brands/brands";
    }
}
