package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter @Setter
public class Store {

    @Id @GeneratedValue
    @Column(name="store_id")
    private Long id;

    @Embedded
    private Address address;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "store")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Rating> ratings = new ArrayList<>();

    //매장 오픈시간과 주문가능 시간 둘다 표시하는게 좋을듯 싶은데..
    private HashMap<DayOfWeek, String> openHours;

    private String storeDescription;

    @Enumerated(EnumType.STRING)
    private List<Tag> tags = new ArrayList<>();

    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Category> categories = new ArrayList<>();

    private boolean status;

//    private event??

//    private String pictureOfStore;
}
