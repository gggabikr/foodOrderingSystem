package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class StoreTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

//    public static StoreTag createStoreTag(Tag tag, Store store){
//        StoreTag storeTag = new StoreTag();
//        storeTag.setTag(tag);
//        storeTag.setStore(store);
//
//    }
    //TODO: how do I set the tag for store? repository? service? or just a method?
}
