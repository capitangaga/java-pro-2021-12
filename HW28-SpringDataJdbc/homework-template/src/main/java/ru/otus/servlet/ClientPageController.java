package ru.otus.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

/**
 * @author kirillgolovko
 */
@Controller
public class ClientPageController {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";

    private final DBServiceClient dbServiceClient;

    public ClientPageController(
            DBServiceClient dbServiceClient)
    {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping(path = "/clients")
    protected String doGet(Model model) throws IOException {
        List<Client> clients = dbServiceClient.findAll();
        List<ClientPojo> clientPojos = clients.stream().map(client -> new ClientPojo(
                        client.getId(),
                        client.getName(),
                        client.getAddress().getStreet(),
                        client.getPhones().stream().map(Phone::getNumber).toList()))
                .toList();
        model.addAttribute("clients", clientPojos);
        return "clients";
    }

    @PostMapping(path = "/clients", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected RedirectView doPost(@ModelAttribute ClientForm form) {
        String name = form.getName().orElse("");
        String address = form.getAddress().orElse("");
        List<Phone> phones = Arrays.stream(form.getPhones().orElse("").split(";"))
                .map(String::strip)
                .map(Phone::new)
                .toList();
        Client client = new Client(name);
        client.setAddress(new Address(address));
        client.setPhones(phones);
        dbServiceClient.saveClient(client);
        return new RedirectView("/clients");
    }

}
