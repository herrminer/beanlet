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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BeanService {

  Bean addBean(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime date);

  Bean getMostRecentBean(EntityId<User> userId, EntityId<Beanlet> beanletId);

  List<Bean> getBeans(EntityId<User> userId, EntityId<Beanlet> beanletId);

  int getBeanCount(EntityId<Beanlet> beanletId);

  List<Bean> getBeansForDate(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime dateTime);

  @Service
  class DefaultBeanService implements BeanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanService.class);

    private BeanletAuthorizationService beanletAuthorizationService;

    private BeanRepository beanRepository;

    private static Sort sort = new Sort(Sort.Direction.DESC, "localDate");

    @Override
    public Bean addBean(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime date) {

      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);

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
      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);
      return beanRepository.findFirstByBeanletIdOrderByLocalDateDesc(beanletId);
    }

    @Override
    public List<Bean> getBeans(EntityId<User> userId, EntityId<Beanlet> beanletId) {
      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);
      return beanRepository.findByBeanletId(beanletId, sort);
    }

    @Override
    public int getBeanCount(EntityId<Beanlet> beanletId) {
      return beanRepository.countByBeanletId(beanletId);
    }

    @Override
    public List<Bean> getBeansForDate(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime dateTime) {
      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);
      DateTime beginTime = dateTime.withTime(0, 0, 0, 0);
      DateTime endTime = beginTime.plusDays(1);
      return beanRepository.findByBeanletIdAndLocalDateBetween(beanletId, beginTime, endTime);
    }

    @Autowired
    public void setBeanletAuthorizationService(BeanletAuthorizationService beanletAuthorizationService) {
      this.beanletAuthorizationService = beanletAuthorizationService;
    }

    @Autowired
    public void setBeanRepository(BeanRepository beanRepository) {
      this.beanRepository = beanRepository;
    }
  }

}
