package com.slupicki.lideo;

import com.slupicki.lideo.dao.UserRepository;
import com.slupicki.lideo.rest.UserController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.hasItems;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LideoDemoAppApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(userController);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void userControllerShouldReturnAllUsers() {
        given()
                .when()
                .get("/users")
                .then()
                .body("id", hasItems(1))
                .body("login", hasItems("login"));
    }
}

