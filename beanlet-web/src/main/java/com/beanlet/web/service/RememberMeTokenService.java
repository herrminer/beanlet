package com.beanlet.web.service;

import com.beanlet.web.jpa.RememberMeToken;
import com.beanlet.web.repository.RememberMeTokenRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RememberMeTokenService implements PersistentTokenRepository {

  private static final Logger logger = LoggerFactory.getLogger(RememberMeTokenService.class);

  @Autowired
  private RememberMeTokenRepository rememberMeTokenRepository;

  @Override
  public void createNewToken(PersistentRememberMeToken token) {
    RememberMeToken rememberMeToken = new RememberMeToken(
      token.getSeries(),
      token.getUsername(),
      token.getTokenValue(),
      new DateTime(token.getDate())
    );
    rememberMeTokenRepository.save(rememberMeToken);
  }

  @Override
  public void updateToken(String series, String tokenValue, Date lastUsed) {
    logger.debug("updating token for series: " + series + " to value " + tokenValue + " and last used " + lastUsed);

    RememberMeToken rememberMeToken = rememberMeTokenRepository.findOne(series);

    if (rememberMeToken == null) {
      logger.warn("no remember me token found for series: " + series);
      return;
    }

    rememberMeToken.setToken(tokenValue);
    rememberMeToken.setLastUsed(new DateTime(lastUsed));

    rememberMeTokenRepository.save(rememberMeToken);
  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(String seriesId) {
    PersistentRememberMeToken token = null;

    RememberMeToken rememberMeToken = rememberMeTokenRepository.findOne(seriesId);

    if (rememberMeToken != null) {
      token = new PersistentRememberMeToken(
        rememberMeToken.getUsername(),
        rememberMeToken.getSeries(),
        rememberMeToken.getToken(),
        rememberMeToken.getLastUsed().toDate());
    }

    return token;
  }

  @Transactional
  @Override
  public void removeUserTokens(String username) {
    rememberMeTokenRepository.deleteByUsername(username);
  }
}
