package com.menuit.menuitreplica.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tag_id")
    private Long id;

    private String name;

    public void setName(String name){
        this.name = name;
    }
}
