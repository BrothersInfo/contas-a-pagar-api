package desafio.controller;

import desafio.controller.request.AuthRequest;
import desafio.domain.model.User;
import desafio.security.JwtTokenProvider;
import desafio.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;

    private User user;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("cosme");
        user.setPassword("Senha@01");

        authRequest = new AuthRequest();
        authRequest.setUsername("cosme");
        authRequest.setPassword("Senha@01");
    }

    @Test
    public void shouldReturnSuccess() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOi...");

        ResponseEntity<?> responseEntity = authController.login(authRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());

        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertEquals("cosme", responseBody.get("username"));
        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOi...", responseBody.get("token"));
    }

    @Test
    public void shouldReturnInvalidCredentials() {
        authRequest.setPassword("12345");

        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));

        ResponseEntity<?> responseEntity = authController.login(authRequest);

        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Dados de acesso inválidos.", responseEntity.getBody());
    }

    @Test
    public void shouldReturnUserNotFound() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = authController.login(authRequest);

        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Dados de acesso inválidos.", responseEntity.getBody());
    }
}
