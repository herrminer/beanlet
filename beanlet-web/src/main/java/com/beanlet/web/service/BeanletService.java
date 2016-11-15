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

import java.util.ArrayList;
import java.util.List;

public interface BeanletService {

  List<Beanlet> getBeanletsForUserId(EntityId<User> userId);

  Beanlet addBeanlet(EntityId<User> userId, String name);

  Beanlet countIt(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTimeZone timeZone);

  List<EntityId<Beanlet>> sortBeanlets(EntityId<User> id, SortBy sortBy);

  @Service
  class DefaultBeanletService implements BeanletService {

    private BeanletRepository beanletRepository;

    private UserRepository userRepository;

    private BeanService beanService;

    @Override
    public List<Beanlet> getBeanletsForUserId(EntityId<User> userId) {
      return beanletRepository.findAllByUserIdOrderBySortOrderDesc(userId);
    }

    @Override
    public Beanlet addBeanlet(EntityId<User> userId, String name) {
      User user = userRepository.findOne(userId);
      Beanlet beanlet = new Beanlet(user, name);
      beanlet.setSortOrder(beanletRepository.countByUserId(userId) + 1);
      beanletRepository.save(beanlet);
      return beanlet;
    }

    @Override
    public Beanlet countIt(EntityId<User> userId, EntityId<Beanlet> beanletId, DateTimeZone timeZone) {
      User user = userRepository.findOne(userId);
      DateTimeZone zone = timeZone == null ? user.getTimeZone() : timeZone;
      beanService.addBean(userId, beanletId, new DateTime(zone));
      return updateBeanletDataFields(userId, beanletId);
    }

    @Override
    public List<EntityId<Beanlet>> sortBeanlets(EntityId<User> userId, SortBy sortBy) {
      List<Beanlet> beanlets = beanletRepository.findAllByUserIdOrderBySortOrderDesc(userId);
      List<EntityId<Beanlet>> entityIds = new ArrayList<>(beanlets.size());
      beanlets.sort(sortBy.getComparator());
      Beanlet beanlet;
      for (int i = 0, sortOrder = beanlets.size(); i < beanlets.size(); i++, sortOrder--) {
        beanlet = beanlets.get(i);
        beanlet.setSortOrder(sortOrder);
        beanletRepository.save(beanlet);
        entityIds.add(beanlet.getId());
      }
      return entityIds;
    }

    Beanlet updateBeanletDataFields(EntityId<User> userId, EntityId<Beanlet> beanletId) {
      Beanlet beanlet = beanletRepository.findOne(beanletId);
      beanlet.setDateLastLogged(beanService.getMostRecentBean(userId, beanletId).getLocalDate());
      beanlet.setBeanCount(beanService.getBeanCount(beanletId));
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
