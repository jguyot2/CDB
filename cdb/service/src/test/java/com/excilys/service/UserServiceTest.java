package com.excilys.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.excilys.model.User;
import com.excilys.model.UserRoles;
import com.excilys.persistence.DaoException;
import com.excilys.persistence.UserSearcher;
import com.excilys.persistence.UserUpdater;

public class UserServiceTest {

    private UserSearcher searcher;

    private UserUpdater updater;

    private UserValidator validator = new UserValidator();

    UserService service = new UserService();

    User user = new User(1L, "pouet", "plopiplop", UserRoles.USER);
    User invalidUser = new User(1L, "", "pouet", UserRoles.USER);

    @Before
    public void init() {
        this.searcher = Mockito.mock(UserSearcher.class);
        this.updater = Mockito.mock(UserUpdater.class);
        this.service = new UserService();
        this.service.setValidator(this.validator);
        this.service.setSearcher(this.searcher);
        this.service.setUpdater(this.updater);
    }

    @Test
    public void testAddUserDbException() throws DaoException, InvalidUserException {
        Mockito.when(this.updater.createUser(Matchers.any())).thenThrow(new DaoException());
        Assert.assertFalse(this.service.create(this.user));
    }

    @Test
    public void testAddUserNullValue() throws DaoException, InvalidUserException {
        try {
            Assert.assertFalse(this.service.create(null));
            Assert.fail();
        } catch (InvalidUserException e) {
            Assert.assertTrue(e.getProblems().contains(UserProblems.NULL_VALUE));
        }
    }

    @Test
    public void testAddUser() throws DaoException, InvalidUserException {
        Mockito.when(this.updater.createUser(Matchers.any())).thenReturn(true);
        Assert.assertTrue(this.service.create(this.user));
    }

    @Test
    public void testAddInvalidUser() throws DaoException, InvalidUserException {
        try {
            Assert.assertFalse(this.service.create(this.invalidUser));
            Assert.fail();
        } catch (InvalidUserException e) {
            Assert.assertTrue(e.getProblems().contains(UserProblems.INVALID_NAME));
        }
    }

    @Test
    public void testUserDetails() throws DaoException {
        Mockito.when(this.searcher.getUserDetails("pouet")).thenReturn(Optional.of(this.user));
        Assert.assertEquals(Optional.of(this.user), this.service.getUserDetails("pouet"));
    }

    @Test
    public void testUserDetailsWithInvalidInput() throws DaoException {
        Assert.assertEquals(Optional.empty(), this.service.getUserDetails(""));
        Assert.assertEquals(Optional.empty(), this.service.getUserDetails(null));
    }

    @Test
    public void testUserDetailsDbException() throws DaoException {
        Mockito.when(this.searcher.getUserDetails("pouet")).thenThrow(new DaoException());
        Assert.assertEquals(Optional.empty(), this.service.getUserDetails("pouet"));
    }

}
