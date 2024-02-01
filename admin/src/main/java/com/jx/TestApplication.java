package com.jx;

import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SpringBootTest(classes = AdminApplication.class)
@RunWith(SpringRunner.class)
public class TestApplication {
    
    @Test
    public void testDemo() {
        Long time = 1706517993L - 1706515993L;
        time/=1000*60;//转化为分钟
        double hour = (double)time/60;
        BigDecimal b = new BigDecimal(hour);
        //保留两位小数
        double resultHour = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}

