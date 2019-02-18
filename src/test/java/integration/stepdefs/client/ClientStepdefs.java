package integration.stepdefs.client;

import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.testTools.RestTool;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientStepdefs implements En {

    private final Logger log = LoggerFactory.getLogger(ClientStepdefs.class);

    private Client client;
    private Long clientId;

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
            this.clientId = restTool.post(path, Long.class, client).orElse(null);
            assertThat(clientId).isNotNull();
        });

        Then("client appears in DB", () -> {
            Optional<Client> clientById = clientRepository.findById(clientId);
            assertThat(clientById).isPresent();
        });
    }
}
