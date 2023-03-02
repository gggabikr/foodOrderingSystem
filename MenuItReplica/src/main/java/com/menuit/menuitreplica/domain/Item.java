package com.menuit.menuitreplica.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
public class Item {
    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @NotEmpty(message = "Item name is mandatory.")
    private String name;

    private String description;

    private String allergyInfo;

    @NotEmpty(message = "Item price is mandatory.")
    private double price;

    //discount on item, no matter who orders it.
    private int discountPercent;

    private int discountAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotEmpty(message = "Category is mandatory.")
    private Category category;

    private boolean ageRestriction; //1 = exist, 0 = not exist

    private int calories;

    private int minimumOrderCount;

    @NotEmpty(message = "Item status is mandatory.")
    private boolean status; // 1 = available, 0 = Out of order

    @Enumerated(EnumType.STRING)
    private ItemTag itemTag; //Popular, Best, Recommended, NoTag;

    @NotEmpty(message = "item type is mandatory.")
    @Enumerated(EnumType.STRING)
    private ItemType itemType; //food, alcoholic, soda, nonSoda  // default: food

    protected Item() {}

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
        store.addItem(this, category);
    }

    public Item(Store store, Category category, String name, double price, String itemType){
        this.store = store;
        this.name = name;
        this.price = price;
        //default setting below
        this.itemType = ItemType.valueOf(itemType);

        //alcoholic-> true, else -> false
        this.ageRestriction = this.itemType == ItemType.alcoholic;

        this.minimumOrderCount = 1;
        this.itemTag = ItemTag.NoTag;
        this.discountAmount = 0;
        this.discountPercent = 0;
        this.status = true;
        store.addItem(this, category);

    }

    public void deleteItem(Item item){

    }

    //==Business logics==//
    public void toggleStatus(){
        this.status = !this.status;
    }
}
