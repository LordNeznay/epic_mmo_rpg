package main;

import accountService.AccountService;
import dbservice.DBService;
import frontend.*;
import mechanics.GameMechanics;
import messageSystem.MessageSystem;
import resource.ServerConfiguration;
import utils.Repairer;

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

        DBService dbservice = new DBService();
        MessageSystem messageSystem = new MessageSystem();

        //GameMechanics gameMechanics = new GameMechanics(messageSystem);
        //Repairer.getInstance().setGameMechanics(gameMechanics);

        AccountService accountService = new AccountService(messageSystem, dbservice);
        accountService.addUser("admin", new UserProfile("admin", "admin", ""));
        accountService.addUser("LordNeznay", new UserProfile("LordNeznay", "LordNeznay", ""));

        Frontend frontend = new Frontend(messageSystem, port);



        final Thread accountServiceThread = new Thread(accountService);
        accountServiceThread.setDaemon(true);
        accountServiceThread.setName("Account Service");
        //final Thread gameMechanicsThread = new Thread(gameMechanics);
        //gameMechanicsThread.setDaemon(true);
        //gameMechanicsThread.setName("Game Mechanics");
        final Thread frontEndThread = new Thread(frontend);
        frontEndThread.setDaemon(true);
        frontEndThread.setName("FrontEnd");

        accountServiceThread.start();
        //gameMechanicsThread.start();
        frontEndThread.start();



        //gameMechanics.start();
    }
}