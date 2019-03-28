package com.niuxuewei.lucius.domain.bo;

import com.niuxuewei.lucius.entity.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    /**
     * BCrypt密码加密测试
     */
    @Test
    public void userPasswordEncodeTest() {
        String psw = "123";
        User user = new User();
        user.setPassword(psw);
        String pswWithBCryptEncode = user.getPassword();
        Assert.assertTrue(BCrypt.checkpw(psw, pswWithBCryptEncode));
    }

}
