package com.beanlet.web.repository;

import com.beanlet.web.jpa.EntityId;
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
  public void testGetUser() {
    User user = repository.findOne(new EntityId<>("ff4c5073e2ec4d8eb42c5786a904a444"));
    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo("herrminer@gmail.com");
    assertThat(user.getRoles()).isNotNull().isNotEmpty().hasSize(1);
  }

//  @Test
//  public void testSaveUser() throws Exception {
//    User user = new User();
//    user.setEmail("test@foo.com");
//    user.setPassword("pwd");
//    repository.save(user);
//    assertThat(repository.findOne(new EntityId<>("1234567890"))).isNotNull();
//    assertThat(repository.findByEmail("test@foo.com")).isNotNull();
//  }
}
