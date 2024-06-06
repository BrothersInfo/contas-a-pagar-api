package desafio.service;

import desafio.domain.model.Conta;
import desafio.domain.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CsvServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private CsvService csvService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldImportData() throws Exception {
        String csvData = "dataVencimento,dataPagamento,valor,descricao,situacao" +
                "2024-06-06,2024-06-07,100.00,Pendente,Pendente" +
                "2024-06-08,,200.00,Pagamento,Pendente";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);

        Conta bill = Conta.builder()
                .dataVencimento(LocalDate.of(2024, 6, 6))
                .dataPagamento(LocalDate.of(2024, 6, 7))
                .valor(new BigDecimal("100.00"))
                .descricao("Pagamento1")
                .situacao("Pendente")
                .build();

        Conta bill2 = Conta.builder()
                .dataVencimento(LocalDate.of(2024, 6, 8))
                .dataPagamento(null)
                .valor(new BigDecimal("200.00"))
                .descricao("Pagamento1")
                .situacao("Pendente")
                .build();

        List<Conta> expectedBills = Arrays.asList(bill, bill2);
        when(billRepository.saveAll(anyList())).thenReturn(expectedBills);

        List<Conta> importedBills = csvService.importFromCsv(file);

        assertEquals(2, importedBills.size());
        assertEquals(expectedBills, importedBills);
        verify(billRepository, times(1)).saveAll(anyList());
    }

    @Test
    void exportCsv_shouldExportDataCorrectly() {
        Conta conta1 = Conta.builder()
                .dataVencimento(LocalDate.of(2024, 6, 6))
                .dataPagamento(LocalDate.of(2024, 6, 7))
                .valor(new BigDecimal("100.00"))
                .descricao("Pagamento1")
                .situacao("Pendente")
                .build();

        Conta conta2 = Conta.builder()
                .dataVencimento(LocalDate.of(2024, 6, 8))
                .dataPagamento(null)
                .valor(new BigDecimal("200.00"))
                .descricao("Pagamento2")
                .situacao("Pendente")
                .build();

        List<Conta> bills = Arrays.asList(conta1, conta2);
        when(billRepository.findAll()).thenReturn(bills);

        String csvContent = csvService.exportCsv();

        verify(billRepository, times(1)).findAll();
    }
}
