package desafio.controller;

import desafio.controller.request.AuthRequest;
import desafio.domain.model.User;
import desafio.security.JwtTokenProvider;
import desafio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoint relacionado à autenticação")
public class AuthController {

    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário e retorna um token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        Optional<User> user = userService.findByUsername(authRequest.getUsername());

        if (user.isPresent() && user.get().getPassword().equals(authRequest.getPassword())) {
            String token = jwtTokenProvider.createToken(authRequest.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("username", authRequest.getUsername());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Dados de acesso inválidos.");
        }
    }
}

