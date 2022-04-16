package ru.otus;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientConfiguration;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.server.ClientsWebServer;
import ru.otus.template.TemplateProcessor;
import ru.otus.template.TemplateProcessorImpl;

/**
 * @author kirillgolovko
 */
public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        DBServiceClient dbClient = DbServiceClientConfiguration.getClient();
        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService
                = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        ClientsWebServer clientsWebServer
                = new ClientsWebServer(WEB_SERVER_PORT, templateProcessor, dbClient, loginService);
        clientsWebServer.start();
        clientsWebServer.join();
    }
}
