@TypeDefs({
  @TypeDef(defaultForType = EntityId.class, typeClass = EntityIdUserType.class),
  @TypeDef(defaultForType = DateTimeZone.class, typeClass = DateTimeZoneUserType.class)
})
package com.beanlet.web.jpa;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.joda.time.DateTimeZone;