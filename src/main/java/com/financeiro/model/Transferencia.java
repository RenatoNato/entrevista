package com.financeiro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transferencias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contaOrigem;

    @Column(nullable = false)
    private String contaDestino;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private BigDecimal taxa;

    @Column(nullable = false)
    private LocalDate dataTransferencia;

    @Column(nullable = false)
    private LocalDate dataAgendamento = LocalDate.now();
}
