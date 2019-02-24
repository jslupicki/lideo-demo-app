package integration.stepdefs.client;

import static com.slupicki.lideo.testTools.RestTool.CHECK_LOGIN;
import static com.slupicki.lideo.testTools.RestTool.CURRENT_CLIENT;
import static com.slupicki.lideo.testTools.RestTool.EMPTY_PARAMS;
import static com.slupicki.lideo.testTools.RestTool.LOGIN;
import static com.slupicki.lideo.testTools.RestTool.REGISTER_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.testTools.RestTool;
import com.slupicki.lideo.testTools.State;
import cucumber.api.java8.En;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class ClientStepdefs implements En {

  private final Logger log = LoggerFactory.getLogger(ClientStepdefs.class);

  @Autowired
  private ClientRepository clientRepository;
  @Autowired
  private RestTool restTool;
  @Autowired
  private State state;

  public ClientStepdefs() {
    Given("client with name {word} surname {word} and login {word} and password {word}", (String name, String surname, String login, String password) -> {
      // Write code here that turns the phrase above into concrete actions
      state.setClient(
          Client.builder()
              .name(name)
              .surname(surname)
              .login(login)
              .password(password)
              .build()
      );
      log.info("Client created: {}", state.getClient());
    });

    When("register new client", () -> {
      state.setClientId(
          restTool.post(REGISTER_CLIENT, Long.class, state.getClient(), EMPTY_PARAMS, EMPTY_PARAMS).orElse(null)
      );
      if (HttpStatus.OK.equals(state.getHttpStatus())) {
        assertThat(state.getClientId()).isNotNull();
      }
      assertThat(state.getHttpStatus()).isIn(HttpStatus.OK, HttpStatus.CONFLICT);
    });

    Then("client appears in DB", () -> {
      Optional<Client> clientById = clientRepository.findById(state.getClientId());
      assertThat(clientById).isPresent();
    });

    Then("got response code {int} and body contains {string}", (Integer code, String body) -> {
      log.info("code: {}, body: {}", code, body);
      assertThat(state.getHttpStatus().value()).isEqualTo(code);
      assertThat(state.getResponseBody()).contains(body);
    });

    When("check login {string}", (String login) -> {
      state.setTestResult(
          restTool.get(CHECK_LOGIN, Boolean.class, EMPTY_PARAMS, ImmutableMap.of("login", login)).orElseThrow(NotFoundException::new)
      );
    });

    Then("result is {bool}", (Boolean expectedResult) -> {
      assertThat(state.getTestResult()).isEqualTo(expectedResult);
    });

    When("get current client", () -> {
      state.setClient(
          restTool.get(CURRENT_CLIENT, Client.class, EMPTY_PARAMS, EMPTY_PARAMS).orElse(null)
      );
    });

    Given("client log in by login {string} and password {string}", (String login, String password) -> {
      HttpHeaders httpHeaders = new HttpHeaders();
      String authorization = "Basic " + Base64.getEncoder().encodeToString((login + ":" + password).getBytes(Charset.defaultCharset()));
      httpHeaders.add("Authorization", authorization);
      restTool.headers = httpHeaders;
      restTool.get(LOGIN, Void.class, EMPTY_PARAMS, EMPTY_PARAMS);
    });

    And("client have name {word}", (String name) -> {
      assertThat(state.getClient().getName()).isEqualTo(name);
    });
  }
}
