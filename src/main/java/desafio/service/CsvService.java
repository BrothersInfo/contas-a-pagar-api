package desafio.service;

import desafio.domain.model.Conta;
import desafio.domain.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvService {

    private final BillRepository billRepository;

    public CsvService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public List<Conta> importFromCsv(MultipartFile file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<Conta> bills = br.lines()
                    .skip(1)
                    .map(this::lineToConta)
                    .collect(Collectors.toList());

            return billRepository.saveAll(bills);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer a importação do arquivo CSV: " + e.getMessage());
        }
    }

    private Conta lineToConta(String line) {
        String[] data = line.split(",");
        return Conta.builder()
                .dataVencimento(LocalDate.parse(data[0]))
                .dataPagamento(data[1].isEmpty() ? null : LocalDate.parse(data[1]))
                .valor(new BigDecimal(data[2]))
                .descricao(data[3])
                .situacao(data[4])
                .build();
    }

    public String exportCsv() {
        List<Conta> bills = billRepository.findAll();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        writer.println("dataVencimento,dataPagamento,valor,descricao,situacao");

        for (Conta conta : bills) {
            writer.printf("%s,%s,%.2f,%s,%s%n",
                    conta.getDataVencimento(),
                    conta.getDataPagamento(),
                    conta.getValor(),
                    conta.getDescricao(),
                    conta.getSituacao());
        }

        return stringWriter.toString();
    }
}
