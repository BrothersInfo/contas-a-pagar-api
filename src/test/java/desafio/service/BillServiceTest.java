package desafio.service;

import desafio.domain.model.Conta;
import desafio.domain.repository.BillRepository;
import desafio.enums.PaymentStatus;
import desafio.exception.BillNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveAndReturnBill() {
        Conta conta = getBill();
        conta.setDataPagamento(LocalDate.now());
        when(billRepository.save(any(Conta.class))).thenReturn(conta);

        Conta bill = billService.saveBill(conta);

        assertEquals(PaymentStatus.PAYED.getValue(), bill.getSituacao());
        verify(billRepository, times(1)).save(conta);
    }

    @Test
    void shouldReturnBillById() {
        when(billRepository.findById(anyLong())).thenReturn(Optional.of(Conta.builder().build()));

        Conta bill = billService.findBillById(1L);

        assertNotNull(bill);
        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(billRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.findBillById(1L));
        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateAndReturnBill() {
        Conta existingBill = new Conta();
        Conta updatedBill = new Conta();
        updatedBill.setDataPagamento(LocalDate.now());
        when(billRepository.findById(anyLong())).thenReturn(Optional.of(existingBill));
        when(billRepository.save(any(Conta.class))).thenReturn(updatedBill);

        Conta bill = billService.updateBill(1L, updatedBill);

        assertEquals(PaymentStatus.PAYED.getValue(), bill.getSituacao());
        verify(billRepository, times(1)).findById(1L);
        verify(billRepository, times(1)).save(updatedBill);
    }

    @Test
    void shouldReturnPageOfBills() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> page = new PageImpl<>(Collections.singletonList(new Conta()));
        when(billRepository.findAll(pageable)).thenReturn(page);

        Page<Conta> bill = billService.findBills(0, 10);

        assertNotNull(bill);
        assertEquals(1, bill.getTotalElements());
        verify(billRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldDeleteBillWhenExists() {
        when(billRepository.findById(anyLong())).thenReturn(Optional.of(Conta.builder().build()));
        doNothing().when(billRepository).deleteById(anyLong());

        billService.deleteBillById(1L);

        verify(billRepository, times(1)).findById(1L);
        verify(billRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBillById_shouldThrowExceptionWhenNotFound() {
        when(billRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.deleteBillById(1L));
        verify(billRepository, times(1)).findById(1L);
        verify(billRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void shouldReturnPageOfBills_whenFindBillsByDataVencimentoAndDescricao() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> page = new PageImpl<>(Collections.singletonList(new Conta()));
        when(billRepository.findByDataVencimentoAndDescricao(any(LocalDate.class), anyString(), eq(pageable))).thenReturn(page);

        Page<Conta> bills = billService.findBillsByDataVencimentoAndDescricao("2024-06-06", "descricao", 0, 10);

        assertNotNull(bills);
        assertEquals(1, bills.getTotalElements());
        verify(billRepository, times(1)).findByDataVencimentoAndDescricao(any(LocalDate.class), eq("descricao"), eq(pageable));
    }

    @Test
    void shouldReturnTotalValue() {
        when(billRepository.getTotalBetweenDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(BigDecimal.TEN);

        BigDecimal bill = billService.getValorTotalBetweenDates("2024-06-01", "2024-06-30");

        assertEquals(BigDecimal.TEN, bill);
        verify(billRepository, times(1)).getTotalBetweenDates(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void shouldUpdateBillStatus() {
        Conta conta = new Conta();
        when(billRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(billRepository.save(any(Conta.class))).thenReturn(conta);

        billService.updateStatus(1L, PaymentStatus.PAYED.getValue());

        assertEquals(PaymentStatus.PAYED.getValue(), conta.getSituacao());
        assertNotNull(conta.getDataPagamento());
        verify(billRepository, times(1)).findById(1L);
        verify(billRepository, times(1)).save(conta);
    }

    @Test
    void updateStatus_shouldThrowExceptionWhenBillNotFound() {
        when(billRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.updateStatus(1L, PaymentStatus.PAYED.getValue()));
        verify(billRepository, times(1)).findById(1L);
    }

    private Conta getBill() {
        return Conta.builder()
                .id(1L)
                .valor(new BigDecimal(100))
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .situacao("Paga")
                .descricao("Conta")
                .build();
    }
}
