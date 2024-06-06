package desafio.controller;

import desafio.domain.model.Conta;
import desafio.service.BillService;
import desafio.service.CsvService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillControllerTest {

    @Mock
    private BillService billService;

    @Mock
    private CsvService csvService;

    @InjectMocks
    private BillController billController;

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta();
        conta.setId(1L);
        conta.setDataPagamento(LocalDate.now());
        conta.setDataVencimento(LocalDate.now().plusDays(10));
        conta.setValor(new BigDecimal("500.00"));
        conta.setDescricao("Teste");
        conta.setSituacao("Paga");
    }

    @Test
    public void shouldCreateBill() {
        when(billService.saveBill(any(Conta.class))).thenReturn(conta);

        ResponseEntity<Conta> responseEntity = billController.createBill(conta);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(conta, responseEntity.getBody());
    }

    @Test
    public void shouldGetBillById() {
        when(billService.findBillById(1L)).thenReturn(conta);

        ResponseEntity<Conta> responseEntity = billController.getContaById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(conta, responseEntity.getBody());
    }

    @Test
    public void shouldUpdateBill() {
        when(billService.updateBill(1L, conta)).thenReturn(conta);

        ResponseEntity<Conta> responseEntity = billController.updateBill(1L, conta);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(conta, responseEntity.getBody());
    }

    @Test
    public void shouldDeleteBillById() {
        ResponseEntity<Void> responseEntity = billController.deleteBillById(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void shouldGetTotalBetweenDates() {
        BigDecimal total = new BigDecimal("1000.00");
        when(billService.getValorTotalBetweenDates(any(), any())).thenReturn(total);

        ResponseEntity<BigDecimal> responseEntity = billController.getTotalBetweenDates("2024-06-01", "2024-06-10");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(total, responseEntity.getBody());
    }

    @Test
    public void shouldUpdateStatus() {
        ResponseEntity<Void> responseEntity = billController.updateStatus(1L, "Paga");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
