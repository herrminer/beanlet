package com.beanlet.web.repository;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeanRepository extends JpaRepository<Bean, EntityId<Bean>> {

  List<Bean> findByBeanletId(EntityId<Beanlet> beanletId);

}
