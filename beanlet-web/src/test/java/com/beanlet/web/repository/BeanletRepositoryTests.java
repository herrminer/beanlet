package com.beanlet.web.repository;

import com.beanlet.web.jpa.Beanlet;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.beanlet.web.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BeanletRepositoryTests {

  @Autowired
  private BeanletRepository beanletRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindOne() {
    Beanlet beanlet = beanletRepository.findOne(EXERCISE);
    assertThat(beanlet.getName()).isEqualTo("exercise");
    DateTime dateLastLogged = beanlet.getDateLastLogged().withZone(DateTimeZone.UTC);
    assertThat(dateLastLogged.getYear()).isEqualTo(2016);
    assertThat(dateLastLogged.getMonthOfYear()).isEqualTo(7);
    assertThat(dateLastLogged.getDayOfMonth()).isEqualTo(1);
    assertThat(dateLastLogged.getHourOfDay()).isEqualTo(12);
    assertThat(dateLastLogged.getMinuteOfHour()).isEqualTo(0);
    assertThat(dateLastLogged.getSecondOfMinute()).isEqualTo(0);
    assertThat(beanlet.getBeanCount()).isEqualTo(3);
    assertThat(beanlet.getSortOrder()).isEqualTo(5);
  }

  @Test
  public void testSaveBeanlet() {
    Beanlet beanlet = new Beanlet();
    beanlet.setName("scripture reading");
    beanlet.setUser(userRepository.findOne(HERRMINER));
    beanlet.setDateLastLogged(new DateTime());
    beanlet.setBeanCount(23);
    beanlet.setSortOrder(10);
    beanletRepository.save(beanlet);

    assertThat(beanlet.getId()).isNotNull();
    beanlet = beanletRepository.findOne(beanlet.getId());
    assertThat(beanlet.getName()).isEqualTo("scripture reading");
    assertThat(beanlet.getDateCreated()).isNotNull();
    assertThat(beanlet.getDateModified()).isNotNull();
    assertThat(beanlet.getDateLastLogged()).isNotNull();
    assertThat(beanlet.getBeanCount()).isEqualTo(23);
    assertThat(beanlet.getSortOrder()).isEqualTo(10);
  }

  @Test
  public void testFindAllByUserId() {
    createBeanlet("mar", 1);
    createBeanlet("jan", 3);
    createBeanlet("feb", 2);
    List<Beanlet> beanlets = beanletRepository.findAllByUserIdOrderBySortOrderDesc(FRAUMINER);
    assertThat(beanlets).isNotNull().hasSize(3);
    assertThat(beanlets.get(0).getName()).isEqualTo("jan");
    assertThat(beanlets.get(1).getName()).isEqualTo("feb");
    assertThat(beanlets.get(2).getName()).isEqualTo("mar");
  }

  @Test
  public void testCreateBeanletInUtc() {
    Beanlet beanlet = new Beanlet(userRepository.findOne(HERRMINER), "UTC you know it");
    beanlet.setDateLastLogged(new DateTime(2000, 6, 1, 12, 0, 0, DateTimeZone.forID("America/Chicago")));
    beanletRepository.save(beanlet);
  }

  @Test
  public void testCountByUserId() {
    assertThat(beanletRepository.countByUserId(HERRMINER)).isEqualTo(5);
  }

  private Beanlet createBeanlet(String name, int sortOrder) {
    Beanlet beanlet = new Beanlet();
    beanlet.setName(name);
    beanlet.setUser(userRepository.findOne(FRAUMINER));
    beanlet.setDateLastLogged(new DateTime(2016, 3, 1, 1, 1, 1, 1));
    beanlet.setSortOrder(sortOrder);
    beanletRepository.save(beanlet);
    return beanlet;
  }

}
