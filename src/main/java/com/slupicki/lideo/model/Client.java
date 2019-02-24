package com.slupicki.lideo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {

  @Id
  @GenericGenerator(name = "UseIdOrGenerate", strategy = "com.slupicki.lideo.misc.UseIdOrGenerate")
  @GeneratedValue(generator = "UseIdOrGenerate")
  private Long id;

  private String name;
  private String surname;
  private String address;
  private String login;
  private String password;
  @JsonIgnore
  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private List<Reservation> reservations;


  public static Client of(String name) {
    return Client.builder()
        .name(name)
        .build();
  }

  public static Client of(String name, String surname) {
    return Client.builder()
        .name(name)
        .surname(surname)
        .build();
  }

  public static Client of(String name, String surname, String address) {
    return Client.builder()
        .name(name)
        .surname(surname)
        .address(address)
        .build();
  }

  public static Client of(String name, String surname, String address, String login, String password) {
    return Client.builder()
        .name(name)
        .surname(surname)
        .address(address)
        .login(login)
        .password(password)
        .build();
  }

}
