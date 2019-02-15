package integration;


import com.slupicki.lideo.model.User;
import cucumber.api.java8.En;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MyStepdefs extends SpringIntegrationTest implements En {

    private List<User> userList;

    public MyStepdefs() {
        Before(() -> userList = null);
        When("^client call get (.*)$", (String path) -> {
            System.out.println("Path:" + path);
            userList = getList(path, new ParameterizedTypeReference<List<User>>() {
            });
        });
        Then("^receiver got list of users$", () -> {
            assertThat(userList).isNotNull();
        });
        And("^list of users have user with id = (\\d+)$", (Integer id) -> {
            List<Long> idList = userList.stream().map(User::getId).collect(Collectors.toList());
            assertThat(idList).contains(id.longValue());
        });
    }
}
