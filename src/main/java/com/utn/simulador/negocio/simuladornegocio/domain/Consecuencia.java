package com.utn.simulador.negocio.simuladornegocio.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public Cuenta obtenerCuenta(Proyecto proyecto, int periodoActual) {

        CuentaPeriodo cuentaPeriodo = new CuentaPeriodo();

        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();

        Cuenta cuenta = Cuenta.builder()
                .descripcion(descripcion)
                .tipoCuenta(tipoCuenta)
                .tipoFlujoFondo(tipoFlujoFondo)
                .proyectoId(proyecto.getId())
                .build();

        cuentasPeriodos.add(cuentaPeriodo.builder().monto(monto).periodo(periodoActual).cuenta(cuenta).build());

        cuenta.setCuentasPeriodo(cuentasPeriodos);
        return cuenta;
    }

}
