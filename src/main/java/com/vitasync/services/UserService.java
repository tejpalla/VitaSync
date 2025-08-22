package com.vitasync.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.vitasync.dto.UpdateUserRequest;
import com.vitasync.entity.User;
import com.vitasync.repository.UserRepository;


@Service
public class UserService {

    private UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, UpdateUserRequest updateRequest, UserDetails userDetails) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update only the fields that are provided
        if (updateRequest.getName() != null && !updateRequest.getName().trim().isEmpty()) {
            existingUser.setName(updateRequest.getName());
        }
        if (updateRequest.getPhone() != null && !updateRequest.getPhone().trim().isEmpty()) {
            existingUser.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getAddress() != null && !updateRequest.getAddress().trim().isEmpty()) {
            existingUser.setAddress(updateRequest.getAddress());
        }
        if (updateRequest.getCity() != null && !updateRequest.getCity().trim().isEmpty()) {
            existingUser.setCity(updateRequest.getCity());
        }
        if (updateRequest.getIsAvailable() != null) {
            existingUser.setIsAvailable(updateRequest.getIsAvailable());
        }
        
        return userRepository.save(existingUser);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
