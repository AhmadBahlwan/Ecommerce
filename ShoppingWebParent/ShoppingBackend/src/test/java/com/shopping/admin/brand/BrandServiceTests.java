package com.shopping.admin.brand;

import com.shopping.library.entity.Brand;
import com.shopping.library.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {

    @MockBean
    BrandRepository brandRepository;

    @InjectMocks
    BrandService brandService;

    @Test
    public void checkUniqueInNewModeReturnFalse() {
        Integer id = null;
        String name = "Acer";

        Brand brand = new Brand(id, name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        assertThat(brandService.isNameUnique(id, name)).isFalse();
    }

    @Test
    public void checkUniqueInNewModeReturnTrue() {
        Integer id = null;
        String name = "Acer2";

        Brand brand = new Brand(id, name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        assertThat(brandService.isNameUnique(id, name)).isTrue();
    }

    @Test
    public void checkUniqueInEditModeReturnFalse() {
        Integer id = 1;
        String name = "Acer";

        Brand brand = new Brand(2, name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        assertThat(brandService.isNameUnique(id, name)).isFalse();
    }

    @Test
    public void checkUniqueInEditModeReturnTrue() {
        Integer id = 1;
        String name = "Acer1";

        Brand brand = new Brand(id, name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        assertThat(brandService.isNameUnique(id, name)).isTrue();
    }
}
