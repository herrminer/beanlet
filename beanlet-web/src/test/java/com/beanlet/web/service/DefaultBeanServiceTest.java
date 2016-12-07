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
import org.mockito.Mock;
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

  @MockBean
  private BeanletSummaryDataUpdater beanletSummaryDataUpdater;

  @Before
  public void setUp() throws Exception {
    service = new BeanService.DefaultBeanService();
    service.setBeanletAuthorizationService(beanletAuthorizationService);
    service.setBeanRepository(beanRepository);
    service.setBeanletSummaryDataUpdater(beanletSummaryDataUpdater);
  }

  @Test
  public void addBean() throws Exception {
    DateTime date = testDateCentral();
    Bean bean = service.addBean(HERRMINER, EXERCISE, date);
    assertThat(bean).isNotNull();
    assertThat(bean.getUtcDate().getMillis()).isEqualTo(date.getMillis());
    assertThat(bean.getLocalDate()).isEqualTo(date.withZoneRetainFields(DateTimeZone.UTC));
    verify(beanRepository).save(isA(Bean.class));
    verify(beanletSummaryDataUpdater, times(1)).updateSummaryData(any(EntityId.class));
  }

  @Test
  public void testGetBeansForDate() {
    DateTime date = new DateTime(2016, 11, 22, 1, 22, 33);
    service.getBeansForDate(HERRMINER, EXERCISE, date);
    verify(beanletAuthorizationService).checkBeanletAuthorization(HERRMINER, EXERCISE);
    verify(beanRepository).findByBeanletIdAndLocalDateBetween(eq(EXERCISE),
      eq(new DateTime(2016, 11, 22, 0, 0, 0)), eq(new DateTime(2016, 11, 22, 23, 59, 59, 999)));
  }

  @Test
  public void testDeleteBean_happyPath() {
    Bean bean = new Bean();
    bean.setBeanletId(EXERCISE);
    when(beanRepository.findOne(BEAN_ID)).thenReturn(bean);
    service.deleteBean(HERRMINER, EXERCISE, BEAN_ID);
    verify(beanletAuthorizationService).checkBeanletAuthorization(HERRMINER, EXERCISE);
    verify(beanRepository).delete(bean);
    verify(beanletSummaryDataUpdater, times(1)).updateSummaryData(EXERCISE);
  }

  @Test(expected = NotYourBeanException.class)
  public void testDeleteBean_beanNotPartOfBeanlet() {
    Bean bean = new Bean();
    bean.setBeanletId(new EntityId<>("someotherid"));
    when(beanRepository.findOne(BEAN_ID)).thenReturn(bean);
    service.deleteBean(HERRMINER, EXERCISE, BEAN_ID);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeleteBean_badBeanId() {
    service.deleteBean(HERRMINER, EXERCISE, BEAN_ID);
  }

}