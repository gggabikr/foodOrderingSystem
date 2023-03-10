package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepository {

    private final EntityManager em;

//    public Long register(User owner, Store store) throws Exception {
//        if(!owner.getRole().equals(UserRole.ROLE_OWNER)){
//            throw new IllegalArgumentException("The user's role must be owner to register new store.");
//        } else{
//            if(this.findOne(store.getId()) != null){
//                em.merge(store);
//            } else{
//                em.persist(store);
//            }
//            return store.getId();
//        }
//    }
    public Long register(Store store) throws Exception {
        if(store.getId() != null){
            em.merge(store);
        } else{
            em.persist(store);
        }
        return store.getId();
    }

    public Store findOne(Long id){
        return em.find(Store.class, id);
    }

    public List<Store> findAll(){
        return em.createQuery("select s from Store s", Store.class)
                .getResultList();
    }

    public List<Store> findByPhoneNumber(String phoneNumber){
        return em.createQuery("select s from Store s where s.phone = :phoneNumber", Store.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    public List<Store> findByOwner(User owner){
        if(!owner.getRole().equals(UserRole.ROLE_OWNER)){
            return new ArrayList<>();
        } else{
            return em.createQuery("select s from Store s where s.owner = :owner", Store.class)
                    .setParameter("owner", owner)
                    .getResultList();
        }
    }

    public List<Store> findByStatus(boolean status){
        return em.createQuery("select s from Store s where s.status = :status", Store.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Store> findByTag(Tag tag){
        return em.createQuery("select distinct s from Store s join s.tags st join st.tag t where t = :tag", Store.class)
                .setParameter("tag", tag)
                .getResultList();
    }

    public List<Store> findByRatingScore(double score){
        return em.createQuery("select s from Store s where s.ratingScore >= :score", Store.class)
                .setParameter("score", score)
                .getResultList();
    }

    //==CATEGORY REPOSITORY==//

    public Category findOneCategory(Long id){
        return em.find(Category.class,id);
    }

    public List<Category> findAllCategories(){
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    public List<Category> findCategoriesByStore(Store store){
        return em.createQuery("select c from Category c where c.store = :store", Category.class)
                .setParameter("store", store)
                .getResultList();
    }

    public Long addNewCategory(Store store, String name){
        Category category = store.addCategory(name);
        em.persist(category);
        return category.getId();
    }

    public void deleteCategory(Store store, Category category) throws IllegalAccessException {
        if(category.getItems().size() != 0){
            throw new IllegalAccessException("Category's item list must be empty before proceeding it.");
        } else{
            if(category.getStore() != store){
                throw new IllegalAccessException("This category does not belong to the given store.");
            } else{
                store.deleteCategory(category);
                em.remove(category);
            }
        }
    }

    //==STORE TAG REPOSITORY==//
    public List<StoreTag> findOneStoreTagByStoreAndTag(Store store, Tag tag){
        return em.createQuery("select st from StoreTag st where st.store = :store and st.tag = :tag", StoreTag.class)
                .setParameter("store", store)
                .setParameter("tag", tag)
                .getResultList();
    }

    public List<StoreTag> findAllStoreTags(){
        return em.createQuery("select st from StoreTag st", StoreTag.class).getResultList();
    }

    public List<StoreTag> findStoreTagsByStore(Store store){
        return em.createQuery("select st from StoreTag st where st.store = :store", StoreTag.class)
                .setParameter("store", store)
                .getResultList();
    }

    public List<StoreTag> findStoreTagsByTag(Tag tag){
        return em.createQuery("select st from StoreTag st where st.tag = :tag", StoreTag.class)
                .setParameter("tag", tag)
                .getResultList();
    }

//    public void deleteStoreTag(StoreTag storeTag){
//        storeTag.getStore().deleteStoreTag(storeTag);
//        em.remove(storeTag);
//    }

    public void deleteStoreTagByStoreAndTag(Store store, Tag tag){
        List<StoreTag> storeTags = findOneStoreTagByStoreAndTag(store, tag);
        for(StoreTag storeTag: storeTags){
            storeTag.getStore().deleteStoreTag(storeTag);
            em.remove(storeTag);
        }
    }
}
