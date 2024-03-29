package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Long registerItem(Item item) throws IllegalAccessException {
        Store store = item.getStore();
        Category category = item.getCategory();

        List<Item> itemsByStore = findByStore(store);
        for(Item item1 : itemsByStore){
            if(item1.getName().equals(item.getName())){
                throw new IllegalArgumentException("Item with same name exists on the store.");
            }
        }

        if(category.getStore() != store){
            throw new IllegalAccessException("Given category doesn't belong to the store.");
        }

        store.addItem(item, category);
        em.persist(item);
        return item.getId();
    }

    public void removeItem(Store store, Item item) throws IllegalAccessException {
        if(item.getStore() != store){
            throw new IllegalAccessException("This item doesn't belong to the given store.");
        } else{
            store.deleteItem(item);
            item.deleteItem(); //logically delete item by set 'deleted' value to be true;
//            em.remove(item);
        }
    }

    public void reRegisterItem(Store store, Category category,Item item) throws IllegalAccessException {
        if(item.getStore() != store){
            throw new IllegalAccessException("This item doesn't belong to the given store.");
        } else{
            store.addItem(item, category);
            item.reRegisterItem();
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i where i.deleted = false", Item.class)
                .getResultList();
    }

    public List<Item> findAllDeleted(){
        return em.createQuery("select i from Item i where i.deleted = true", Item.class)
                .getResultList();
    }

    public List<Item> findByStore(Store store){
        return em.createQuery("select i from Item i where i.store = :store and i.deleted = false", Item.class)
                .setParameter("store", store)
                .getResultList();
    }

    public List<Item> findByStoreDeleted(Store store){
        return em.createQuery("select i from Item i where i.store = :store and i.deleted = true", Item.class)
                .setParameter("store", store)
                .getResultList();
    }

    public List<Item> findByStoreAndCategory(Store store, Category category){
        return em.createQuery("select i from Item i where i.store = :store and i.category = :category and i.deleted = false", Item.class)
                .setParameter("store", store)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Item> findByStoreAndCategoryDeleted(Store store, Category category){
        return em.createQuery("select i from Item i where i.store = :store and i.category = :category and i.deleted = true", Item.class)
                .setParameter("store", store)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Item> findByItemType(ItemType itemType){
        return em.createQuery("select i from Item i where i.itemType = :type and i.deleted = false", Item.class)
                .setParameter("type", itemType)
                .getResultList();
    }

    public List<Item> findByStoreAndItemType(Store store, ItemType itemType){
        return em.createQuery("select i from Item i where i.store = :store and i.itemType = :type and i.deleted = false", Item.class)
                .setParameter("store", store)
                .setParameter("type", itemType)
                .getResultList();
    }

    public List<Item> findByStoreAndItemTypeDeleted(Store store, ItemType itemType){
        return em.createQuery("select i from Item i where i.store = :store and i.itemType = :type and i.deleted = true", Item.class)
                .setParameter("store", store)
                .setParameter("type", itemType)
                .getResultList();
    }

    public List<Item> findByItemTag(ItemTag itemTag){
        return em.createQuery("select i from Item i where i.itemTag = :tag and i.deleted = false", Item.class)
                .setParameter("tag", itemTag)
                .getResultList();
    }

    public List<Item> findByStoreAndItemTag(Store store, ItemTag itemTag){
        return em.createQuery("select i from Item i where i.itemTag = :tag and i.deleted = false and i.store = :store", Item.class)
                .setParameter("tag", itemTag)
                .setParameter("store", store)
                .getResultList();
    }

    public List<Item> findByStatus(Boolean status){
        return em.createQuery("select i from Item i where i.status = :status and i.deleted = false", Item.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Item> findDiscountedItemOnStore(Store store){
//        return em.createQuery("select i from Item i where i.store = :store and NOT (i.discountAmount is NULL and i.discountPercent is NULL) and i.deleted = false", Item.class)
        return em.createQuery("select i from Item i where i.store = :store and NOT (i.discountAmount =0 and i.discountPercent =0) and i.deleted = false", Item.class)
                .setParameter("store", store)
                .getResultList();
//        List<Item> resultList1 = em.createQuery("SELECT i FROM Item i WHERE i.store = :store AND i.discountAmount IS NOT NULL", Item.class)
//                .setParameter("store", store)
//                .getResultList();
//        List<Item> resultList2 = em.createQuery("SELECT i FROM Item i WHERE i.store = :store AND i.discountPercent IS NOT NULL", Item.class)
//                .setParameter("store", store)
//                .getResultList();
//        resultList1.removeAll(resultList2);
//        resultList1.addAll(resultList2);
//        return resultList1;
    }

    public List<Item> findByNameAndStore(Store store, String name){
        return em.createQuery("SELECT i FROM Item i WHERE UPPER(i.name) LIKE CONCAT('%' ,UPPER(TRIM(:name)), '%') AND i.store = :store AND i.deleted = false",Item.class)
                .setParameter("name", name)
                .setParameter("store", store)
                .getResultList();
    }
}
