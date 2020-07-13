package com.excilys.service;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.persistence.UserSearcher;
import com.excilys.persistence.UserUpdater;
import com.excilys.persistence.config.PersistenceConfig;
import com.excilys.serviceconfig.ServiceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class, PersistenceConfig.class })
public class UserServiceTest {
    @Mock
    private UserSearcher searcher;
    @Mock
    private UserUpdater updater;

    @Autowired
    UserService service;

    public void init() {
        this.searcher = Mockito.mock(UserSearcher.class);
        this.updater = Mockito.mock(UserUpdater.class);
    }
}
