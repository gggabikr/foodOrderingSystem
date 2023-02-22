package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

@Entity
@Getter @Setter
public class Store {

    @Id @GeneratedValue
    @Column(name="store_id")
    private Long id;

    @Embedded
    private Address address;

    private String phone;

    private ArrayList<User> staffList;

    private ArrayList<Rating> ratings;

    //매장 오픈시간과 주문가능 시간 둘다 표시하는게 좋을듯 싶은데..
    private HashMap<DayOfWeek, String> openHours;

    private String storeDescription;

    @Enumerated(EnumType.STRING)
    private ArrayList<Tag> tags;

    private ArrayList<Item> items;

    private ArrayList<Category> categories;

    private boolean status;

//    private event??

//    private String pictureOfStore;
}
