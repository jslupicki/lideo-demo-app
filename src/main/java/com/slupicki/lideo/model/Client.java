package com.slupicki.lideo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String surname;
    private String address;
    private String login;
    private String password;
    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST)
    private List<Payment> payments;
    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST)
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
