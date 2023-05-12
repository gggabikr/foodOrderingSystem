package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Table(uniqueConstraints=@UniqueConstraint(columnNames={"store_id", "user_id"}))
@Entity
@Getter @Setter
public class Rating {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rating_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writtenBy;

    private int score; // 1~5 stars // not null

    private String comment;

    private boolean status; //true = active, visible  //false = un-active, non-visible, but still in records

    protected Rating() {}

    //==Relational methods==//
    public void setUser(User user){
        this.writtenBy = user;
        user.getRatings().add(this);
    }

    public void setStore(Store store){
        this.store = store;
        store.getRatings().add(this);
        store.updateRatingScore();
    }

    public Rating(User user, Store store, int score){
        this.setUser(user);
        this.score = score;
        this.setStore(store);
        this.status = true;
    }

    public Rating(User user, Store store, int score, String comment){
        this.setUser(user);
        this.score = score;
        this.setStore(store);
        this.status = true;
        this.comment = comment;
    }

    public void toggleStatus(){
        this.status = !this.status;
    }
}
