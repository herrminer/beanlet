package com.beanlet.web.jpa;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class EntityIdGenerator implements IdentifierGenerator {
  @Override
  public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
    return new EntityId(UUID.randomUUID().toString().replace("-", ""));
  }
}
