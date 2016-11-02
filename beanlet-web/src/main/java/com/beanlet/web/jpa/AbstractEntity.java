package com.beanlet.web.jpa;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity<T> {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name="uuid", strategy = "com.beanlet.web.jpa.EntityIdGenerator")
  private EntityId<T> id;

  private DateTime dateCreated;

  private DateTime dateModified;

  public AbstractEntity() {
    // no-arg
  }

  public AbstractEntity(String id) {
    this.id = new EntityId<>(id);
  }

  @Version
  private Integer version;

  public EntityId<T> getId() {
    return id;
  }

  public void setId(EntityId<T> id) {
    this.id = id;
  }

  public DateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(DateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public DateTime getDateModified() {
    return dateModified;
  }

  public void setDateModified(DateTime dateModified) {
    this.dateModified = dateModified;
  }

  @PrePersist
  protected void prePersist() {
    dateCreated = new DateTime();
    dateModified = new DateTime();
  }

  @PreUpdate
  protected void preUpdate() {
    dateModified = new DateTime();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    AbstractEntity<?> that = (AbstractEntity<?>) o;

    return new EqualsBuilder()
      .append(id, that.id)
      .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(id)
      .toHashCode();
  }

  @Override
  public String toString() {
    return "AbstractEntity{" +
      "id=" + id +
      '}';
  }
}
