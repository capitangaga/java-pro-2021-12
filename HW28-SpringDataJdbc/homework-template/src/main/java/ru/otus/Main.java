package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kirillgolovko
 */
@SpringBootApplication
public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
//        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
//        DBServiceClient dbClient = DbServiceClientConfiguration.getClient();
//        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
//        LoginService loginService
//                = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
//        ClientsWebServer clientsWebServer
//                = new ClientsWebServer(WEB_SERVER_PORT, templateProcessor, dbClient, loginService);
//        clientsWebServer.start();
//        clientsWebServer.join();
    }
}
