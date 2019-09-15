package com.utn.simulador.negocio.simuladornegocio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    
    @ManyToOne
    //@JoinColumn(name = "opcion_id", nullable = false)
    @JsonBackReference
    private Opcion opcion;
    private int periodoInicio;
    private int cantidadPeriodos;


    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta; // financiero , economico , etc

    @Enumerated(EnumType.STRING)
    private TipoFlujoFondo tipoFlujoFondo;

    public Cuenta obtenerCuenta(Proyecto proyecto) {

        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();

        Cuenta cuenta = Cuenta.builder()
                .descripcion(descripcion)
                .tipoCuenta(tipoCuenta)
                .tipoFlujoFondo(tipoFlujoFondo)
                .proyectoId(proyecto.getId())
                .build();

        for (int numeroPeriodo = 0; numeroPeriodo < cantidadPeriodos; numeroPeriodo++) {
            cuentasPeriodos.add(CuentaPeriodo.builder().monto(monto).periodo(periodoInicio + numeroPeriodo).cuenta(cuenta).build());
        }

        cuenta.setCuentasPeriodo(cuentasPeriodos);
        return cuenta;
    }



}
