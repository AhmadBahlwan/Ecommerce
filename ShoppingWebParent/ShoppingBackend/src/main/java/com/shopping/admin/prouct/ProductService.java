package com.shopping.admin.prouct;

import com.shopping.library.entity.Brand;
import com.shopping.library.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (StringUtils.isEmpty(product.getAlias())) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    public boolean isNameUnique(Integer id, String name) {
        Product product = productRepository.findByName(name);

        if (id == null) {
            return product == null;
        }

        return product == null || product.getId().equals(id);
    }
}