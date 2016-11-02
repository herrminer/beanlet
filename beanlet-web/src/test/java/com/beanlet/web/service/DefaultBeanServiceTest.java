package com.beanlet.web.service;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanRepository;
import com.beanlet.web.repository.BeanletRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.beanlet.web.TestUtils.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
public class DefaultBeanServiceTest {

  private BeanService.DefaultBeanService service;

  @MockBean
  private BeanletRepository beanletRepository;

  @MockBean
  private BeanRepository beanRepository;

  @Before
  public void setUp() throws Exception {
    service = new BeanService.DefaultBeanService(){
      @Override
      void checkBeanletAuthorization(EntityId<User> userId, EntityId<Beanlet> beanletId) {
        // do nothing for now...test this separately
      }
    };
    service.setBeanletRepository(beanletRepository);
    service.setBeanRepository(beanRepository);

  }

  @Test
  public void addBean() throws Exception {
    DateTime date = testDateCentral();
    Bean bean = service.addBean(HERRMINER, EXERCISE, date);
    assertThat(bean).isNotNull();
    assertThat(bean.getDateUtc()).isEqualTo(date);
    assertThat(bean.getDateLocal()).isEqualTo(date.withZoneRetainFields(DateTimeZone.UTC));
    verify(beanRepository).save(isA(Bean.class));
  }

  @Test
  public void getMostRecentBean() throws Exception {
    when(beanRepository.findFirstByBeanletIdOrderByDateLocalDesc(EXERCISE)).thenReturn(new Bean());
    Bean bean = service.getMostRecentBean(HERRMINER, EXERCISE);
    assertThat(bean).isNotNull();
    verify(beanRepository).findFirstByBeanletIdOrderByDateLocalDesc(EXERCISE);
  }

  @Test
  public void checkBeanletAuthorization_noProblem() throws Exception {
    BeanService.DefaultBeanService service = new BeanService.DefaultBeanService();
    service.setBeanletRepository(beanletRepository);
    User user = new User(HERRMINER.getValue());
    Beanlet beanlet = new Beanlet(user, "foo");
    when(beanletRepository.findOne(EXERCISE)).thenReturn(beanlet);
    service.checkBeanletAuthorization(HERRMINER, EXERCISE);
    verify(beanletRepository).findOne(EXERCISE);
  }

  @Test(expected = NotYourBeanException.class)
  public void checkBeanletAuthorization_authIssue() throws Exception {
    BeanService.DefaultBeanService service = new BeanService.DefaultBeanService();
    service.setBeanletRepository(beanletRepository);
    User user = new User(uuid());
    Beanlet beanlet = new Beanlet(user, "foo");
    when(beanletRepository.findOne(EXERCISE)).thenReturn(beanlet);
    service.checkBeanletAuthorization(HERRMINER, EXERCISE);
    verify(beanletRepository).findOne(EXERCISE);
  }

}