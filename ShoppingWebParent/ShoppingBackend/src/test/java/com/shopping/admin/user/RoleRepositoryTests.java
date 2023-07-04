package com.shopping.admin.user;

import com.shopping.library.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role adminRole = new Role("Admin", "Manager of system");
        Role savedRole = roleRepository.save(adminRole);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role salesPersonRole = new Role("Salesperson", "manage product price," +
                "customers, shipping, orders and sales report");


        Role editorRole = new Role("Editor", "manage categories, brands," +
                "products, articles and menus");

        Role shipperRole = new Role("Shipper", "view products, view orders " +
                "and update order status");

        Role assistantRole = new Role("Assistant", "manage questions and reviews");

        roleRepository.saveAll(List.of(salesPersonRole, editorRole, shipperRole, assistantRole));

    }
}
