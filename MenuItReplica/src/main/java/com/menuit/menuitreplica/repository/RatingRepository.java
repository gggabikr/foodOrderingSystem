package com.menuit.menuitreplica.repository;
import com.menuit.menuitreplica.domain.Rating;
import com.menuit.menuitreplica.domain.Store;
import com.menuit.menuitreplica.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RatingRepository {

    private final EntityManager em;

    public Long save(Rating rating) throws Exception {
        if(rating.getId() != null){
            em.merge(rating);
        } else{
            em.persist(rating);
        }
        return rating.getId();
    }

    public Rating findById(Long id){
        return em.find(Rating.class, id);
    }

    public List<Rating> findByUser(User user){
        return em.createQuery("select r from Rating r where r.writtenBy = :user", Rating.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Rating> findByStore(Store store){
        return em.createQuery("select r from Rating r where r.store = :store", Rating.class)
                .setParameter("store", store)
                .getResultList();
    }

    public List<Rating> findByString(String str){
        return em.createQuery(
                        "SELECT r FROM Rating r WHERE UPPER(r.comment) LIKE CONCAT('%' ,UPPER(TRIM(:str)), '%')",Rating.class)
                .setParameter("str", str)
                .getResultList();
    }

    public List<Rating> findByScore(int score){
        return em.createQuery("select r from Rating r where r.score = :score", Rating.class)
                .setParameter("score", score)
                .getResultList();
    }

    public List<Rating> findByUserAndStore(User user, Store store){
        return em.createQuery("select r from Rating r where r.writtenBy = :user and r.store = :store and r.status = true", Rating.class)
                .setParameter("store", store)
                .setParameter("user", user)
                .getResultList();
    }
}
