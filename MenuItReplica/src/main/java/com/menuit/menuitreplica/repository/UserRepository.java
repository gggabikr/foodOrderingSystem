package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.User;
import com.menuit.menuitreplica.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user){
        em.persist(user);
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

    public List<User> findByUserRole(String userRoleName){
        UserRole userRole = UserRole.valueOf(userRoleName);
        return findByUserRole(userRole);
    }

    public List<User> findByPhoneNumber(String phoneNumber){
        return em.createQuery("select u from User u where u.phone = :phoneNumber", User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }
}
