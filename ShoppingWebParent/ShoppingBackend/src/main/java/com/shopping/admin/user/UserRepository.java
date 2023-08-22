package com.shopping.admin.user;

import com.shopping.admin.paging.SearchRepository;
import com.shopping.library.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends SearchRepository<User, Integer> {

    List<User> findAll();
    User findByEmail(String email);

    Long countById(Integer id);

    @Query("SELECT u FROM User u WHERE CONCAT (u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) " +
            "LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);
}
