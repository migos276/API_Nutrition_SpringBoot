package com.allergydetection.service;

import com.allergydetection.dto.UserDto;
import com.allergydetection.entity.User;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.exception.DuplicateResourceException;
import com.allergydetection.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + userDto.getUsername());
        }
        
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userDto.getEmail());
        }

        User user = new User(userDto.getUsername(), userDto.getEmail());
        User savedUser = userRepository.save(user);
        
        return convertToDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable, String search) {
        Page<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userRepository.findByUsernameContainingOrEmailContaining(search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        
        return users.map(this::convertToDto);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (userDto.getUsername() != null && !userDto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userDto.getUsername())) {
                throw new DuplicateResourceException("Username already exists: " + userDto.getUsername());
            }
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new DuplicateResourceException("Email already exists: " + userDto.getEmail());
            }
            user.setEmail(userDto.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}