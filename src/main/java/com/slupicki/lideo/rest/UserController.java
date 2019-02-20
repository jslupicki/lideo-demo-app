package com.slupicki.lideo.rest;

import com.slupicki.lideo.dao.UserRepository;
import com.slupicki.lideo.model.User;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/users")
  List<User> all(HttpSession session) {
    System.out.println("************************");
    System.out.println("Session ID: " + session.getId());
    System.out.println("Number of users last time: " + session.getAttribute("last_time_users_count"));
    List<User> users = userRepository.findAll();
    session.setAttribute("last_time_users_count", users.size());
    System.out.println("Number of users currently: " + session.getAttribute("last_time_users_count"));
    System.out.println("************************");
    return users;
  }
}
