package com.financeiro.service;

import com.financeiro.model.Transferencia;
import com.financeiro.repository.TransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    public Transferencia agendarTransferencia(Transferencia transferencia) {
        validarContas(transferencia);
        BigDecimal taxa = calcularTaxa(transferencia.getValor(), transferencia.getDataTransferencia());

        if (taxa == null) {
            throw new IllegalArgumentException("Nenhuma taxa aplicável. Transferência não permitida.");
        }

        transferencia.setTaxa(taxa);
        return transferenciaRepository.save(transferencia);
    }

    public List<Transferencia> listarAgendamentos() {
        return transferenciaRepository.findAll();
    }

    public Optional<Transferencia> buscarPorId(Long id) {
        return transferenciaRepository.findById(id);
    }

    public Transferencia atualizarTransferencia(Long id, Transferencia transferenciaAtualizada) {
        return transferenciaRepository.findById(id)
                .map(transferencia -> {
                    transferencia.setContaOrigem(transferenciaAtualizada.getContaOrigem());
                    transferencia.setContaDestino(transferenciaAtualizada.getContaDestino());
                    transferencia.setValor(transferenciaAtualizada.getValor());
                    transferencia.setDataTransferencia(transferenciaAtualizada.getDataTransferencia());
                    BigDecimal taxa = calcularTaxa(transferenciaAtualizada.getValor(), transferenciaAtualizada.getDataTransferencia());

                    if (taxa == null) {
                        throw new IllegalArgumentException("Nenhuma taxa aplicável. Transferência não permitida.");
                    }

                    transferencia.setTaxa(taxa);
                    return transferenciaRepository.save(transferencia);
                }).orElseThrow(() -> new RuntimeException("Transferência não encontrada"));
    }

    public void excluirTransferencia(Long id) {
        transferenciaRepository.deleteById(id);
    }

    private void validarContas(Transferencia transferencia) {
        if (!transferencia.getContaOrigem().matches("\\d{10}") || !transferencia.getContaDestino().matches("\\d{10}")) {
            throw new IllegalArgumentException("As contas devem ter exatamente 10 dígitos.");
        }
    }

    private BigDecimal calcularTaxa(BigDecimal valor, LocalDate dataTransferencia) {
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), dataTransferencia);

        if (dias == 0) {
            return valor.multiply(BigDecimal.valueOf(0.025)).add(BigDecimal.valueOf(3.00)); // 2,5% + R$ 3,00
        } else if (dias >= 1 && dias <= 10) {
            return BigDecimal.valueOf(12.00); // Taxa fixa de R$ 12,00
        } else if (dias >= 11 && dias <= 20) {
            return valor.multiply(BigDecimal.valueOf(0.082)); // 8,2%
        } else if (dias >= 21 && dias <= 30) {
            return valor.multiply(BigDecimal.valueOf(0.069)); // 6,9%
        } else if (dias >= 31 && dias <= 40) {
            return valor.multiply(BigDecimal.valueOf(0.047)); // 4,7%
        } else if (dias >= 41 && dias <= 50) {
            return valor.multiply(BigDecimal.valueOf(0.017)); // 1,7%
        } else {
            return null; // Fora dos limites estabelecidos → Transferência não permitida
        }
    }
}
