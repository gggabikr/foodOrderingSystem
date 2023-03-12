package com.menuit.menuitreplica.repository;


import com.menuit.menuitreplica.domain.StoreTag;
import com.menuit.menuitreplica.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;
    private final StoreRepository storeRepository;

    public Long create(Tag tag) throws Exception {
        if(tag.getId() != null){
            em.merge(tag);
        } else{
            em.persist(tag);
        }
        return tag.getId();
    }

    public Tag findOne(Long tagId){
        return em.find(Tag.class, tagId);
    }

    public List<Tag> findAll(){
        return em.createQuery("select t from Tag t", Tag.class)
                .getResultList();
    }

    public List<Tag> findByExactName(String name){
        return em.createQuery(
                        "select t from Tag t where UPPER(t.name) = UPPER(TRIM(:name))",Tag.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Tag> findByNameContaining(String name){
        return em.createQuery(
                        "select t from Tag t where UPPER(t.name) LIKE CONCAT('%' ,UPPER(TRIM(:name)), '%')",Tag.class)
                .setParameter("name", name)
                .getResultList();
    }

    public void deleteTag(Tag tag){
        List<StoreTag> storeTagsByTag = storeRepository.findStoreTagsByTag(tag);
        em.remove(storeTagsByTag.stream());
        em.remove(tag);
    }
}
