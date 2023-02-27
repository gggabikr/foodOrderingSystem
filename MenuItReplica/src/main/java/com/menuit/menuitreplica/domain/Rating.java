package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Table(uniqueConstraints=@UniqueConstraint(columnNames={"store_id", "user_id"}))
@Entity
@Getter @Setter
public class Rating {

    @Id @GeneratedValue
    @Column(name="rating_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writtenBy;

    @NotEmpty(message = "rating is mandatory to leave a rating.")
    private double rating; // 1~5 stars

    private String comment;

    protected Rating() {}

    //==Relational methods==//
    public void setUser(User user){
        this.writtenBy = user;
        user.getRatings().add(this);
    }

    public void setStore(Store store){
        this.store = store;
        store.getRatings().add(this);
    }

    public Rating(User user, Store store, double rating, @Nullable String comment){
        this.writtenBy = user;
        this.store = store;
        this.rating = rating;
        this.comment = comment;
    }
}
