package desafio.controller;

import desafio.domain.model.Conta;
import desafio.service.BillService;
import desafio.service.CsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@Tag(name = "Contas", description = "Operações relacionadas a contas a pagar")
public class BillController {

    private final BillService billService;
    private final CsvService csvService;

    public BillController(BillService billService, CsvService csvService) {
        this.billService = billService;
        this.csvService = csvService;
    }

    @Operation(summary = "Criar uma nova conta", description = "Adiciona uma nova conta a pagar")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Conta> createBill(@RequestBody Conta conta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.saveBill(conta));
    }

    @Operation(summary = "Atualizar conta existente", description = "Atualiza os detalhes de uma conta existente")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Conta> updateBill(@PathVariable Long id,
                                            @RequestBody Conta conta) {
        return ResponseEntity.ok(billService.updateBill(id, conta));
    }

    @Operation(summary = "Listar todas as contas", description = "Retorna uma lista de todas as contas a pagar")
    @Parameters({
            @Parameter(name = "page", description = "Número da página", example = "0"),
            @Parameter(name = "size", description = "Tamanho da página", example = "10")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Conta>> getBills(@RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(billService.findBills(page, size));
    }

    @Operation(summary = "Obter conta por ID", description = "Retorna uma conta específica por seu ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Conta> getContaById(@PathVariable Long id) {
        return ResponseEntity.ok().body(billService.findBillById(id));
    }

    @Operation(summary = "Excluir conta por ID", description = "Exclui uma conta específica por seu ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBillById(@PathVariable Long id) {
        billService.deleteBillById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filtrar contas por data de vencimento e descrição",
            description = "Retorna uma página de contas que correspondem aos filtros de data de vencimento e descrição.")
    @Parameters({
            @Parameter(name = "dataVencimento", description = "Data de vencimento", example = "2024-06-06"),
            @Parameter(name = "descricao", description = "Descrição", example = "Conta de Agua"),
            @Parameter(name = "page", description = "Número da página", example = "0"),
            @Parameter(name = "size", description = "Tamanho da página", example = "10")
    })
    @GetMapping("/find-by-filters")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Conta>> getBillsByFilters(@RequestParam(defaultValue = "2024-06-06") String dataVencimento,
                                                         @RequestParam(defaultValue = "Pagamento de Conta") String descricao,
                                                         @RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(billService.findBillsByDataVencimentoAndDescricao(dataVencimento, descricao, page, size));
    }

    @Operation(summary = "Obter total pago entre datas",
            description = "Retorna o valor total pago entre as datas de início e fim.")
    @GetMapping("/total-payed")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BigDecimal> getTotalBetweenDates(@RequestParam(defaultValue = "2024-06-06") String dataInicio, @RequestParam(defaultValue = "2024-06-06") String dataFim) {
        return ResponseEntity.ok().body(billService.getValorTotalBetweenDates(dataInicio, dataFim));
    }

    @Operation(summary = "Atualizar status da conta",
            description = "Atualiza o status de uma conta específica pelo ID.")
    @Parameters({
            @Parameter(name = "situacao", description = "Status da Conta a Paga", example = "Paga")
    })
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam(defaultValue = "Paga") String situacao) {
        billService.updateStatus(id, situacao);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Importar contas via CSV",
            description = "Importa um lote de contas a pagar a partir de um arquivo CSV.")
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<Conta>> importBillCsv(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(csvService.importFromCsv(file));
    }

    @Operation(summary = "Exportar contas para CSV",
            description = "Exporta todas as contas a pagar em formato CSV.")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv() {
        String csvContent = csvService.exportCsv();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "contas.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent.getBytes());
    }
}

