package integration.stepdefs.client;

import com.slupicki.lideo.model.Client;
import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Value;

public class ClientStepdefs implements En {

    @Value("${server.port}")
    private String serverPort;

    private Client client;

    public ClientStepdefs() {
        Given("client with name {word} surname {word} and login {word} and password {word}", (String name, String surname, String login, String password) -> {
            // Write code here that turns the phrase above into concrete actions
            this.client = Client.builder()
                    .name(name)
                    .surname(surname)
                    .login(login)
                    .password(password)
                    .build();
            System.out.println("Client created: " + this.client);
            System.out.println("Server port:" + this.serverPort);
        });

        When("send client by POST on \\/client\\/", () -> {
            // Write code here that turns the phrase above into concrete actions
            throw new cucumber.api.PendingException();
        });

        Then("client appears in DB", () -> {
            // Write code here that turns the phrase above into concrete actions
            throw new cucumber.api.PendingException();
        });
    }
}
