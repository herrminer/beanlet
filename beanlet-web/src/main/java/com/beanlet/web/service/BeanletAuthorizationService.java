package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface BeanletAuthorizationService {

  void checkBeanletAuthorization(EntityId<User> userId, EntityId<Beanlet> beanletId);

  @Service
  class DefaultBeanletAuthorizationService implements BeanletAuthorizationService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanletAuthorizationService.class);

    private BeanletRepository beanletRepository;

    public void checkBeanletAuthorization(EntityId<User> userId, EntityId<Beanlet> beanletId) {
      Beanlet beanlet = beanletRepository.findOne(beanletId);
      if (beanlet != null && !beanlet.getUser().getId().equals(userId)) {
        LOGGER.error("beanlet " + beanletId + " does not belong to user " + userId);
        throw new NotYourBeanException();
      }
    }

    @Autowired
    public void setBeanletRepository(BeanletRepository beanletRepository) {
      this.beanletRepository = beanletRepository;
    }
  }
}
