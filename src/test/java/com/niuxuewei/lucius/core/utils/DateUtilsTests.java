package com.niuxuewei.lucius.core.utils;

import com.niuxuewei.lucius.core.conf.SpringSecurityTestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringSecurityTestConfig.class})
public class DateUtilsTests {

    @Test
    public void testParseISO8601() {
        String s = "2019-03-14T12:53:52.000Z";
        Date d = DateUtils.parseISO8601(s);
        assert d != null;
        Assert.assertEquals("Thu Mar 14 12:53:52 CST 2019", d.toString());
    }

    @Test
    public void testGetDifferenceDays() {
        Date now = new Date();
        Date tomorrow = DateUtils.addDay(now, 1);
        Assert.assertEquals(1L, DateUtils.getDifferenceDays(now, tomorrow));
        Date add1Min = DateUtils.addMinute(now, 1);
        Assert.assertEquals(0L, DateUtils.getDifferenceDays(now, add1Min));
    }

}
