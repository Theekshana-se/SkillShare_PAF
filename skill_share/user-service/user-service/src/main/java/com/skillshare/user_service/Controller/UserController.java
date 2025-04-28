package com.skillshare.user_service.Controller;

import com.skillshare.user_service.JwtUtil;
import com.skillshare.user_service.UserService;
import com.skillshare.user_service.model.User;
import com.skillshare.user_service.repositary.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // Traditional Register
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.registerTraditional(request.getUsername(), request.getEmail(), request.getPassword());
    }

    // Traditional Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.loginTraditional(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    // OAuth Register/Login
    @GetMapping("/register/oauth")
    public ResponseEntity<Void> registerOAuth() {
        return ResponseEntity.status(HttpStatus.FOUND)  // 302 redirect
                .header("Location", "/login/oauth2/authorization/google")
                .build();
    }

    @GetMapping("/oauth-success")
    public ResponseEntity<String> oauthSuccess(@AuthenticationPrincipal OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        String token = jwtUtil.generateToken(email);  // Generate JWT for OAuth user
        return ResponseEntity.status(302).header("Location", "http://localhost:8081/feed?token=" + token).build();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId, @AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser != null && !oidcUser.getSubject().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        return userRepository.findById(userId).orElseThrow();
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody UpdateProfileRequest request,
                           @AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser != null && !oidcUser.getSubject().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        return userService.updateProfile(userId, request.getBio(), request.getSkills());
    }
}

class RegisterRequest {
    private String username;
    private String email;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class LoginRequest {
    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class UpdateProfileRequest {
    private String bio;
    private List<String> skills;

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}