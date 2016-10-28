package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.BeanletRepository;
import com.beanlet.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BeanletService {

  List<Beanlet> getBeanletsForUserId(EntityId<User> userId);

  Beanlet addBeanlet(EntityId<User> userId, String name);

  @Service
  class DefaultBeanletService implements BeanletService {

    public DefaultBeanletService(BeanletRepository beanletRepository, UserRepository userRepository) {
      this.beanletRepository = beanletRepository;
      this.userRepository = userRepository;
    }

    private BeanletRepository beanletRepository;

    private UserRepository userRepository;

    @Override
    public List<Beanlet> getBeanletsForUserId(EntityId<User> userId) {
      return beanletRepository.findAllByUserId(userId);
    }

    @Override
    public Beanlet addBeanlet(EntityId<User> userId, String name) {
      Beanlet beanlet = new Beanlet(userRepository.findOne(userId), name);
      beanletRepository.save(beanlet);
      return beanlet;
    }
  }

}
