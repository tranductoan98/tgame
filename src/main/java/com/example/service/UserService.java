package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.entity.User;

public interface UserService {
	User register(String username, String password, String email);
	boolean deleteByUserId(Integer userid); 
	boolean updatePassword(Integer userid, String password, String newpassword);
    boolean checkPassword(User user, String rawPassword);
    User findByUsername(String username);
    Optional<User> findByUserId(Integer userid);
    List<User> getAllUsers();
}
