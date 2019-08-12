package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Consecuencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;
    private String descripcion;
    private Long opcionId;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta; // financiero , economico , etc

    @Enumerated(EnumType.STRING)
    private TipoFlujoFondo tipoFlujoFondo;

}
