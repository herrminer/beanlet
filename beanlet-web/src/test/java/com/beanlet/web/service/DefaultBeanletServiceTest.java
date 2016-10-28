package com.beanlet.web.service;

import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.repository.BeanletRepository;
import com.beanlet.web.repository.UserRepository;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultBeanletServiceTest {

  @Test
  public void testGetAllBeanletsForUser() {
    BeanletRepository repository = mock(BeanletRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    BeanletService.DefaultBeanletService service =
      new BeanletService.DefaultBeanletService(repository, userRepository);
    service.getBeanletsForUserId(new EntityId<>("1234567890"));
    verify(repository).findAllByUserId(new EntityId<>("1234567890"));
  }

}
