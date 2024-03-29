package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter @Setter
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Hours> openHours = new ArrayList<>();

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

    private boolean runningFlag;

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

    public Hours getHoursForGivenDay(DayOfWeek dayOfWeek){
        Hours result = null;
        for (Hours hours : this.getOpenHours()) {
            if (hours.getDayOfWeek() == dayOfWeek) {
                result = hours;
                break;
            }
        }
        return result;
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
        this.runningFlag = false;
        this.status = !this.status;
    }

    public void runStore(){
        this.runningFlag = true;
        while (runningFlag){
            DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
            LocalTime currentTime = LocalTime.now();

            if(getHoursForGivenDay(currentDayOfWeek).getOpeningTime() == currentTime){
                this.status = true;
            }

            if(getHoursForGivenDay(currentDayOfWeek).getClosingTime() == currentTime){
                this.status = false;
            }
        }
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


    public String printOpenHours(){
        // Sort the list by dayOfWeek
        openHours.sort(Comparator.comparing(Hours::getDayOfWeek));

        // Create a StringBuilder object
        StringBuilder sb = new StringBuilder();

        // Iterate over the sorted list and append the opening hours for each day to the StringBuilder
        for (Hours hours : openHours) {
            sb.append(hours.getDayOfWeek())
                    .append(": ")
                    .append(hours.getOpeningTime())
                    .append(" ~ ")
                    .append(hours.getClosingTime())
                    .append(" (Last Call: ")
                    .append(hours.getLastCallTime())
                    .append(")\n");
        }

        // Return the final string
        System.out.println(sb);
        return sb.toString();
    }
}
