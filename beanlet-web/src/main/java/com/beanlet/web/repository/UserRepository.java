package com.beanlet.web.repository;

import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, EntityId<User>> {

  /**
   * Get a user by his email address
   */
  User findByEmail(String email);

}
