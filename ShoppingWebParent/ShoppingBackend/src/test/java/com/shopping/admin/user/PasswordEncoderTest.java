package com.shopping.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "Ahmad2021";
        String encodedPassword = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        assertThat(matches).isTrue();
    }
}
