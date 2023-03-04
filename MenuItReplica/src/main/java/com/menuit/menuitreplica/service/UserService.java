package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.User;
import com.menuit.menuitreplica.domain.UserRole;
import com.menuit.menuitreplica.repository.UserRepository;
import com.menuit.menuitreplica.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    //Sign Up
    @Transactional
    public Long join(String email, String fullName, String password, String role, @Nullable String phoneNumber) throws Exception {
        User user = new User();
        user.setEmail(email
        );
        //checking for a username if it is duplicated.
        validateDuplicateEmail(email);
        user.setFullName(fullName);
        user.setRole(UserRole.valueOf(role));
        user.setPhone(phoneNumber);

        //encoding the password
        String enPw = passwordEncoder.encode(password);
        user.setPassword(enPw);
        return userRepository.save(user);
    }

//    private void validateDuplicateUser(User user) {
//        String email = user.getEmail();
//        validateDuplicateEmail(email);
//    }
    
    private void validateDuplicateEmail(String email){
        List<User> findUsers = userRepository.findByEmail(email);
        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("There is an user with a same email address.");
        }
    }

    @Transactional
    public Long changePassword(Long userId, String newPassword) throws Exception {
        User user = userRepository.findOne(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return userId;
    }

    public Boolean login(String email, String rawPw) {
//        User user = userRepository.findByEmail(email).get(0);
        UserDetails user = loadUserByEmail(email);
        return passwordEncoder.matches(rawPw, user.getPassword());
    }


    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return loadUserByUsername(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<User> user = userRepository.findByEmail(email);
        if(user.size() == 0) {
            throw new UsernameNotFoundException("Cannot find the email: " + email);
        }
        System.out.println("User with email:" + email + " is found. Will try login");
        return new PrincipalDetails(user.get(0));
    }

    //look up all the users
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findOneById(Long userId){
        return userRepository.findOne(userId);
    }

    public List<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findByUserRole(String userRole){
        return userRepository.findByUserRole(UserRole.valueOf(userRole));
    }

    public List<User> findByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
