package com.menuit.menuitreplica.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private String name;

    private String description;

    private String allergyInfo;

    private double price;

    //discount on item, no matter who orders it.
    private int discountPercent;

    private int discountAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private boolean ageRestriction; //1 = exist, 0 = not exist

    private int calories;

    private int minimumOrderCount;

    private boolean status; // 1 = available to order, 0 = Out of order

    @Column(name = "deleted")
    private boolean deleted; // true = registered, false = logically deleted

    @Enumerated(EnumType.STRING)
    private ItemTag itemTag; //Popular, Best, Recommended, NoTag;

    @Enumerated(EnumType.STRING)
    private ItemType itemType; //food, alcoholic, soda, nonSoda  // default: food

    public void setCategory(Category category) {
        this.category = category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
        this.discountAmount = 0;
    }
    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
        this.discountPercent = 0;
    }
    public void setAgeRestriction(boolean ageRestriction) {
        this.ageRestriction = ageRestriction;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public void setMinimumOrderCount(int minimumOrderCount) {
        this.minimumOrderCount = minimumOrderCount;
    }
    public void setItemTags(ItemTag itemTags) {
        this.itemTag = itemTags;
    }
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
    public void changeItemPrice(double price){
        this.price = price;
    }



    //==Constructor==//
    public Item(Store store, Category category, String name, double price){
        this.store = store;
        this.name = name;
        this.price = price;
        //default setting below
        this.itemType = ItemType.food;
        this.ageRestriction = false;
        this.minimumOrderCount = 1;
        this.itemTag = ItemTag.NoTag;
        this.discountAmount = 0;
        this.discountPercent = 0;
        this.status = true;
        this.deleted = false;
        store.addItem(this, category);
    }

    public Item(Store store, Category category, String name, double price, String itemType){
        this.store = store;
        this.name = name;
        this.price = price;
        //default setting below
        this.itemType = ItemType.valueOf(itemType);
        this.deleted = false;

        //alcoholic-> true, else -> false
        this.ageRestriction = this.itemType == ItemType.alcoholic;

        this.minimumOrderCount = 1;
        this.itemTag = ItemTag.NoTag;
        this.discountAmount = 0;
        this.discountPercent = 0;
        this.status = true;
        store.addItem(this, category);

    }

    //==Business logics==//
    public void toggleStatus(){
        this.status = !this.status;
    }

    public void deleteItem(){
        this.deleted = true;
    }
}
