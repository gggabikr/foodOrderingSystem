package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.StoreTag;
import com.menuit.menuitreplica.domain.Tag;
import com.menuit.menuitreplica.repository.StoreRepository;
import com.menuit.menuitreplica.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final StoreRepository storeRepository;

    public Long create(String tagName) throws Exception {
        Tag tag = new Tag();
        tag.setName(tagName);
        return tagRepository.create(tag);

    }

    public Tag findOne(Long tagId){
        return tagRepository.findOne(tagId);
    }

    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

    public List<Tag> findByExactName(String name){
        return tagRepository.findByExactName(name);
    }

    public List<Tag> findByNameContaining(String name){
        return tagRepository.findByNameContaining(name);
    }

    public List<StoreTag> findStoreTagsByTag(Long tagId){
        Tag tag = tagRepository.findOne(tagId);
        return storeRepository.findStoreTagsByTag(tag);
    }

    public void deleteTag(Long tagId){
        Tag tag = tagRepository.findOne(tagId);
        tagRepository.deleteTag(tag);
    }
}
