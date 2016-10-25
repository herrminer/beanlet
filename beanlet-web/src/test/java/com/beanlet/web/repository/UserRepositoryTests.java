package com.beanlet.web.repository;

import com.beanlet.web.jpa.User;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

  @Autowired
  private UserRepository repository;

  @Autowired
  private Environment environment;

  @Test
  public void testSaveUser() throws Exception {
    User user = new User();
    user.setEmail("test@foo.com");
    user.setPassword("pwd");
    repository.save(user);
    assertThat(repository.findOne(user.getId())).isNotNull();
    assertThat(repository.findByEmail("test@foo.com")).isNotNull();
  }
}
