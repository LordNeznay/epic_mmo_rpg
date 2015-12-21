package frontend;

import accountService.messages.MessageAuthenticate;
import accountService.messages.MessageIsUserExist;
import accountService.messages.MessageRegisterUser;
import accountService.messages.MessageSignalShutdownAccountService;
import main.UserProfile;
import mechanics.messages.MessageSignalShutdownGameMechanics;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import resource.ServerConfiguration;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 20.12.2015.
 */
public class Frontend implements Abonent, Runnable {
    private Address address = new Address();
    private MessageSystem messageSystem;
    private boolean isWorked = false;
    Server server;

    private Map<String, UserProfile> sessions = new HashMap<>();
    private Map<String, Boolean> responsesAuthorization = new HashMap<>();
    private Map<String, Boolean> responsesExistUser = new HashMap<>();

    public Frontend(MessageSystem messageSystem, int port) throws Exception {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerFrontend(this);

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        Servlet signin = new SignInServlet(this);
        Servlet signUp = new SignUpServlet(this);
        Servlet exitServlet = new ExitServlet(this);
        Servlet adminServlet = new AdminServlet(this);
        Servlet gameServlet = new WebSocketGameServlet(this);
        Servlet topPlayersServlet = new TopPlayersServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(exitServlet), "/api/v1/auth/exit");
        context.addServlet(new ServletHolder(adminServlet), "/api/admin");
        context.addServlet(new ServletHolder(gameServlet), "/gameplay");
        context.addServlet(new ServletHolder(topPlayersServlet), "/api/topplayers");


        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(false);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server = new Server(port);
        server.setHandler(handlers);

        server.start();
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress(){
        return address;
    }



    public void removeUser(String sessionId) { sessions.remove(sessionId); }

    public int getAuthUsersNumber(){
        return sessions.size();
    }

    public void addResponseAuthorization(String session, boolean result){
        responsesAuthorization.put(session, result);
    }

    @TestOnly
    public int sizeResponseAuthorization(){
        return responsesAuthorization.size();
    }

    public boolean isResivedResponseAuthorization(String session){
        return responsesAuthorization.containsKey(session);
    }

    public boolean getResponseAuthorization(String session){
        boolean result = responsesAuthorization.get(session);
        responsesAuthorization.remove(session);
        return result;
    }

    public UserProfile getUserBySession(String sessionId){
        return sessions.get(sessionId);
    }

    public boolean isAuthenticated(String sessionId){
        return sessions.containsKey(sessionId);
    }

    public void authenticated(String sessionId, @Nullable UserProfile userProfile){
        if(userProfile != null) {
            sessions.put(sessionId, userProfile);
        }
    }

    public void authenticate(String login, String password, String sessionId){
        Message messageAuthenticate = new MessageAuthenticate(address, messageSystem.getAddressService().getAccountServiceAddress(), login, password, sessionId);
        messageSystem.sendMessage(messageAuthenticate);
    }

    public void addResponseExistUser(String session, boolean result){
        responsesExistUser.put(session, result);
    }

    public boolean isResivedResponseExistUser(String session){
        return responsesExistUser.containsKey(session);
    }

    @TestOnly
    public int sizeResponseExistUser(){
        return responsesExistUser.size();
    }

    public boolean getResponseExistUser(String session){
        boolean result = responsesExistUser.get(session);
        responsesExistUser.remove(session);
        return result;
    }

    public void isUserExist(String session, String login){
        Message messageIsUserExist = new MessageIsUserExist(address, messageSystem.getAddressService().getAccountServiceAddress(), session, login);
        messageSystem.sendMessage(messageIsUserExist);
    }

    public void registerUser(String login, String password){
        Message messageRegisterUser = new MessageRegisterUser(address, messageSystem.getAddressService().getAccountServiceAddress(), login, password);
        messageSystem.sendMessage(messageRegisterUser);
    }


    public void signalShutdown(){
        Message messageShutdownAccountService = new MessageSignalShutdownAccountService(address, messageSystem.getAddressService().getAccountServiceAddress());
        messageSystem.sendMessage(messageShutdownAccountService);
        Message messageShutdownGameMechanics = new MessageSignalShutdownGameMechanics(address, messageSystem.getAddressService().getGameMechanicsAddress());
        messageSystem.sendMessage(messageShutdownGameMechanics);
        isWorked = false;
//        System.exit(0);
    }

    public void stop(){
        isWorked = false;
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        isWorked = true;
        while (isWorked){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(ServerConfiguration.getInstance().getStepTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
