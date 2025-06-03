package com.storerating.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.storerating.Entity.User;
import com.storerating.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User save(User user) {
	    if (user.getId() == null) {
	        // New user, encode password
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	    } else {
	        // Existing user - fetch current user from DB to avoid re-encoding password
	        User existingUser = userRepository.findById(user.getId()).orElse(null);
	        if (existingUser != null && !user.getPassword().equals(existingUser.getPassword())) {
	            // Password was changed, re-encode
	            user.setPassword(passwordEncoder.encode(user.getPassword()));
	        } else {
	            // Password not changed, retain old password
	            user.setPassword(existingUser.getPassword());
	        }
	    }
	    return userRepository.save(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	public long getTotalUsers() {
		return userRepository.count();
	}

	public User updateUserRole(Long userId, User.Role role) {
		User user = findById(userId);
		if (user != null) {
			user.setRole(role);
			return userRepository.save(user);
		}
		return null;
	}

	@Autowired
	private final PasswordEncoder passwordEncoder;

	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	public List<User> findAllUsers() {
	    return userRepository.findAll();
	}
	
	public User updateUserDetails(Long userId, String name, String email, String address) {
	    User user = findById(userId);
	    if (user != null) {
	        user.setName(name);
	        user.setEmail(email);
	        user.setAddress(address);
	        return userRepository.save(user);
	    }
	    return null;
	}
}
