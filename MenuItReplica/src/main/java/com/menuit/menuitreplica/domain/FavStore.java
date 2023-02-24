package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
public class FavStore {

    @Id @GeneratedValue
    @Column(name="favstore_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "favstore_id")
    private Store favStore;

    protected FavStore(){};

    public FavStore(User user, Store store){
        this.user = user;
        this.favStore = store;
    }
}
