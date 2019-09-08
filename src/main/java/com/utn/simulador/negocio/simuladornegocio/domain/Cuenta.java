package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Long proyectoId;

    private Long opcionId;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta; // financiero , economico , etc

    @Enumerated(EnumType.STRING)
    private TipoFlujoFondo tipoFlujoFondo;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CuentaPeriodo> cuentasPeriodo;
}
