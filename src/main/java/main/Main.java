package main;

import accountService.AccountService;
import dbservice.DBService;
import dbservice.UserDataSet;
import frontend.*;
import mechanics.GameMechanics;
import messageSystem.MessageSystem;
import org.hibernate.cfg.Configuration;
import resource.ServerConfiguration;

/**
 * @author v.chibrikov
 */
public class Main {
    @SuppressWarnings({"OverlyBroadThrowsClause", "SpellCheckingInspection"})
    public static void main(String[] args) throws Exception {

        int port = ServerConfiguration.getInstance().getPort();
        if (args != null) {
            if (args.length == 1) {
                String portString = args[0];
                port = Integer.valueOf(portString);
            }
        }

        Configuration configuration = new Configuration()
                .addAnnotatedClass(UserDataSet.class)
                .setProperty("hibernate.dialect", ServerConfiguration.getInstance().getDialect())
                .setProperty("hibernate.connection.driver_class", ServerConfiguration.getInstance().getDriverClass())
                .setProperty("hibernate.connection.url", ServerConfiguration.getInstance().getConnectionUrl())
                .setProperty("hibernate.connection.username", ServerConfiguration.getInstance().getConnectionUsername())
                .setProperty("hibernate.connection.password", ServerConfiguration.getInstance().getConnectionPassword())
                .setProperty("hibernate.show_sql", ServerConfiguration.getInstance().getShowSql())
                .setProperty("hibernate.hbm2ddl.auto", ServerConfiguration.getInstance().getHbm2ddAuto());

        DBService dbservice = new DBService(configuration);
        MessageSystem messageSystem = new MessageSystem();

        GameMechanics gameMechanics = new GameMechanics(messageSystem);
        //Repairer.getInstance().setGameMechanics(gameMechanics);

        AccountService accountService = new AccountService(messageSystem, dbservice);
        accountService.addUser("admin", new UserProfile("admin", "admin", ""));
        accountService.addUser("LordNeznay", new UserProfile("LordNeznay", "LordNeznay", ""));

        Frontend frontend = new Frontend(messageSystem, port);



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
}