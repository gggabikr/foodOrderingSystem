package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.controller.ItemDTO;
import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.ItemRepository;
import com.menuit.menuitreplica.repository.OrderRepository;
import com.menuit.menuitreplica.repository.StoreRepository;
import com.menuit.menuitreplica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public Long registerItem(ItemDTO itemDTO) throws IllegalAccessException {
        Store store = storeRepository.findOne(itemDTO.getStoreId());
        Category category = storeRepository.findOneCategory(itemDTO.getCategoryId());
        Item item = new Item(store,category,itemDTO.getName(), itemDTO.getPrice(),itemDTO.getItemType());
        return itemRepository.registerItem(store,category,item);
    }


    public void removeItem(Long storeId, Long itemId) throws IllegalAccessException {
        //it wouldn't actually delete the item cuz it will mess up all the order records ordered the item.
        Store store = storeRepository.findOne(storeId);
        Item item = itemRepository.findOne(itemId);

        itemRepository.removeItem(store,item);
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public List<Item> findByStore(Long storeId){
        Store store = storeRepository.findOne(storeId);
        return itemRepository.findByStore(store);
    }

    public List<Item> findByStoreDeleted(Long storeId) {
        Store store = storeRepository.findOne(storeId);
        return itemRepository.findByStoreDeleted(store);
    }

    public List<Item> findByStoreAndCategory(Long storeId, Long categoryId){
        //findByCategory ??? ?????? ????????? ?????? ???????????? ???????????? ????????????.
        Store store = storeRepository.findOne(storeId);
        Category category = storeRepository.findOneCategory(categoryId);
        return itemRepository.findByStoreAndCategory(store, category);
    }

    public List<Item> findByStoreAndCategoryDeleted(Long storeId, Long categoryId) {
        Store store = storeRepository.findOne(storeId);
        Category category = storeRepository.findOneCategory(categoryId);
        return itemRepository.findByStoreAndCategoryDeleted(store,category);
    }

    public List<Item> findByItemType(String str){
        ItemType itemType = ItemType.valueOf(str);
        return itemRepository.findByItemType(itemType);
    }

    public List<Item> findByStoreAndItemType(Long storeId, String strType){
        Store store = storeRepository.findOne(storeId);
        ItemType itemType = ItemType.valueOf(strType);

        return itemRepository.findByStoreAndItemType(store,itemType);
    }

    public List<Item> findByStoreAndItemTypeDeleted(Long storeId, String strType){
        Store store = storeRepository.findOne(storeId);
        ItemType itemType = ItemType.valueOf(strType);
        return itemRepository.findByStoreAndItemTypeDeleted(store, itemType);
    }

    public List<Item> findByItemTag(String str){
        ItemTag itemTag = ItemTag.valueOf(str);
        return itemRepository.findByItemTag(itemTag);
    }

    public List<Item> findByStatus(Boolean status){
        return itemRepository.findByStatus(status);
    }

    public List<Item> findDiscountedItemOnStore(Long storeId){
        Store store = storeRepository.findOne(storeId);
        return itemRepository.findDiscountedItemOnStore(store);
    }
}
