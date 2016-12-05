package com.beanlet.web.service;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.repository.BeanRepository;
import com.beanlet.web.repository.BeanletRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.beanlet.web.TestUtils.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
public class DefaultBeanletSummaryDataUpdaterTest {

  @MockBean
  private BeanRepository beanRepository;

  @MockBean
  private BeanletRepository beanletRepository;

  BeanletSummaryDataUpdater.DefaultBeanletSummaryDataUpdater service;

  @Before
  public void setUp() throws Exception {
    service = new BeanletSummaryDataUpdater.DefaultBeanletSummaryDataUpdater();
    service.setBeanRepository(beanRepository);
    service.setBeanletRepository(beanletRepository);
  }

  @Test
  public void testUpdateSummaryData_happyPath() {
    when(beanRepository.countByBeanletId(EXERCISE)).thenReturn(7);

    DateTime localDate = new DateTime(2016, 1, 1, 0, 0, 0);
    Bean bean = new Bean();
    bean.setLocalDate(localDate);
    when(beanRepository.findFirstByBeanletIdOrderByLocalDateDesc(EXERCISE)).thenReturn(bean);

    Beanlet beanlet = new Beanlet();
    when(beanletRepository.findOne(EXERCISE)).thenReturn(beanlet);

    Beanlet result = service.updateSummaryData(EXERCISE);
    assertThat(result.getDateLastLogged()).isEqualTo(localDate);
    assertThat(result.getBeanCount()).isEqualTo(7);

    verify(beanletRepository, atLeastOnce()).save(beanlet);
  }

}
