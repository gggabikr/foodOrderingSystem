package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Store {

    @Id @GeneratedValue
    @Column(name="store_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "store")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Rating> ratings = new ArrayList<>();

    private double ratingScore = 0;

    @OneToMany(mappedBy = "store")
    private List<Hours> openHours = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Hours> orderAvailableHours = new ArrayList<>();

    private String storeDescription;

//    @Enumerated(EnumType.STRING)
    @OneToMany(mappedBy = "store")
    private List<StoreTag> tags = new ArrayList<>(); //food types eg. Chinese, Italian..

    @OneToMany(mappedBy = "store")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Category> categories = new ArrayList<>();

    private int gratuity; //if number of customers is above certain number, it will set a tip with certain percentage.

    private boolean status;

//    private event??

//    private String pictureOfStore;

    //==Relational methods==//
    public void setOwner(User owner) {
        this.owner = owner;
        owner.addStore(this);
    }

    public void setOpenHours(Hours hour){
        this.getOpenHours().removeIf(hours -> hours.getDayOfWeek() == hour.getDayOfWeek());
        this.openHours.add(hour);
        hour.setStore(this);
    }

    public void setAvailableHours(Hours hour){
        this.getOrderAvailableHours().removeIf(hours -> hours.getDayOfWeek() == hour.getDayOfWeek());
        this.orderAvailableHours.add(hour);
        hour.setStore(this);
    }

    public void addCategory(Category category){
        this.getCategories().add(category);
        category.setStore(this);
    }

    public void setStoreTag(StoreTag storeTag){
        this.tags.add(storeTag);
        storeTag.setStore(this);
    }


    public void updateRatingScore(){
        double sumOfRatings = 0;
        for(Rating rating: ratings){
            sumOfRatings += rating.getScore();
        }
        this.setRatingScore(Math.round(sumOfRatings/getRatings().size()*100)/100.0);
    }

    public void toggleStatus(){
        this.status = !this.status;
    }
}
