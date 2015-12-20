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


    @BeforeClass
    public static void setUp() throws Exception {
        dbService   =   new DBService("test");
        s_accountService = new AccountService(dbService);
        s_accountService.addUser(s_testFirstUser.getLogin(), s_testFirstUser);
        s_accountService.addUser(s_testSecondUser.getLogin(), s_testSecondUser);

    }

    @AfterClass
    public static void after() throws IOException {
        s_accountService.shutdown();
    }

    @Test
    public void testAddUser() throws Exception {
        UserProfile newUser = new UserProfile("newUser", "password", "email1");

        s_accountService.registerUser(newUser.getLogin(), newUser.getPassword());

        assertTrue(s_accountService.isExistUser((newUser.getLogin())));

        s_accountService.deleteUserByName(newUser.getLogin());
    }

    @Test
    public void testAuthen() throws Exception {

        UserProfile authUser = s_accountService.authenticate(s_testFirstUser.getLogin(), s_testFirstUser.getPassword());

        assertEquals(authUser.getLogin(), s_testFirstUser.getLogin());
        assertEquals(authUser.getPassword(), s_testFirstUser.getPassword());
        assertEquals(authUser.getEmail(), s_testFirstUser.getEmail());
    }

    @Test
    public void testAddExistingUser() throws Exception {
        assertFalse(s_accountService.addUser(s_testFirstUser.getLogin(), s_testFirstUser));
    }


    @Test
    public void testGetUserByName() throws Exception {
        UserProfile user = s_accountService.getUserByName(s_testFirstUser.getLogin());

        assertEquals(user.getLogin(), s_testFirstUser.getLogin());
        assertEquals(user.getPassword(), s_testFirstUser.getPassword());
        assertEquals(user.getEmail(), s_testFirstUser.getEmail());
    }



    @Test
    public void testGetRegUsersNumber() throws Exception {
        assertEquals(s_accountService.getRegUsersNumber(), 2);

    }
}*/