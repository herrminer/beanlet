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
  private BeanletAuthorizationService beanletAuthorizationService;

  @MockBean
  private BeanRepository beanRepository;

  @Before
  public void setUp() throws Exception {
    service = new BeanService.DefaultBeanService();
    service.setBeanletAuthorizationService(beanletAuthorizationService);
    service.setBeanRepository(beanRepository);

  }

  @Test
  public void addBean() throws Exception {
    DateTime date = testDateCentral();
    Bean bean = service.addBean(HERRMINER, EXERCISE, date);
    assertThat(bean).isNotNull();
    assertThat(bean.getUtcDate().getMillis()).isEqualTo(date.getMillis());
    assertThat(bean.getLocalDate()).isEqualTo(date.withZoneRetainFields(DateTimeZone.UTC));
    verify(beanRepository).save(isA(Bean.class));
  }

  @Test
  public void getMostRecentBean() throws Exception {
    when(beanRepository.findFirstByBeanletIdOrderByLocalDateDesc(EXERCISE)).thenReturn(new Bean());
    Bean bean = service.getMostRecentBean(HERRMINER, EXERCISE);
    assertThat(bean).isNotNull();
    verify(beanRepository).findFirstByBeanletIdOrderByLocalDateDesc(EXERCISE);
  }

  @Test
  public void testGetBeansForDate() {
    DateTime date = new DateTime(2016, 11, 22, 1, 22, 33);
    service.getBeansForDate(HERRMINER, EXERCISE, date);
    verify(beanletAuthorizationService).checkBeanletAuthorization(HERRMINER, EXERCISE);
    verify(beanRepository).findByBeanletIdAndLocalDateBetween(eq(EXERCISE),
      eq(new DateTime(2016, 11, 22, 0, 0, 0)), eq(new DateTime(2016, 11, 23, 0, 0, 0)));
  }

}