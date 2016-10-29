package com.beanlet.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanletServiceTest {

  @Autowired
  private BeanletService beanletService;

  @Test
  public void testAddBeanlet() {
    int numBefore = beanletService.getBeanletsForUserId(1).size();
    beanletService.addBeanlet(1, "my beanlet");
    assertThat(beanletService.getBeanletsForUserId(1).size()).isEqualTo(numBefore + 1);
  }

}
