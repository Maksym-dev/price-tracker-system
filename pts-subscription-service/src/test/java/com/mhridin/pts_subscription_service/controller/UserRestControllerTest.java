package com.mhridin.pts_subscription_service.controller;

import com.mhridin.pts_common.entity.User;
import com.mhridin.pts_common.exception.UserNotFoundException;
import com.mhridin.pts_common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRestController userRestController;

    @Test
    void testGetAllUsers() {
        List<User> all = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        all.add(user);

        when(userRepository.findAll()).thenReturn(all);

        List<User> allUsers = userRestController.getAllUsers();

        verify(userRepository, times(1)).findAll();
        assertThat(allUsers).isNotEmpty();
    }

    @Test
    void testGetUserById() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> userById = userRestController.getUserById(1L);

        verify(userRepository, times(1)).findById(userId);
        User result = userById.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
    }

    @Test
    void testGetUserByIdThrowUserNotFoundException() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenThrow(new UserNotFoundException("User with id " + userId + " not found"));

        assertThrows(UserNotFoundException.class, () -> userRestController.getUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testCreateUser() {
        User user = new User();

        when(userRepository.save(any(User.class))).thenReturn(user);

        userRestController.createUser(user);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        User fromDb = new User();
        fromDb.setId(1L);
        fromDb.setName("testDb");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(fromDb));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userRestController.updateUser(1L, user);

        ArgumentCaptor<User> eventCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(eventCaptor.capture());

        User captured = eventCaptor.getValue();
        assertThat(captured.getId()).isEqualTo(1L);
        assertThat(captured.getName()).isEqualTo(user.getName());
    }

    @Test
    void testUpdateUserThrowsIllegalStateException() {
        User user = new User();
        user.setId(1L);

        assertThrows(IllegalStateException.class, () -> userRestController.updateUser(2L, user));

        verify(userRepository, times(0)).findById(user.getId());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void testUpdateUserThrowsUserNotFoundException() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userRestController.updateUser(1L, user));

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void testDeleteUser() {
        User fromDb = new User();
        fromDb.setId(1L);

        when(userRepository.findById(fromDb.getId())).thenReturn(Optional.of(fromDb));

        userRestController.deleteUser(1L);

        verify(userRepository, times(1)).findById(fromDb.getId());
        verify(userRepository, times(1)).delete(fromDb);
    }

    @Test
    void testDeleteUserThrowsUserNotFoundException() {
        User fromDb = new User();
        fromDb.setId(1L);

        when(userRepository.findById(fromDb.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userRestController.deleteUser(1L));

        verify(userRepository, times(1)).findById(fromDb.getId());
        verify(userRepository, times(0)).delete(fromDb);
    }
}