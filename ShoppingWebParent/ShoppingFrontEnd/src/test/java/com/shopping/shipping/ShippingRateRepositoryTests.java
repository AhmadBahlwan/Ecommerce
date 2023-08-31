package com.shopping.shipping;

import com.shopping.library.entity.Country;
import com.shopping.library.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRateRepositoryTests {

    @Autowired
    private ShippingRateRepository repo;

    @Test
    public void testFindByCountryAndState() {
        Country usa = new Country(234);
        String state = "New York";
        ShippingRate shippingRate = repo.findByCountryAndState(usa, state);

        assertThat(shippingRate).isNotNull();
        System.out.println(shippingRate);
    }
}