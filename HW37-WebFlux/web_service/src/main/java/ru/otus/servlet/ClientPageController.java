package ru.otus.servlet;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.otus.servlet.dbclient.ClientsDbReactiveClient;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

/**
 * @author kirillgolovko
 */
@Controller
public class ClientPageController {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";

    private final ClientsDbReactiveClient dbClient;

    public ClientPageController(ClientsDbReactiveClient client) {
        this.dbClient = client;
    }

    @GetMapping(path = "/clients")
    protected Mono<Rendering> doGet() {
       return dbClient.findAll()
                .map(clients -> clients.getClients().stream().map(client -> new ClientPojo(
                        client.getId(),
                        client.getName(),
                        client.getAddress().getStreet(),
                        client.getPhones().stream().map(Phone::getNumber).toList()))
                .toList())
                .map(clientPojos ->
                        Rendering.view("clients").modelAttribute("clients", clientPojos).build());
    }

    @PostMapping(path = "/clients", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected Mono<String> doPost(@ModelAttribute ClientForm form) {
        String name = form.getName().orElse("");
        String address = form.getAddress().orElse("");
        List<Phone> phones = Arrays.stream(form.getPhones().orElse("").split(";"))
                .map(String::strip)
                .map(Phone::new)
                .toList();
        Client client = new Client(name);
        client.setAddress(new Address(address));
        client.setPhones(phones);
        return dbClient.save(Mono.just(client)).map(result -> "redirect:/clients");
   }

}
