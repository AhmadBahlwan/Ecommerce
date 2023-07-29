package com.shopping.admin.brand;

import com.shopping.library.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = brandRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }

        brandRepository.deleteById(id);
    }

    public boolean isNameUnique(Integer id, String name) {
        Brand brand = brandRepository.findByName(name);

        if (id == null) {
            return brand == null;
        }

        return brand == null || brand.getId().equals(id);
    }



}
