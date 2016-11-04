package com.beanlet.web.service;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanRepository;
import com.beanlet.web.repository.BeanletRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface BeanService {

  Bean addBean(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime date);

  Bean getMostRecentBean(EntityId<User> userId, EntityId<Beanlet> beanletId);

  @Service
  class DefaultBeanService implements BeanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanService.class);

    private BeanletRepository beanletRepository;

    private BeanRepository beanRepository;

    @Override
    public Bean addBean(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime date) {

      checkBeanletAuthorization(userId, beanletId);

      Bean bean = new Bean();
      bean.setBeanletId(beanletId);

      // this will be converted and saved to the database in UTC time
      // this represent the actual *instant* the bean took place.
      // this would have been converted to UTC for storage in db anyway, so just doing .withZone(UTC) to be explicit
      bean.setUtcDate(date.withZone(DateTimeZone.UTC));

      // use UTC time zone *but keep the date fields,*
      // thus the database will represent the *local time*
      // this bean was created.
      bean.setLocalDate(date.withZoneRetainFields(DateTimeZone.UTC));
      bean.setLocalTimeZone(date.getZone());

      beanRepository.save(bean);

      return bean;
    }

    @Override
    public Bean getMostRecentBean(EntityId<User> userId, EntityId<Beanlet> beanletId) {
      checkBeanletAuthorization(userId, beanletId);
      return beanRepository.findFirstByBeanletIdOrderByLocalDateDesc(beanletId);
    }

    void checkBeanletAuthorization(EntityId<User> userId, EntityId<Beanlet> beanletId) {
      Beanlet beanlet = beanletRepository.findOne(beanletId);
      if (!beanlet.getUser().getId().equals(userId)) {
        LOGGER.error("beanlet " + beanletId + " does not belong to user " + userId);
        throw new NotYourBeanException();
      }
    }

    @Autowired
    public void setBeanletRepository(BeanletRepository beanletRepository) {
      this.beanletRepository = beanletRepository;
    }

    @Autowired
    public void setBeanRepository(BeanRepository beanRepository) {
      this.beanRepository = beanRepository;
    }
  }

}
