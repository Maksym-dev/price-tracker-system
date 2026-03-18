package com.mhridin.pts_subscription_service.controller;

import com.mhridin.pts_common.entity.User;
import com.mhridin.pts_common.exception.UserNotFoundException;
import com.mhridin.pts_common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        Iterable<User> all = userRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        if (!Objects.equals(user.getId(), id)) {
            throw new IllegalStateException("User id and path variable are not the same");
        }
        User fromDB = userRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        fromDB.setName(user.getName());
        fromDB.setEmail(user.getEmail());
        userRepository.save(fromDB);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        User fromDB = userRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.delete(fromDB);
        return ResponseEntity.noContent().build();
    }
}
