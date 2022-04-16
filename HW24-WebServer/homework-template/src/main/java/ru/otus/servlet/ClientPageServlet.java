package ru.otus.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.template.TemplateProcessor;

/**
 * @author kirillgolovko
 */
public class ClientPageServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientPageServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Client> clients = dbServiceClient.findAll();
        Map<String, Object> paramsMap = Map.of("clients", clients);

        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> formContent = new HashMap<>();
        Arrays.stream(req.getReader().readLine().split("&"))
                .filter(s -> !s.isBlank())
                .map(param -> param.split("="))
                .filter(arr -> arr.length == 2)
                .forEach(kv -> formContent.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8)));
        String name = formContent.getOrDefault("name", "Unknown");
        String address = formContent.getOrDefault("address", "Unknown");
        List<Phone> phones = Arrays.stream(formContent.getOrDefault("phones", "").split(";"))
                .map(String::strip)
                .map(Phone::new)
                .toList();
        Client client = new Client(name);
        client.setAddress(new Address(address));
        client.setPhones(phones);
        dbServiceClient.saveClient(client);
        resp.sendRedirect("/clients");
    }
}
