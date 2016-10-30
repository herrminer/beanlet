package com.beanlet.web.jpa;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserRole extends AbstractEntity<UserRole> implements GrantedAuthority {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public RoleType getRoleType() {
    return roleType;
  }

  public void setRoleType(RoleType roleType) {
    this.roleType = roleType;
  }

  @Override
  public String getAuthority() {
    return roleType.toString();
  }

  public enum RoleType {
    ROLE_USER
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("roleType", roleType)
      .toString();
  }
}
