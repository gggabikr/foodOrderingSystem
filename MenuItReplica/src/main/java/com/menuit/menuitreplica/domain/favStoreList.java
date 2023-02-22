package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter @Setter
public class favStoreList {
    private List<Store> favStores = new ArrayList<>();
}
