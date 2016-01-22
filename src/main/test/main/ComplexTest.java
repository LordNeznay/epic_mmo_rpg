package main;

import accountservice.AccountService;
import dbservice.DBService;
import dbservice.UserDataSet;
import frontend.Frontend;
import main.TimeHelper;
import mechanics.GameMechanics;
import messagesystem.MessageSystem;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resource.ServerConfiguration;

import static org.junit.Assert.*;
/**
 * Created by Андрей on 17.01.2016.
 */
public class ComplexTest {
    MessageSystem messageSystem;
    Frontend frontend;
    AccountService accountService;
    static final int STEP_TIME = ServerConfiguration.getInstance().getStepTime();
    static final int AMOUNT_STEP = 10;

    @Before
    public void setUp() throws Exception {
        messageSystem = new MessageSystem();

        frontend = new Frontend(messageSystem, ServerConfiguration.getInstance().getPort());

        Configuration configuration = new Configuration()
                .addAnnotatedClass(UserDataSet.class)
                .setProperty("hibernate.dialect", ServerConfiguration.getInstance().getDialect())
                .setProperty("hibernate.connection.driver_class", ServerConfiguration.getInstance().getDriverClass())
                .setProperty("hibernate.connection.url", ServerConfiguration.getInstance().getConnectionUrlTest())
                .setProperty("hibernate.connection.username", ServerConfiguration.getInstance().getConnectionUsername())
                .setProperty("hibernate.connection.password", ServerConfiguration.getInstance().getConnectionPassword())
                .setProperty("hibernate.show_sql", ServerConfiguration.getInstance().getShowSql())
                .setProperty("hibernate.hbm2ddl.auto", ServerConfiguration.getInstance().getHbm2ddAutoTest());

        DBService dbservice = new DBService(configuration);
        accountService = new AccountService(messageSystem, dbservice);

        GameMechanics gameMechanics = new GameMechanics(messageSystem);

        final Thread accountServiceThread = new Thread(accountService);
        accountServiceThread.setDaemon(true);
        accountServiceThread.setName("Account Service");
        final Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setDaemon(true);
        gameMechanicsThread.setName("Game Mechanics");
        final Thread frontEndThread = new Thread(frontend);
        frontEndThread.setDaemon(true);
        frontEndThread.setName("FrontEnd");

        accountServiceThread.start();
        gameMechanicsThread.start();
        frontEndThread.start();
    }

    @After
    public void after(){
        frontend.signalShutdown();
    }

    @Test
    public void failedLoginTest(){
        String sessionId = "sessionId";
        frontend.authenticate("unexisted_login", "password", sessionId);
        boolean isResivedResponse = false;
        for(int i=0; i<AMOUNT_STEP; ++i){
            isResivedResponse = frontend.isResivedResponseAuthorization(sessionId);
            if(isResivedResponse){
                break;
            }
            TimeHelper.sleep(STEP_TIME);
        }
        assertTrue(isResivedResponse);
        assertFalse(frontend.getResponseAuthorization(sessionId));
        assertFalse(frontend.isAuthenticated(sessionId));
    }

    @Test
    public void successRegistrationLoginTest(){
        String login = "login";
        String password = "password";
        frontend.registerUser(login, password);
        //Ждем окончания переходных процессов
        for(int i=0; i<AMOUNT_STEP; ++i){
            TimeHelper.sleep(STEP_TIME);
        }

        String sessionId = "sessionId";
        frontend.authenticate(login, password, sessionId);
        boolean isResivedResponse = false;
        for(int i = 0; i<AMOUNT_STEP; ++i){
            isResivedResponse = frontend.isResivedResponseAuthorization(sessionId);
            if(isResivedResponse){
                break;
            }
            TimeHelper.sleep(STEP_TIME);
        }
        assertTrue(isResivedResponse);
        assertTrue(frontend.getResponseAuthorization(sessionId));
        assertTrue(frontend.isAuthenticated(sessionId));
    }

    @Test
    public void userExistingTrueTest(){
        String login = "login";
        frontend.registerUser(login, "password");
        //Ждем окончания переходных процессов
        for(int i=0; i<AMOUNT_STEP; ++i){
            TimeHelper.sleep(STEP_TIME);
        }

        String sessionId = "sessionId";
        frontend.isUserExist(sessionId, login);
        boolean isResivedResponse = false;
        for(int i = 0; i<AMOUNT_STEP; ++i){
            isResivedResponse = frontend.isResivedResponseExistUser(sessionId);
            if(isResivedResponse){
                break;
            }
            TimeHelper.sleep(STEP_TIME);
        }
        assertTrue(isResivedResponse);
        assertTrue(frontend.getResponseExistUser(sessionId));
    }

    @Test
    public void userExistingFalseTest(){
        String sessionId = "sessionId";
        frontend.isUserExist(sessionId, "login");
        boolean isResivedResponse = false;
        for(int i = 0; i<AMOUNT_STEP; ++i){
            isResivedResponse = frontend.isResivedResponseExistUser(sessionId);
            if(isResivedResponse){
                break;
            }
            TimeHelper.sleep(STEP_TIME);
        }
        assertTrue(isResivedResponse);
        assertFalse(frontend.getResponseExistUser(sessionId));
    }
}
