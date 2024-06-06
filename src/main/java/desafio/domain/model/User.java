package desafio.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da conta", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "admin")
    @Column(nullable = false, unique = true)
    private String username;
    @Schema(description = "Senha do usuário", example = "admin")
    @Column(nullable = false)
    private String password;
}