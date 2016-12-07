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

  List<Bean> getBeans(EntityId<User> userId, EntityId<Beanlet> beanletId);

  List<Bean> getBeansForDate(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime dateTime);

  Bean modifyBean(EntityId<User> userId, EntityId<Beanlet> beanletId, EntityId<Bean> beanId, DateTime dateTime);

  Bean deleteBean(EntityId<User> userId, EntityId<Beanlet> beanletId, EntityId<Bean> beanId);

  @Service
  class DefaultBeanService implements BeanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanService.class);

    private BeanletAuthorizationService beanletAuthorizationService;

    private BeanletSummaryDataUpdater beanletSummaryDataUpdater;

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

      beanletSummaryDataUpdater.updateSummaryData(beanletId);

      LOGGER.debug("added bean " + bean.getId());

      return bean;
    }

    @Override
    public List<Bean> getBeans(EntityId<User> userId, EntityId<Beanlet> beanletId) {
      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);
      return beanRepository.findByBeanletId(beanletId, sort);
    }

    @Override
    public List<Bean> getBeansForDate(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTime dateTime) {
      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);
      DateTime beginTime = dateTime.withTime(0, 0, 0, 0);
      DateTime endTime = dateTime.withTime(23, 59, 59, 999);
      return beanRepository.findByBeanletIdAndLocalDateBetween(beanletId, beginTime, endTime);
    }

    /**
     * todo: add test for this method!
     */
    @Override
    public Bean modifyBean(EntityId<User> userId, EntityId<Beanlet> beanletId, EntityId<Bean> beanId, DateTime localDate) {
      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);
      Bean bean = beanRepository.findOne(beanId);

      if (bean == null) {
        throw new IllegalArgumentException("no such bean with ID: " + beanId);
      }

      // make sure the bean belongs to this beanlet
      if (!bean.getBeanletId().equals(beanletId)) {
        throw new NotYourBeanException();
      }

      /**
       * this could potentially be a different time zone than was originally used.
       * maybe later i should consider honoring the original time zone...not sure
       */
      bean.setLocalTimeZone(localDate.getZone());

      /**
       * localDate is in the user's time zone, so need to change this to UTC
       * and retain fields so when it's later converted to UTC the fields are
       * the same.
       */
      bean.setLocalDate(localDate.withZoneRetainFields(DateTimeZone.UTC));

      /**
       * Setting the UTC date. the withZone=UTC part would have been done implicitly...doing it here just for clarity.
       */
      bean.setUtcDate(localDate.withZone(DateTimeZone.UTC)); // conversion would have happened anyway

      beanRepository.save(bean);

      beanletSummaryDataUpdater.updateSummaryData(beanletId);

      return bean;
    }

    @Override
    public Bean deleteBean(EntityId<User> userId, EntityId<Beanlet> beanletId, EntityId<Bean> beanId) {
      beanletAuthorizationService.checkBeanletAuthorization(userId, beanletId);
      Bean bean = beanRepository.findOne(beanId);

      if (bean == null) {
        throw new IllegalArgumentException("no such bean with ID: " + beanId);
      }

      // make sure the bean belongs to this beanlet
      if (!bean.getBeanletId().equals(beanletId)) {
        throw new NotYourBeanException();
      }

      beanRepository.delete(bean);

      beanletSummaryDataUpdater.updateSummaryData(beanletId);

      return bean;
    }

    @Autowired
    public void setBeanletAuthorizationService(BeanletAuthorizationService beanletAuthorizationService) {
      this.beanletAuthorizationService = beanletAuthorizationService;
    }

    @Autowired
    public void setBeanRepository(BeanRepository beanRepository) {
      this.beanRepository = beanRepository;
    }

    @Autowired
    public void setBeanletSummaryDataUpdater(BeanletSummaryDataUpdater beanletSummaryDataUpdater) {
      this.beanletSummaryDataUpdater = beanletSummaryDataUpdater;
    }
  }

}
