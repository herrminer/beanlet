package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanletRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.beanlet.web.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BeanletServiceTest {

  @Autowired
  private BeanletService beanletService;

  @Autowired
  private BeanletRepository beanletRepository;

  @Test
  public void testAddBeanlet() {
    EntityId<User> userId = HERRMINER;
    int numBefore = beanletService.getBeanletsForUserId(userId).size();
    Beanlet beanlet = beanletService.addBeanlet(userId, "my beanlet");
    assertThat(beanlet.getSortOrder()).isEqualTo(6); // most recently added gets highest number
    assertThat(beanletService.getBeanletsForUserId(userId).size()).isEqualTo(numBefore + 1);
  }

  @Test
  public void testCountIt() {
    Beanlet one = beanletRepository.findOne(EXERCISE);
    assertThat(one.getBeanCount()).isEqualTo(3);
    DateTime originalDateLastLogged = one.getDateLastLogged();

    Beanlet beanlet = beanletService.countIt(HERRMINER, EXERCISE, DateTimeZone.UTC);

    assertThat(beanlet).isNotNull();
    assertThat(beanlet.getDateLastLogged()).isGreaterThan(originalDateLastLogged);
    assertThat(beanlet.getBeanCount()).isEqualTo(4);
  }

}
