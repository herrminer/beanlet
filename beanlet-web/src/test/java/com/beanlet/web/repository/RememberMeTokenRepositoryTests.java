package com.beanlet.web.repository;

import com.beanlet.web.jpa.RememberMeToken;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RememberMeTokenRepositoryTests {

  @Autowired
  private RememberMeTokenRepository repository;

  @Test
  public void testSaveToken() {
    RememberMeToken token = new RememberMeToken(
      "12345", "herrminer@gmail.com", "1234-1234-1234", new DateTime(2016, 1, 1, 12, 0, 0)
    );
    repository.save(token);
    RememberMeToken savedToken = repository.findOne(token.getSeries());
    assertThat(savedToken).isNotNull();
    assertThat(savedToken.getSeries()).isEqualTo("12345");
    assertThat(savedToken.getUsername()).isEqualTo("herrminer@gmail.com");
    assertThat(savedToken.getToken()).isEqualTo("1234-1234-1234");
    assertThat(savedToken.getLastUsed()).isEqualTo(new DateTime(2016, 1, 1, 12, 0, 0));
  }

  @Test
  public void testDeleteByUsername() {
    String username = "testfoo@gmail.com";
    RememberMeToken token = new RememberMeToken(
      "12345", username, "1234-1234-1234", new DateTime(2016, 1, 1, 12, 0, 0)
    );
    repository.save(token);
    assertThat(repository.findByUsername(username).size()).isEqualTo(1);
    repository.deleteByUsername(username);
    assertThat(repository.findByUsername(username).size()).isEqualTo(0);
  }

}
