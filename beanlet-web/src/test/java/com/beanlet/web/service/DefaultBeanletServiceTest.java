package com.beanlet.web.service;

import com.beanlet.web.TestUtils;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.repository.BeanletRepository;
import com.beanlet.web.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DefaultBeanletServiceTest {

  private BeanletService.DefaultBeanletService service = new BeanletService.DefaultBeanletService();

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private BeanletRepository beanletRepository;

  @Before
  public void setUp() throws Exception {
    service = new BeanletService.DefaultBeanletService();
    service.setUserRepository(userRepository);
    service.setBeanletRepository(beanletRepository);
  }

  @Test
  public void testGetAllBeanletsForUser() {
    service.getBeanletsForUserId(new EntityId<>("1234567890"));
    verify(beanletRepository).findAllByUserIdOrderBySortOrderDesc(new EntityId<>("1234567890"));
  }

  @Test
  public void testSortBeanlets() {
    List<Beanlet> beanlets = new ArrayList<>(5);
    beanlets.add(createBeanlet("a", "1"));
    beanlets.add(createBeanlet("e", "5"));
    beanlets.add(createBeanlet("b", "2"));
    beanlets.add(createBeanlet("d", "4"));
    beanlets.add(createBeanlet("c", "3"));
    when(beanletRepository.findAllByUserIdOrderBySortOrderDesc(TestUtils.HERRMINER)).thenReturn(beanlets);

    List<EntityId<Beanlet>> entityIds = service.sortBeanlets(TestUtils.HERRMINER, SortBy.NAME);

    verify(beanletRepository, times(5)).save(isA(Beanlet.class));
    for (int i=0; i < entityIds.size(); i++) {
      assertThat(entityIds.get(i).getValue()).isEqualTo(""+(i + 1));
    }
  }

  Beanlet createBeanlet(String name, String id) {
    Beanlet beanlet = new Beanlet(null, name);
    beanlet.setId(new EntityId<>(id));
    return beanlet;
  }

}
