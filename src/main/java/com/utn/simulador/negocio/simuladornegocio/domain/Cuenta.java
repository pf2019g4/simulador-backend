package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Long proyectoId;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta; // financiero , economico , etc

    @Enumerated(EnumType.STRING)
    private TipoFlujoFondo tipoFlujoFondo;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<CuentaPeriodo> cuentasPeriodo;
}
