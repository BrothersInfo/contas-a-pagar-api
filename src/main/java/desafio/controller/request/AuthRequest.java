package desafio.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Schema(description = "Nome de usuário para autenticação", example = "admin")
    private String username;
    @Schema(description = "Senha para autenticação", example = "admin")
    private String password;

}