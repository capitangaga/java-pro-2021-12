package ru.otus.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.servlet.ClientPageServlet;
import ru.otus.template.TemplateProcessor;

/**
 * @author kirillgolovko
 */
public class ClientsWebServer implements WebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String ROLE_NAME_USER = "admin";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String CONSTRAINT_NAME = "auth";

    private final Server server;
    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;
    private final LoginService loginService;

    public ClientsWebServer(
            int port,
            TemplateProcessor templateProcessor,
            DBServiceClient dbServiceClient,
            LoginService loginService)
    {
        this.server = new Server(port);
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.loginService = loginService;
    }


    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/clients"));


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        Constraint constraint = new Constraint();
        constraint.setName(CONSTRAINT_NAME);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{ROLE_NAME_USER, ROLE_NAME_ADMIN});

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        Arrays.stream(paths).forEachOrdered(path -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(path);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(servletContextHandler));

        return security;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(
                new ServletHolder(new ClientPageServlet(dbServiceClient, templateProcessor)), "/clients");
        return servletContextHandler;
    }
}
