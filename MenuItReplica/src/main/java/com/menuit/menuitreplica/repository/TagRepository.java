package com.menuit.menuitreplica.repository;


import com.menuit.menuitreplica.domain.Rating;
import com.menuit.menuitreplica.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;

    public Long create(Tag tag) throws Exception {
        if(tag.getId() != null){
            em.merge(tag);
        } else{
            em.persist(tag);
        }
        return tag.getId();
    }
}
