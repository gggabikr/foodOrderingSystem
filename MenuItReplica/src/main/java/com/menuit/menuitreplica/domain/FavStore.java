package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
public class FavStore {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="favstore_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favstore_id")
    private Store favStore;

    protected FavStore(){}

    public FavStore(User user, Store store){
        this.user = user;
        this.favStore = store;
    }
}
