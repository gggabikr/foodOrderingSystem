package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.StoreRepository;
import com.menuit.menuitreplica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long registerStore(String storeName, Address address, String phone, Long userId) throws Exception {
        User user = userRepository.findOne(userId);
        if(user.getRole() != UserRole.ROLE_OWNER){
            throw new IllegalArgumentException("The user's account type must be an OWNER.");
        } else{
            Store store = new Store();
            store.setName(storeName);
            store.setAddress(address);
            store.setPhone(phone);
            store.setOwner(user);
            store.setGratuity(10); //initial value
            store.setGratuityPercent(15); //initial value
//            store.setOpenHours();                 //set an initial hour later
//            store.setOrderAvailableHours();       //set an initial hour later
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


    //==CATEGORY SERVICE==//
    public Category findOneCategory(Long id){
        return storeRepository.findOneCategory(id);
    }

    public List<Category> findAllCategories(){
        return storeRepository.findAllCategories();
    }

    public List<Category> findCategoriesByStore(Store store){
        return storeRepository.findCategoriesByStore(store);
    }

    public Long addNewCategory(Store store, String name){
        return storeRepository.addNewCategory(store, name);
    }

    public void deleteCategory(Store store, Category category) throws IllegalAccessException {
        storeRepository.deleteCategory(store,category);
    }
}
