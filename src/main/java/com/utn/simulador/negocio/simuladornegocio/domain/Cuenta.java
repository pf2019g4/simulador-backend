package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Long proyectoId;

    private BigDecimal monto;

    private Integer periodo; // mes 1, 2, 3, etc

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta; // financiero , economico , etc

    @OneToMany(mappedBy = "cuenta")
    private Set<CuentaPeriodo> cuentPeriodo;
}
