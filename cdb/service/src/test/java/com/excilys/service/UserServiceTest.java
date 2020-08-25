package com.excilys.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.model.User;
import com.excilys.model.UserRoles;
import com.excilys.persistence.PersistanceException;
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

    User user = new User(1L, "pouet", "plopiplop", UserRoles.USER);
    User invalidUser = new User(1L, "", "pouet", UserRoles.USER);

    @Before
    public void init() {
        this.searcher = Mockito.mock(UserSearcher.class);
        this.updater = Mockito.mock(UserUpdater.class);
        this.service.setSearcher(this.searcher);
        this.service.setUpdater(this.updater);
    }

    @Test
    public void testAddUserDbException() throws PersistanceException, InvalidUserException {
        Mockito.when(this.updater.createUser(Matchers.any())).thenThrow(new PersistanceException());
        Assert.assertFalse(this.service.create(this.user));
    }

    @Test
    public void testAddUserNullValue() throws PersistanceException, InvalidUserException {
        try {
            Assert.assertFalse(this.service.create(null));
            Assert.fail();
        } catch (InvalidUserException e) {
            Assert.assertTrue(e.getProblems().contains(UserProblems.NULL_VALUE));
        }
    }

    @Test
    public void testAddUser() throws PersistanceException, InvalidUserException {
        Mockito.when(this.updater.createUser(Matchers.any())).thenReturn(true);
        Assert.assertTrue(this.service.create(this.user));
    }

    @Test
    public void testAddInvalidUser() throws PersistanceException, InvalidUserException {
        try {
            Assert.assertFalse(this.service.create(this.invalidUser));
            Assert.fail();
        } catch (InvalidUserException e) {
            Assert.assertTrue(e.getProblems().contains(UserProblems.INVALID_NAME));
        }
    }

    @Test
    public void testUserDetails() throws PersistanceException {
        Mockito.when(this.searcher.getUserDetails("pouet")).thenReturn(Optional.of(this.user));
        Assert.assertEquals(Optional.of(this.user), this.service.getUserDetails("pouet"));
    }

    @Test
    public void testUserDetailsWithInvalidInput() throws PersistanceException {
        Assert.assertEquals(Optional.empty(), this.service.getUserDetails(""));
        Assert.assertEquals(Optional.empty(), this.service.getUserDetails(null));
    }

    @Test
    public void testUserDetailsDbException() throws PersistanceException {
        Mockito.when(this.searcher.getUserDetails("pouet")).thenThrow(new PersistanceException());
        Assert.assertEquals(Optional.empty(), this.service.getUserDetails("pouet"));
    }

}
