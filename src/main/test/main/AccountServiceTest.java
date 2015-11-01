package main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by uschsh on 01.11.15.
 */
public class AccountServiceTest {

    private final AccountService accountService = new AccountService();

    private final UserProfile testFirstUser = new UserProfile("testLogin1", "testPassword1", "testEmail1");
    private final UserProfile testSecondUser = new UserProfile("testLogin2", "testPassword2", "testEmail2");

    private final String sessionIdSecond = "sessioidsecond";

    @Before
    public void setUp() throws Exception {
        accountService.addUser(testFirstUser.getLogin(), testFirstUser);
        accountService.addUser(testSecondUser.getLogin(), testSecondUser);

        accountService.addSession(sessionIdSecond, testSecondUser);
    }

    @Test
    public void testAddUser() throws Exception {
        UserProfile newUser = new UserProfile("newUser", "password", "email1");

        accountService.addUser(newUser.getLogin(), newUser);

        UserProfile user = accountService.getUserByName(newUser.getLogin());

        assertNotNull(user);

        assertEquals(newUser.getLogin(), user.getLogin());
        assertEquals(newUser.getEmail(), user.getEmail());
        assertEquals(newUser.getPassword(), user.getPassword());
    }

    @Test
    public void testAddExistingUser() throws Exception {
        assertFalse(accountService.addUser(testFirstUser.getLogin(), testFirstUser));
    }

    @Test
    public void testAuthorization() throws Exception {
        String sessionIdFirst = "sessionidfirst";

        assertTrue(accountService.addSession(sessionIdFirst, testFirstUser));
    }

    @Test
    public void testNoAuthorization() throws Exception {
        UserProfile notRegisteredUser = new UserProfile("login", "password", "email");

        String sessionIdError = "sessioiderror";

        assertFalse(accountService.addSession(sessionIdError, notRegisteredUser));
    }


    @Test
    public void testGetUserByName() throws Exception {
        UserProfile user = accountService.getUserByName(testFirstUser.getLogin());

        assertEquals(user, testFirstUser);
    }

    @Test
    public void testGetUserBySession() throws Exception {
        UserProfile user = accountService.getUserBySession(sessionIdSecond);

        assertEquals(user, testSecondUser);
    }

    @Test
    public void testRemoveUser() throws Exception {
        accountService.removeUser(sessionIdSecond);

        UserProfile user = accountService.getUserBySession(sessionIdSecond);

        assertNull(user);
    }

    @Test
    public void testGetAuthUsersNumber() throws Exception {
        assertEquals(accountService.getAuthUsersNumber(), 1);
    }

    @Test
    public void testGetRegUsersNumber() throws Exception {
        assertEquals(accountService.getRegUsersNumber(), 2);

    }
}