package com.slupicki.lideo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
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
        return of(name, null, null, null, null);
    }

    public static Client of(String name, String surname) {
        return of(name, surname, null, null, null);
    }

    public static Client of(String name, String surname, String address) {
        return of(name, surname, address, null, null);
    }

    public static Client of(String name, String surname, String address, String login, String password) {
        Client client = new Client();
        client.setName(name);
        client.setSurname(surname);
        client.setAddress(address);
        client.setLogin(login);
        client.setPassword(password);
        return client;
    }
}
