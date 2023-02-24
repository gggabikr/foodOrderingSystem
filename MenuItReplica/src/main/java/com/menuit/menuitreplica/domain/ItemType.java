package com.menuit.menuitreplica.domain;

public enum ItemType {
    food("food"), alcoholic("alcoholic"),
    soda("soda"), nonSoda("nonSoda");

    //food: 5% GST
    //alcoholic: 5% GST + 10% PST
    //soda: 5% GST + 7% PST
    //nonSoda: 5% GST

    //soda includes: Soft drinks, soda pop, Sparkling fruit juices
    //  Carbonated or nitrogenized energy drinks  Kombucha
    //  Nitrogenized coffee (if sweetened)
    //  Sparkling, sweetened water

    //nonSoda includes: De-alcoholized beer, sparkling wine or cider, if the beverages contain 1% or less alcohol by volume
    //Frozen sweetened beverages that are not carbonated and do not have any gases added to them
    //Liquor (note: liquor is subject to 10% PST)
    //Plain bottled water (still and carbonated)
    //Sparkling flavoured waters that contain no sweeteners
    //Still fruit juices (not carbonated)

    private final String text;

    ItemType(final String text){
        this.text = text;
    }
}
