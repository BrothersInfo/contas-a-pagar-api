package desafio.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contas")
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da conta", example = "1")
    private Long id;

    @Schema(description = "Data de vencimento da conta", example = "2024-06-06")
    private LocalDate dataVencimento;
    @Schema(description = "Data de pagamento da conta", example = "2024-06-06")
    private LocalDate dataPagamento;
    @Schema(description = "Valor da conta", example = "100.00")
    private BigDecimal valor;
    @Schema(description = "Descrição da conta", example = "Pagamento de serviços")
    private String descricao;
    @Schema(description = "Situação da conta", example = "Paga ou Pendente")
    private String situacao;

}

