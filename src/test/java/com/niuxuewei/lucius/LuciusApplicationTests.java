package com.niuxuewei.lucius;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuciusApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void BCryptTest() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode("B97Ry9q6CcewF312");
        System.out.println(hashedPassword);
    }

}

