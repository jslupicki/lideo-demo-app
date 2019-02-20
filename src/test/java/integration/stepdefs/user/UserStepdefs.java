package integration.stepdefs.user;


import static com.slupicki.lideo.testTools.RestTool.BASE_TEMPLATE;
import static com.slupicki.lideo.testTools.RestTool.EMPTY_PARAMS;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.slupicki.lideo.misc.TimeProvider;
import com.slupicki.lideo.model.User;
import com.slupicki.lideo.testTools.RestTool;
import cucumber.api.java8.En;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

public class UserStepdefs implements En {

  private List<User> userList;

  @Autowired
  private TimeProvider timeProvider;

  @Autowired
  private RestTool restTool;

  public UserStepdefs() {
    Before(() -> userList = null);
    When("client call get {word}", (String path) -> {
      System.out.println("**********************");
      System.out.println("Path:" + path);
      System.out.println("Time provider:" + timeProvider.getClass().getCanonicalName());
      System.out.println("**********************");
      userList = restTool.get(BASE_TEMPLATE, new TypeReference<List<User>>() {
      }, EMPTY_PARAMS, ImmutableMap.of("path", path)).orElse(null);
    });
    Then("receiver got list of users", () -> {
      assertThat(userList).isNotNull();
    });
    And("list of users have user with id = {int}", (Integer id) -> {
      List<Long> idList = userList.stream().map(User::getId).collect(Collectors.toList());
      assertThat(idList).contains(id.longValue());
    });
  }
}
