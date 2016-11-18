package com.beanlet.web.repository;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeanRepository extends JpaRepository<Bean, EntityId<Bean>> {

  List<Bean> findByBeanletId(EntityId<Beanlet> beanletId, Sort sort);

  Long countByBeanletIdAndLocalDateBetween(EntityId<Beanlet> beanletId, DateTime start, DateTime end);

  List<Bean> findByBeanletIdAndLocalDateBetween(EntityId<Beanlet> beanletId, DateTime start, DateTime end);

  int countByBeanletId(EntityId<Beanlet> beanletId);

  Bean findFirstByBeanletIdOrderByLocalDateDesc(EntityId<Beanlet> beanletId);

}
