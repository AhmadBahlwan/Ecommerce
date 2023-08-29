package com.shopping.address;

import com.shopping.library.entity.Address;
import com.shopping.library.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repo;

    public List<Address> listAddressBook(Customer customer) {
        return repo.findByCustomer(customer);
    }

}