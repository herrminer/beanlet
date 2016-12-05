package com.beanlet.web.service;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanRepository;
import com.beanlet.web.repository.BeanletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface BeanletSummaryDataUpdater {

  Beanlet updateSummaryData(EntityId<Beanlet> beanletId);

  @Service
  class DefaultBeanletSummaryDataUpdater implements BeanletSummaryDataUpdater {

    private BeanletRepository beanletRepository;
    private BeanRepository beanRepository;

    public Beanlet updateSummaryData(EntityId<Beanlet> beanletId) {
      Beanlet beanlet = beanletRepository.findOne(beanletId);
      Bean mostRecentBean = beanRepository.findFirstByBeanletIdOrderByLocalDateDesc(beanletId);

      beanlet.setDateLastLogged(mostRecentBean.getLocalDate());
      beanlet.setBeanCount(beanRepository.countByBeanletId(beanletId));
      beanletRepository.save(beanlet);

      return beanlet;
    }

    @Autowired
    public void setBeanRepository(BeanRepository beanRepository) {
      this.beanRepository = beanRepository;
    }

    @Autowired
    public void setBeanletRepository(BeanletRepository beanletRepository) {
      this.beanletRepository = beanletRepository;
    }
  }
}
