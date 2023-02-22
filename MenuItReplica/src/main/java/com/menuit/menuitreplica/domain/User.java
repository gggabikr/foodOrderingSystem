package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @NotEmpty(message = "User email is mandatory.")
    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String phone;

    private ArrayList<Order> orders;

    private ArrayList<Store> stores;

    private List<Long> favStores;
}
