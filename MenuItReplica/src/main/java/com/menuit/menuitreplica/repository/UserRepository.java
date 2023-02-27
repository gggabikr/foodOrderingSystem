package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.User;
import com.menuit.menuitreplica.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Long save(User user) throws Exception {
        if(!this.findByEmail(user.getEmail()).isEmpty()){
            if(!Objects.equals(user.getId(), this.findByEmail(user.getEmail()).get(0).getId())){
                throw new IllegalStateException("There is an user with a same email address.");
            } else {
                em.merge(user);
            }
        }
        em.persist(user);
        return user.getId();
    }


    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findByEmail(String email){
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<User> findByUserRole(UserRole userRole){
        return em.createQuery("select u from User u where u.role = :userRole", User.class)
                .setParameter("userRole", userRole)
                .getResultList();
    }


    public List<User> findByPhoneNumber(String phoneNumber){
        return em.createQuery("select u from User u where u.phone = :phoneNumber", User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }
}
