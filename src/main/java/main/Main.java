package main;

import frontend.ExitServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import frontend.AdminServlet;
import frontend.WebSocketGameServlet;
import mechanics.GameMechanics;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {
    public static final int PORT = 8084;

    public static void main(String[] args) throws Exception {
        int port = PORT;
        if (args != null) {
            if (args.length == 1) {
                String portString = args[0];
                port = Integer.valueOf(portString);
            }
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        GameMechanics gameMechanics = new GameMechanics();

        AccountService accountService = new AccountService();
        accountService.addUser("admin", new UserProfile("admin", "admin", ""));
        accountService.addUser("LordNeznay", new UserProfile("LordNeznay", "LordNeznay", ""));

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet exitServlet = new ExitServlet(accountService);
        Servlet adminServlet = new AdminServlet(accountService);
        Servlet gameServlet = new WebSocketGameServlet(accountService, gameMechanics);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(exitServlet), "/api/v1/auth/exit");
        context.addServlet(new ServletHolder(adminServlet), "/api/admin");

        context.addServlet(new ServletHolder(gameServlet), "/gameplay");


        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}