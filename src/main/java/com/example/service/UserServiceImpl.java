package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

	@Override
	public User register(String username, String password, String email) {
		User user = new User();
		user.setPassword(encoder.encode(password));
		user.setUsername(username);
		user.setCreatedate(LocalDateTime.now());
		user.setEmail(email);
        return userRepository.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	@Override
	public boolean checkPassword(User user, String rawPassword) {
		return encoder.matches(rawPassword, user.getPassword());
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public boolean deleteByUserId(Integer userid) {
		if (userRepository.existsById(userid)) {
			userRepository.deleteById(userid);
            return true;
        }
		return false;
	}

	@Override
	public boolean updatePassword(Integer userid, String password, String newpassword) {
		Optional<User> optionalUser = userRepository.findById(userid);
	    
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();

	        if (passwordEncoder.matches(password, user.getPassword())) {
	            String encodedNewPassword = passwordEncoder.encode(newpassword);
	            user.setPassword(encodedNewPassword);
	            userRepository.save(user);
	            return true;
	        }
	    }

	    return false;
	}

	@Override
	public Optional<User> findByUserId(Integer userid) {
		return userRepository.findById(userid);
	}
    
}
