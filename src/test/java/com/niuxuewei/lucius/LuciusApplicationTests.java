package com.niuxuewei.lucius;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuciusApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void BCryptTest() {
        String hashedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("3u7f2827AE3oA4LB");
        System.out.println(hashedPassword);
    }

}

