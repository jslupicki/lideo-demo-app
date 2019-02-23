package com.slupicki.lideo.rest;

import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.exceptions.AlreadyExistException;
import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.exceptions.NotLoggedInException;
import com.slupicki.lideo.exceptions.UnauthorizedException;
import com.slupicki.lideo.model.Client;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/client")
public class ClientController {

  public static final String CLIENT_ID = "client_id";
  private static final Pattern BASIC_AUTHORIZATION_PATTERN = Pattern.compile("Basic (.*)");

  private final ClientRepository clientRepository;

  public ClientController(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @GetMapping("/")
  public List<Client> getAllClients() {
    return clientRepository.findAll().stream()
        .map(client -> client.toBuilder().password("(filtered out)").build())
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public Client getClient(@PathVariable("id") Long id) throws NotFoundException {
    return clientRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  @PostMapping("/")
  public Long registerNewClient(@RequestBody Client client) throws AlreadyExistException {
    checkIfClientAlreadyExist(client);
    clientRepository.save(client);
    return client.getId();
  }

  @GetMapping("/login")
  public void login(HttpServletRequest request, HttpSession session) throws UnauthorizedException {
    String authorization = request.getHeader("Authorization");
    Matcher matcher = BASIC_AUTHORIZATION_PATTERN.matcher(authorization);
    if (matcher.matches()) {
      String[] decoded = new String(Base64.getDecoder().decode(matcher.group(1))).split(":");
      String login = decoded[0];
      String password = decoded[1];
      Client client = clientRepository.findByLoginAndPassword(login, password);
      if (client != null) {
        session.setAttribute(CLIENT_ID, client.getId());
        return;
      }
    }
    throw new UnauthorizedException("Unauthorized");
  }

  @GetMapping("/logged")
  public Client getLogged(HttpSession session) throws NotLoggedInException, NotFoundException {
    Long clientId = (Long) session.getAttribute(CLIENT_ID);
    if (clientId == null) {
      throw new NotLoggedInException("Client not logged in");
    }
    return clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
  }

  @PutMapping("/")
  public Long updateClient(@RequestBody Client client) throws AlreadyExistException {
    checkIfClientAlreadyExist(client);
    clientRepository.save(client);
    return client.getId();
  }

  @GetMapping("/template")
  public Client getTemplate() {
    return Client.builder().build();
  }

  @GetMapping("/login/{login}")
  public Boolean isLoginAlreadyExist(@PathVariable("login") String login) {
    return clientRepository.countByLogin(login) > 0;
  }

  private void checkIfClientAlreadyExist(@RequestBody Client client) throws AlreadyExistException {
    if (client.getLogin() == null) {
      return;
    }
    if (clientRepository.countByLogin(client.getLogin()) > 0) {
      throw new AlreadyExistException("Login '" + client.getLogin() + "' already exist");
    }
  }

}
