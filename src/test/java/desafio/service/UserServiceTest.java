package desafio.service;

import desafio.domain.model.User;
import desafio.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUser() {
        User user = new User();
        user.setUsername("admin");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> userAdmin = userService.findByUsername("admin");

        assertTrue(userAdmin.isPresent());
        assertEquals("admin", userAdmin.get().getUsername());
        verify(userRepository, times(1)).findByUsername("admin");
    }

    @Test
    void shouldReturnEmpty() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> user = userService.findByUsername("");

        assertFalse(user.isPresent());
        verify(userRepository, times(1)).findByUsername("");
    }
}
