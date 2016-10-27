package com.beanlet.web.repository;

import com.beanlet.web.jpa.RememberMeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RememberMeTokenRepository extends JpaRepository<RememberMeToken, String> {

  List<RememberMeToken> findByUsername(String username);

  @Modifying
  @Query("delete RememberMeToken r where r.username = ?1")
  void deleteByUsername(String username);

}
