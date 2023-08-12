package com.shopping.admin.setting.state;

import com.shopping.library.entity.Country;
import com.shopping.library.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    List<State> findByCountryOrderByNameAsc(Country country);
}