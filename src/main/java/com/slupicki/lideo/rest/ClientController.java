package com.slupicki.lideo.rest;

import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.exceptions.UnauthorizedException;
import com.slupicki.lideo.model.Client;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/client")
public class ClientController {

    private final ClientRepository clientRepository;

    private final Pattern BASIC_AUTHORIZATION_PATTERN = Pattern.compile("Basic (.*)");

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/")
    public List<Client> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> client.toBuilder().password("(filtered out)").build())
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    public Long registerNewClient(@RequestBody Client client) {
        clientRepository.save(client);
        return client.getId();
    }

    @GetMapping("/login")
    public void login(HttpServletRequest request, HttpSession session) throws UnauthorizedException {
        String authorization = request.getHeader("authorization");
        Matcher matcher = BASIC_AUTHORIZATION_PATTERN.matcher(authorization);
        if (matcher.matches()) {
            String[] decoded = new String(Base64.getDecoder().decode(matcher.group(1))).split(":");
            String login = decoded[0];
            String password = decoded[1];
            System.out.println("************************");
            System.out.println("Login:" + login);
            System.out.println("Password:" + password);
            System.out.println("************************");
            Client client = clientRepository.findByLoginAndPassword(login, password);
            if (client != null) {
                session.setAttribute("client_id", client.getId());
                return;
            }
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/")
    public Long updateClient(@RequestBody Client client) {
        clientRepository.save(client);
        return client.getId();
    }

    @GetMapping("/template")
    public Client getTemplate() {
        return Client.builder().build();
    }
}
