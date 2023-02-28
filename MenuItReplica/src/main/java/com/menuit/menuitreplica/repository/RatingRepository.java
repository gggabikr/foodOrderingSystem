package com.menuit.menuitreplica.repository;
import com.menuit.menuitreplica.domain.Rating;
import com.menuit.menuitreplica.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}
