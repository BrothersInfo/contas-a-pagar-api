package desafio.domain.repository;

import desafio.domain.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface BillRepository extends JpaRepository<Conta, Long> {
    Page<Conta> findByDataVencimentoAndDescricao(LocalDate dataVencimento, String descricao, Pageable pageable);

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento IS NOT NULL AND c.dataPagamento BETWEEN :startDate AND :endDate")
    BigDecimal getTotalBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

