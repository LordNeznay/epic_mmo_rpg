package main;
/*
import dbservice.DBService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by uschsh on 01.11.15.
 *//*
public class AccountServiceTest {
    private static AccountService s_accountService;
    private static DBService dbService;
    private static UserProfile s_testFirstUser = new UserProfile("testLogin1", "testPassword1", "testEmail1");
    private static UserProfile s_testSecondUser = new UserProfile("testLogin2", "testPassword2", "testEmail2");

    private static String s_sessionIdSecond = "sessioidsecond";

    @BeforeClass
    public static void setUp() throws Exception {
        dbService   =   new DBService("test");
        s_accountService = new AccountService(dbService);
        s_accountService.addUser(s_testFirstUser.getLogin(), s_testFirstUser);
        s_accountService.addUser(s_testSecondUser.getLogin(), s_testSecondUser);

        s_accountService.addSession(s_sessionIdSecond, s_testSecondUser);
    }

    @AfterClass
    public static void after() throws IOException {
        dbService.shutdown();
    }

    @Test
    public void testAddUser() throws Exception {
        UserProfile newUser = new UserProfile("newUser", "password", "email1");

        s_accountService.addUser(newUser.getLogin(), newUser);

        UserProfile user = s_accountService.getUserByName(newUser.getLogin());

        assertNotNull(user);

        assertEquals(newUser.getLogin(), user.getLogin());
        assertEquals(newUser.getEmail(), user.getEmail());
        assertEquals(newUser.getPassword(), user.getPassword());

        s_accountService.deleteUserByName(newUser.getLogin());
    }

    @Test
    public void testAddExistingUser() throws Exception {
        assertFalse(s_accountService.addUser(s_testFirstUser.getLogin(), s_testFirstUser));
    }

    @Test
    public void testAuthorization() throws Exception {
        String sessionIdFirst = "sessionidfirst";

        assertTrue(s_accountService.addSession(sessionIdFirst, s_testFirstUser));
    }

    @Test
    public void testNoAuthorization() throws Exception {
        UserProfile notRegisteredUser = new UserProfile("login", "password", "email");

        String sessionIdError = "sessioiderror";

        assertFalse(s_accountService.addSession(sessionIdError, notRegisteredUser));
    }


    @Test
    public void testGetUserByName() throws Exception {
        UserProfile user = s_accountService.getUserByName(s_testFirstUser.getLogin());

        assertEquals(user.getLogin(), s_testFirstUser.getLogin());
        assertEquals(user.getPassword(), s_testFirstUser.getPassword());
        assertEquals(user.getEmail(), s_testFirstUser.getEmail());
    }

    @Test
    public void testGetUserBySession() throws Exception {
        UserProfile user = s_accountService.getUserBySession(s_sessionIdSecond);

        assertEquals(user.getLogin(), s_testSecondUser.getLogin());
        assertEquals(user.getPassword(), s_testSecondUser.getPassword());
        assertEquals(user.getEmail(), s_testSecondUser.getEmail());
    }

    @Test
    public void testRemoveUser() throws Exception {
        s_accountService.removeUser(s_sessionIdSecond);

        UserProfile user = s_accountService.getUserBySession(s_sessionIdSecond);

        assertNull(user);
    }

    @Test
    public void testGetAuthUsersNumber() throws Exception {
        assertEquals(s_accountService.getAuthUsersNumber(), 1);
    }

    @Test
    public void testGetRegUsersNumber() throws Exception {
        assertEquals(s_accountService.getRegUsersNumber(), 2);

    }
}*/