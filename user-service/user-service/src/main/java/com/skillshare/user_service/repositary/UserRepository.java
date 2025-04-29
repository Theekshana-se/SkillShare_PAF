package com.skillshare.user_service.repositary;

import com.skillshare.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);  // To check if user exists during login
}