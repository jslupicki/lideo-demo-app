package com.slupicki.lideo.dao;

import com.slupicki.lideo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByLoginAndPassword(String login, String password);
}
