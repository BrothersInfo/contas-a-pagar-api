package desafio.service;

import desafio.domain.model.Conta;
import desafio.domain.repository.BillRepository;
import desafio.enums.PaymentStatus;
import desafio.exception.BillNotFoundException;
import desafio.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BillService {

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Conta saveBill(Conta conta) {
        conta.setSituacao(conta.getDataPagamento() != null ? PaymentStatus.PAYED.getValue() : PaymentStatus.PENDING.getValue());
        return billRepository.save(conta);
    }

    public Conta findBillById(Long id) {
        return billRepository.findById(id).orElseThrow(() ->
                new BillNotFoundException("Conta não encontrada"));
    }

    public Conta updateBill(Long id, Conta conta) {
        conta.setSituacao(conta.getDataPagamento() != null ? PaymentStatus.PAYED.getValue() : PaymentStatus.PENDING.getValue());
        return billRepository.findById(id).map(existingConta -> {
            conta.setId(id);
            return billRepository.save(conta);
        }).orElse(null);
    }

    public Page<Conta> findBills(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return billRepository.findAll(pageable);
    }

    public void deleteBillById(Long id) {
        billRepository.findById(id).ifPresentOrElse(
                conta -> billRepository.deleteById(id),
                () -> {
                    throw new BillNotFoundException("Conta com id " + id + " não encontrada.");
                }
        );
    }

    public Page<Conta> findBillsByDataVencimentoAndDescricao(String dataVencimento, String descricao, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return billRepository.findByDataVencimentoAndDescricao(Utils.getLocalDate(dataVencimento), descricao, pageable);
    }

    public BigDecimal getValorTotalBetweenDates(String dataInicio, String dataFim) {
        return billRepository.getTotalBetweenDates(Utils.getLocalDate(dataInicio), Utils.getLocalDate(dataFim));
    }

    public void updateStatus(Long id, String situacao) {
        var bill = billRepository.findById(id).orElseThrow(() -> new BillNotFoundException("Conta não encontrada"));

        if (situacao.equals(PaymentStatus.PAYED.getValue())) {
            bill.setDataPagamento(LocalDate.now());
            bill.setSituacao(PaymentStatus.PAYED.getValue());
        } else {
            bill.setDataPagamento(null);
            bill.setSituacao(PaymentStatus.PENDING.getValue());
        }

        billRepository.save(bill);
    }
}
