package com.skillshare.user_service;

import com.skillshare.user_service.model.User;
import com.skillshare.user_service.repositary.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService extends OidcUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);
        String id = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        String username = oidcUser.getFullName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User(id, username, email, null);  // No password for OAuth
            userRepository.save(user);
        }
        return oidcUser;
    }

    public User registerTraditional(String username, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already exists");
        }
        String id = UUID.randomUUID().toString();  // Generate a unique ID
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(id, username, email, hashedPassword);
        return userRepository.save(user);
    }

    public String loginTraditional(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(user.getUsername());
    }

    public User updateProfile(String userId, String bio, List<String> skills) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBio(bio);
        user.setSkills(skills);
        return userRepository.save(user);
    }

    public void updateTotalLikes(String userId, long likes) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setTotalLikes(user.getTotalLikes() + likes);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);  // Using email as username
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}