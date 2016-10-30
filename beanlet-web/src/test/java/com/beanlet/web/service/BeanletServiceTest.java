package com.beanlet.web.service;

import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.beanlet.web.TestConstants.HERRMINER;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanletServiceTest {

  @Autowired
  private BeanletService beanletService;

  @Test
  public void testAddBeanlet() {
    EntityId<User> userId = HERRMINER;
    int numBefore = beanletService.getBeanletsForUserId(userId).size();
    beanletService.addBeanlet(userId, "my beanlet");
    assertThat(beanletService.getBeanletsForUserId(userId).size()).isEqualTo(numBefore + 1);
  }

}
