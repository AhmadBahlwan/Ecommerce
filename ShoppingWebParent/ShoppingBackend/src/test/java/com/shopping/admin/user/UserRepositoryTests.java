package com.shopping.admin.user;

import com.shopping.library.entity.Role;
import com.shopping.library.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;
    @Test
    public void createFirstUserTest(){
        Role roleAdmin = entityManager.find(Role.class, 1L);
        User user = new User("bahahmad787@gmail.com", "ahmad221", "ahmad", "balawan");
        user.addRole(roleAdmin);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void createNewUserWithTwoRolesTest(){
        Role editorRole = entityManager.find(Role.class, 3);
        Role assistantRole = entityManager.find(Role.class, 5);
        User user = new User("ahmadsd135@gmail.com", "ahmad311", "ahmad", "ali");

        user.addRole(editorRole);
        user.addRole(assistantRole);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void listAllUsersTest(){
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void updateUserTest(){
        Role editorRole = entityManager.find(Role.class, 3);
        Role assistantRole = entityManager.find(Role.class, 5);
        User user = userRepository.findById(2).orElse(null);
        assertThat(user).isNotNull();
        user.addRole(editorRole);
        user.addRole(assistantRole);

        user = userRepository.save(user);
        assertThat(user.getRoles()).isNotEmpty();
    }

    @Test
    public void deleteUserTest(){
        userRepository.deleteById(2);
        assertThat(userRepository.findById(2).orElse(null)).isNull();

    }

    @Test
    public void getUserByEmailTest(){
        String email = "ahmadsd1353@gmail.com";

        User user = userRepository.findByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void countByIdTest(){
        Integer id = 3;
        assertThat(userRepository.countById(id)).isGreaterThan(0);
    }

    @Test
    public void listFirstPageTest() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);
        List<User> users = page.getContent();

        assertThat(users.size()).isEqualTo(pageSize);
    }

}
