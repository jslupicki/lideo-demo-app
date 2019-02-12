package com.slupicki.lideo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Client {
    private @Id
    @GeneratedValue
    Long id;
}