package com.utn.simulador.negocio.simuladornegocio.domain;

import java.util.ArrayList;
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

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta; // financiero , economico , etc

    @Enumerated(EnumType.STRING)
    private TipoFlujoFondo tipoFlujoFondo;

    @Enumerated(EnumType.STRING)
    private TipoBalance tipoBalance;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoTransaccion;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<CuentaPeriodo> cuentasPeriodo;

    public void agregarCuenta(CuentaPeriodo cuentaPeriodo) {

        if (cuentasPeriodo == null) {
            cuentasPeriodo = new ArrayList<>();
        }
        cuentasPeriodo.add(cuentaPeriodo);
    }

}
