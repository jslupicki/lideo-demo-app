package integration.stepdefs.client;

import com.google.common.collect.ImmutableMap;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.testTools.RestTool;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.slupicki.lideo.testTools.RestTool.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ClientStepdefs implements En {

    private final Logger log = LoggerFactory.getLogger(ClientStepdefs.class);

    private Client client;
    private Long clientId;
    private Boolean lastResult;

    public ClientStepdefs(
            RestTool restTool,
            ClientRepository clientRepository
    ) {
        Given("client with name {word} surname {word} and login {word} and password {word}", (String name, String surname, String login, String password) -> {
            // Write code here that turns the phrase above into concrete actions
            this.client = Client.builder()
                    .name(name)
                    .surname(surname)
                    .login(login)
                    .password(password)
                    .build();
            log.info("Client created: {}", this.client);
        });

        When("send client by POST on {word}", (String path) -> {
            this.clientId = restTool.post(BASE_TEMPLATE, Long.class, client, EMPTY_PARAMS, ImmutableMap.of("path", path)).orElse(null);
            if (HttpStatus.OK.equals(restTool.statusCode)) {
                assertThat(clientId).isNotNull();
            }
            assertThat(restTool.statusCode).isIn(HttpStatus.OK, HttpStatus.CONFLICT);
        });

        Then("client appears in DB", () -> {
            Optional<Client> clientById = clientRepository.findById(clientId);
            assertThat(clientById).isPresent();
        });

        Then("got response code {int} and body contains {string}", (Integer code, String body) -> {
            log.info("code: {}, body: {}", code, body);
            assertThat(restTool.statusCode.value()).isEqualTo(code);
            assertThat(restTool.responseBody).contains(body);
        });

        When("check login {string}", (String login) -> {
            lastResult = restTool.get(CHECK_LOGIN, Boolean.class, EMPTY_PARAMS, ImmutableMap.of("login", login)).orElseThrow(NotFoundException::new);
        });

        Then("result is {bool}", (Boolean expectedResult) -> {
            assertThat(lastResult).isEqualTo(expectedResult);
        });
    }
}
