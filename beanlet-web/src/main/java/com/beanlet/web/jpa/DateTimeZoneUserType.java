package com.beanlet.web.jpa;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.usertype.EnhancedUserType;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DateTimeZoneUserType implements EnhancedUserType, ResultSetIdentifierConsumer {

  @Override
  public String objectToSQLString(Object value) {
    return value.toString();
  }

  @Override
  public String toXMLString(Object value) {
    return value.toString();
  }

  @Override
  public Object fromXMLString(String xmlValue) {
    return DateTimeZone.forID(xmlValue);
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.VARCHAR};
  }

  @Override
  public Class returnedClass() {
    return DateTimeZone.class;
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    return (x == y) || (x != null && y != null && (x.equals(y)));
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    return x != null ? x.hashCode() : 0;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    String value = rs.getString(names[0]);
    return rs.wasNull() ? null : DateTimeZone.forID(value);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      st.setNull(index, Types.VARCHAR);
    }
    else {
      st.setString(index, value.toString());
    }
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    return value.toString();
  }

  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return DateTimeZone.forID((String) cached);
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }

  @Override
  public Serializable consumeIdentifier(ResultSet resultSet) {
    try {
      return DateTimeZone.forID(resultSet.getString(1));
    }
    catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
