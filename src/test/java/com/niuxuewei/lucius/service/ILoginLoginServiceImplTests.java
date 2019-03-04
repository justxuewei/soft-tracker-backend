package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ILoginLoginServiceImplTests {

    @Resource
    private ILoginService loginService;

    @Test
    public void testSelectAll() {
        List<User> data = loginService.selectAll();
        for (User datum : data) {
            System.out.println(datum.getNickname());
        }
    }

}
