package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class CuentaPeriodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    private BigDecimal monto;

    private Integer periodo; // mes 1, 2, 3, etc

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta; // financiero , economico , etc

}
