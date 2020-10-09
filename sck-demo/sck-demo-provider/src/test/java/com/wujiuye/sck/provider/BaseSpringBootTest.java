package com.wujiuye.sck.provider;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SckProviderApplication.class)
@ActiveProfiles("dev")
public abstract class BaseSpringBootTest {


}
