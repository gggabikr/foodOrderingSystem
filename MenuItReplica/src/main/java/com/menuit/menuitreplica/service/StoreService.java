package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public Long registerStore(String storeName, Address address, String phone, User user) throws Exception {
        if(user.getRole() != UserRole.ROLE_OWNER){
            throw new IllegalArgumentException("The user's account type must be an OWNER.");
        } else{
            Store store = new Store();
            store.setName(storeName);
            store.setAddress(address);
            store.setPhone(phone);
            store.setOwner(user);
            store.setStoreDescription("Please describe your store in your own words.");
            store.setStatus(false);

            return storeRepository.register(store);
        }
    }

    public Store findOne(Long id){
        return storeRepository.findOne(id);
    }

    public List<Store> findAll(){
        return storeRepository.findAll();
    }

    public List<Store> findByPhoneNumber(String phoneNumber){
        return storeRepository.findByPhoneNumber(phoneNumber);
    }

    public List<Store> findByOwner(User owner){
        return storeRepository.findByOwner(owner);
    }

    public List<Store> findByStatus(boolean status){
        return storeRepository.findByStatus(status);
    }

    public List<Store> findByStoreTag(Tag tag){
        String tagName = tag.getName();
        return storeRepository.findByStoreTag(tagName);
    }

    public List<Store> findByStoreTagName(String tagName){
        return storeRepository.findByStoreTag(tagName);
    }

    public List<Store> findByRatingScore(double score){
        return storeRepository.findByRatingScore(score);
    }


}
