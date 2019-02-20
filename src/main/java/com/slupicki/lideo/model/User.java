package com.slupicki.lideo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data()
@Entity
public class User {

  @Id
  @GeneratedValue
  private Long id;
  private String login;
  private String password;

  public User() {
  }

  public User(String login, String password) {
    this.login = login;
    this.password = password;
  }
}
