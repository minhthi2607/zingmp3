package com.code.zingmp3.service.impl;

import com.code.zingmp3.model.User;
import com.code.zingmp3.repository.IUserRepository;
import com.code.zingmp3.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            // FIX 2: Van giu RuntimeException de AuthController bat duoc
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Boolean login(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isEmpty()) {
            return false;
        }
        User existUser = userOptional.get();
        return passwordEncoder.matches(user.getPassword(), existUser.getPassword());
    }
}