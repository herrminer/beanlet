package com.beanlet.web.service;

import com.beanlet.web.jpa.User;
import com.beanlet.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
  List<User> getAllUsers();

  @Service
  class DefaultUserService implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
      return userRepository.findAll();
    }
  }
}
