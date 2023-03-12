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

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

    private double ratingScore = 0;

    @OneToMany(mappedBy = "store")
    private List<Hours> openHours = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Hours> orderAvailableHours = new ArrayList<>();

    private String storeDescription;

//    @Enumerated(EnumType.STRING)
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<StoreTag> tags = new ArrayList<>(); //food types eg. Chinese, Italian..

    @OneToMany(mappedBy = "store")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Category> categories = new ArrayList<>();

    private int gratuity; //if number of customers is above certain number, it will set a tip with certain percentage.

    private int gratuityPercent;

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

//    public void addNewCategory(Category category){
//        this.getCategories().add(category);
//        category.setStore(this);
//    }

    public Category addCategory(String name){
        Category category = new Category();
        category.setStore(this);
        category.setName(name);
        category.setCategoryOrder(getCategories().size());
        this.categories.add(category);
        return category;
    }

    public void deleteCategory(Category category){
        this.categories.remove(category);
    }

    public void addItem(Item item, Category category){
        this.getItems().add(item);
        category.getItems().add(item);
        item.setCategory(category);
    }

    public void deleteItem(Item item){
        this.getItems().remove(item);
        item.getCategory().getItems().remove(item);
    }

    public void addStoreTag(Tag tag){
        StoreTag storeTag = new StoreTag();
        storeTag.setTag(tag);
        storeTag.setStore(this);
        boolean flag = false;
        for(StoreTag storeTag1: this.tags){
            if (storeTag1.getTag() == tag) {
                flag = true;
                break;
            }
        }
        if(!flag){
            this.tags.add(storeTag);
        }
    }

    public void deleteStoreTag(StoreTag storeTag){
        this.tags.remove(storeTag);
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

    public String getAddressString(){
        String address = "";
        if(getAddress().getUnit()!= null){
            address += getAddress().getUnit();
        }
        address += " " + getAddress().getStreet();
        address += " " + getAddress().getCity();
        address += " " + getAddress().getProvince();
        address += " " + getAddress().getZipcode();

        if (address.length() <= 28) {
            // If address is null or has length <= 28, return it as is
            return address;
        }

        // Split address into words
        String[] words = address.split(" ");

        // StringBuilder to construct the output
        StringBuilder sb = new StringBuilder();

        // First word goes on first line
        sb.append(words[0]);
        int lineLength = words[0].length();

        // Add remaining words, wrapping to new line if line length exceeds 28 characters
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            int wordLength = word.length();

            if (lineLength + 1 + wordLength > 28) {
                // If adding this word to the current line would exceed 28 characters,
                // start a new line and reset lineLength
                sb.append("\n         ");
                lineLength = 0;
            } else {
                // Otherwise, add a space and update lineLength
                sb.append(" ");
                lineLength++;
            }

            sb.append(word);
            lineLength += wordLength;
        }

        // Return the constructed string
        return sb.toString();
    }
}
