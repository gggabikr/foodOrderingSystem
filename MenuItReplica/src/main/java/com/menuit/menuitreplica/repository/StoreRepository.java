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

    public List<Store> findByStoreTag(Tag tag){
        List<StoreTag> tag1 = em.createQuery("select ST from StoreTag ST where ST.tag =:tag", StoreTag.class)
                .setParameter("tag", tag)
                .getResultList();

        List<Store> stores = new ArrayList<>();
        for (StoreTag st: tag1) {
            stores.add(st.getStore());
        }
        return stores;
    }

    public List<Store> findByRatingScore(double score){
        return em.createQuery("select s from Store s where s.ratingScore >= :score", Store.class)
                .setParameter("score", score)
                .getResultList();
    }
}
