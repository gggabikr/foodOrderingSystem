package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
public class Rating {

    @Id @GeneratedValue
    @Column(name="rating_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writtenBy;

    @NotEmpty(message = "rating is mandatory to leave a rating.")
    private float rating; // 1~5 stars

    private String comment;
}
