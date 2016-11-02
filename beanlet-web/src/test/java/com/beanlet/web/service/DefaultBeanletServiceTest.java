package com.beanlet.web.service;

import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.repository.BeanletRepository;
import com.beanlet.web.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class DefaultBeanletServiceTest {

  private BeanletService.DefaultBeanletService service = new BeanletService.DefaultBeanletService();

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private BeanletRepository beanletRepository;

  @Before
  public void setUp() throws Exception {
    service = new BeanletService.DefaultBeanletService();
    service.setUserRepository(userRepository);
    service.setBeanletRepository(beanletRepository);
  }

  @Test
  public void testGetAllBeanletsForUser() {
    service.getBeanletsForUserId(new EntityId<>("1234567890"));
    verify(beanletRepository).findAllByUserId(new EntityId<>("1234567890"));
  }

}
