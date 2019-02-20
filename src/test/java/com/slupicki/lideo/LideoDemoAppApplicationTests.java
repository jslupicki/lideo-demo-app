package com.slupicki.lideo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

import com.slupicki.lideo.dao.UserRepository;
import com.slupicki.lideo.misc.TimeProvider;
import com.slupicki.lideo.rest.UserController;
import io.restassured.filter.cookie.CookieFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LideoDemoAppApplicationTests {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserController userController;

  @Autowired
  private TimeProvider timeProvider;

  @Test
  public void contextLoads() {
    System.out.println("********************");
    System.out.println("Time provider: " + timeProvider.getClass().getCanonicalName());
    System.out.println("********************");
  }

  @Test
  public void userControllerShouldReturnAllUsers() {
    CookieFilter cookieFilter = new CookieFilter();

    given()
        .filter(cookieFilter)
        .when()
        .get("/users")
        .then()
        .body("id", hasItems(1))
        .body("login", hasItems("login"));

    given()
        .filter(cookieFilter)
        .when()
        .get("/users");
  }
}

