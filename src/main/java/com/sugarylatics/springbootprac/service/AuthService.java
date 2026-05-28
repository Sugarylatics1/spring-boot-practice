package com.sugarylatics.springbootprac.service;

import com.sugarylatics.springbootprac.model.User;
import com.sugarylatics.springbootprac.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    /*
    constructor cuz this is a service that makes a user repository
    and then u can register it with an email and a password
     */
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    // here we say we're going to retun a string here as the API key when we register
    public String register(String email, String rawPassword){
        // if the email exists then it throws an Ex at runtime saying it exists
        if (userRepository.existsByEmail(email)){
            throw new RuntimeException("User with email " + email + " already exists");
        }
        // we make the object user from the class User that's classified as a table.
        User user = new User();
        // so like in normal sql we put the values inside the columns by using the setters.
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setApiKey("nexus_" + UUID.randomUUID().toString());
        user.setTier("BASIC");
        user.setCreatedAt(LocalDateTime.now());
        // we then save the user
        userRepository.save(user);
        return user.getApiKey();
    }
}
