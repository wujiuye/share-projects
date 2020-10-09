package com.wujiuye.sck.consumer;

import com.wujiuye.sck.consumer.service.DemoInvokeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SckConsumerApplication.class)
@ActiveProfiles("dev")
public class DemoTest {

    @Resource
    private DemoInvokeService demoInvokeService;

    @Test
    public void testInvoDemo(){
        System.out.println(demoInvokeService.invokeDemo());
    }

}
