package com.beanlet.web.repository;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeanletRepository extends JpaRepository<Beanlet, Integer> {

  List<Beanlet> findAllByUserId(EntityId<User> userId);

}
