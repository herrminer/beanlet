package com.beanlet.web.jpa;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class EntityId<T> implements Serializable {

  private String value;

  public EntityId(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public int length() {
    return value == null ? 0 : value.length();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    EntityId<?> id = (EntityId<?>) o;

    return new EqualsBuilder()
      .append(value, id.value)
      .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(value)
      .toHashCode();
  }

  @Override
  public String toString() {
    return value;
  }
}
