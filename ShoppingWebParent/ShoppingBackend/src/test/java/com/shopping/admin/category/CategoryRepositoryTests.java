package com.shopping.admin.category;


import com.shopping.library.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void createCategoryTest() {
        Category category = new Category();
        category.setName("Computers");
        category.setAlias("computers");
        category.setImage("default.png");

        Category savedCategory =  categoryRepository.save(category);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void createSubCategoryTest() {
         Category parent = new Category(1);
         Category laptops = new Category("Laptops", parent);
         Category components = new Category("Computer Components", parent);

         categoryRepository.saveAll(List.of(laptops, components));

    }

    @Test
    public void listRootCategoriesTest() {
        categoryRepository.findRootCategories(Sort.by("name").ascending()).forEach(cat -> System.out.println(cat.getName()));
    }
}
