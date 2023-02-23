package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
public class Item {
    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @NotEmpty(message = "Item name is mandatory.")
    private String name;

    private String description;

    private String allergyInfo;

    @NotEmpty(message = "Item price is mandatory.")
    private float price;

    private int discountPercent;

    private int discountAmount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotEmpty(message = "Category is mandatory.")
    private Category category;

    private boolean ageRestriction; //1 = exist, 0 = not exist

    private int calories;

    private int minimumOrderCount;

    @NotEmpty(message = "Item status is mandatory.")
    private boolean status; // 1 = available, 0 = Out of order

    @Enumerated(EnumType.STRING)
    private ItemTag itemTags; //Popular, Best, Recommended, NoTag;

    @NotEmpty(message = "Tax info is mandatory.")
    @Enumerated(EnumType.STRING)
    private TaxInfo taxInfo; //NoTax, GST, PST, BOTH
}
