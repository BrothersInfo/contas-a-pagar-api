package desafio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    PENDING("Pendente"),
    PAYED("Paga");

    private final String value;

}
