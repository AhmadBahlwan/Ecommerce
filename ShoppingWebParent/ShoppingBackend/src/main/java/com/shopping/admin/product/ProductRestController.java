package com.shopping.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products/check-name")
    public String checkDuplicationName(Integer id, String name) {
        return productService.isNameUnique(id, name) ? "OK" : "Duplicated";
    }
}
