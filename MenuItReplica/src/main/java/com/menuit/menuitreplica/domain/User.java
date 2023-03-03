package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")// table name 'user' will make an error
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

    private String fullName; //for the table, table name or table number

    private String phone;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    //only Owner account & table account will have it.
    @OneToMany(mappedBy = "owner")
    private List<Store> stores = new ArrayList<>();

    @OneToMany(mappedBy = "writtenBy")
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany
    private List<Store> favStore;

    public void addStore(Store store){
        this.stores.add(store);
    }
    //==Relational methods==//
}
