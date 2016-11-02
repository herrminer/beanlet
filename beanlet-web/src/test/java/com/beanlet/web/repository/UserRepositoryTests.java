package com.beanlet.web.repository;

import com.beanlet.web.jpa.User;
import com.beanlet.web.jpa.UserRole;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import static com.beanlet.web.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

  @Autowired
  private UserRepository repository;

  @Test
  public void testRetrieveUser() {
    User user = repository.findOne(HERRMINER);
    assertThat(user).isNotNull();
    assertThat(user.getRoles()).hasSize(1);
    assertThat(user.getRoles().get(0).getRoleType()).isEqualTo(UserRole.RoleType.ROLE_USER);
    assertThat(user.getTimeZone()).isEqualTo(DateTimeZone.forID("America/Chicago"));
  }

  @Commit
  @Test
  public void testSaveUser() throws Exception {
    User user = new User();
    user.setEmail("test@foo.com");
    user.setPassword("pwd");
    user.setTimeZone(DateTimeZone.forID("America/Chicago"));
    repository.save(user);
    assertThat(user.getId()).isNotNull();
    assertThat(user.getId().length()).isEqualTo(32);
    assertThat(repository.findOne(user.getId())).isNotNull();
    assertThat(repository.findByEmail("test@foo.com")).isNotNull();
  }
}
