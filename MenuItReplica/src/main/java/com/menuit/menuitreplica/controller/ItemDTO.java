package com.menuit.menuitreplica.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemDTO {
    private String name;
    private Long storeId;
    private Long categoryId;
    private double price;
    private String itemType;

}
