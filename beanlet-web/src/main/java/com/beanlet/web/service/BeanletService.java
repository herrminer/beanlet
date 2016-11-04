package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanletRepository;
import com.beanlet.web.repository.UserRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BeanletService {

  List<Beanlet> getBeanletsForUserId(EntityId<User> userId);

  Beanlet addBeanlet(EntityId<User> userId, String name);

  Beanlet countIt(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTimeZone timeZone);

  @Service
  class DefaultBeanletService implements BeanletService {

    private BeanletRepository beanletRepository;

    private UserRepository userRepository;

    private BeanService beanService;

    @Override
    public List<Beanlet> getBeanletsForUserId(EntityId<User> userId) {
      return beanletRepository.findAllByUserIdOrderByDateLastLoggedDesc(userId);
    }

    @Override
    public Beanlet addBeanlet(EntityId<User> userId, String name) {
      Beanlet beanlet = new Beanlet(userRepository.findOne(userId), name);
      beanletRepository.save(beanlet);
      return beanlet;
    }

    @Override
    public Beanlet countIt(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTimeZone timeZone) {
      User user = userRepository.findOne(userId);
      DateTimeZone zone = timeZone == null ? user.getTimeZone() : timeZone;
      beanService.addBean(userId, beanletId, new DateTime(zone));
      return updateMostRecent(userId, beanletId);
    }

    Beanlet updateMostRecent(EntityId<User> userId, EntityId<Beanlet> beanletId) {
      Beanlet beanlet = beanletRepository.findOne(beanletId);
      beanlet.setDateLastLogged(beanService.getMostRecentBean(userId, beanletId).getLocalDate());
      beanletRepository.save(beanlet);
      return beanlet;
    }

    @Autowired
    public void setBeanletRepository(BeanletRepository beanletRepository) {
      this.beanletRepository = beanletRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
      this.userRepository = userRepository;
    }

    @Autowired
    public void setBeanService(BeanService beanService) {
      this.beanService = beanService;
    }
  }

}
