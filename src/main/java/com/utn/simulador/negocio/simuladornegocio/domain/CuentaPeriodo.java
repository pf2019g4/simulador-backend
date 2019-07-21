package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaPeriodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;
    private BigDecimal monto;

    private Integer periodo; // mes 1, 2, 3, etc
}
