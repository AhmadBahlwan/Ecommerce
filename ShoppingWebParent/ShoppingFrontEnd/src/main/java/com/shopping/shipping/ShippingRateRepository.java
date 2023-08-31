package com.shopping.shipping;

import com.shopping.library.entity.Country;
import com.shopping.library.entity.ShippingRate;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    ShippingRate findByCountryAndState(Country country, String state);
}