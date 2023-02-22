package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
public class Rating {

    @Id @GeneratedValue
    @Column(name="rating_id")
    private Long id;
    private Store store;
    private User writtenBy;

    @NotEmpty(message = "rating is mandatory to leave a rating.")
    private float rating;
    private String comment;
}
