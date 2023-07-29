package com.shopping.admin.brand;

import com.shopping.library.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    public Long countById(Integer id);
}
