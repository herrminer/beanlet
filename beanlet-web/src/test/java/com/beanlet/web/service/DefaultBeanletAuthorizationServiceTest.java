package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanletRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.beanlet.web.TestUtils.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class DefaultBeanletAuthorizationServiceTest {

  private BeanletAuthorizationService.DefaultBeanletAuthorizationService service;

  @MockBean
  private BeanletRepository beanletRepository;

  @Before
  public void setUp() throws Exception {
    service = new BeanletAuthorizationService.DefaultBeanletAuthorizationService();
    service.setBeanletRepository(beanletRepository);
  }

  @Test
  public void checkBeanletAuthorization_allGood() throws Exception {
    User user = new User(HERRMINER.getValue());
    Beanlet beanlet = new Beanlet(user, "foo");
    when(beanletRepository.findOne(EXERCISE)).thenReturn(beanlet);
    service.checkBeanletAuthorization(HERRMINER, EXERCISE);
  }

  @Test(expected = NotYourBeanException.class)
  public void checkBeanletAuthorization_noGood() throws Exception {
    User user = new User(FRAUMINER.getValue());
    Beanlet beanlet = new Beanlet(user, "foo");
    when(beanletRepository.findOne(EXERCISE)).thenReturn(beanlet);
    service.checkBeanletAuthorization(HERRMINER, EXERCISE);
  }

}