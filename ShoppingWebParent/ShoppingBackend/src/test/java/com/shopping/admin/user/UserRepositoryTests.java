package com.shopping.admin.user;

import com.shopping.library.entity.Role;
import com.shopping.library.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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

}
