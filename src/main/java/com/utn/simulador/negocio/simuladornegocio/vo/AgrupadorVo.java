package com.utn.simulador.negocio.simuladornegocio.vo;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AgrupadorVo {

    private String descripcion;

    //TODO no convendria armar dos tipos de agrupadores? Uno para cuentas que pueden tener otras cuentas, como
    // IngresosAfectosAImpuestos o EgresosAfectosAImpuestos y otro para Impuestos, UtilidadAntesDeImpuestos etc.
    // Si se mantiene como esta se setea en null para los casos como IngresosAfectosAImpuestos el atributo montosPeriodo
    // y en los otros casos como UtilidadAntesDeImpuestos se setea en null el atributo cuentas.
    private List<Cuenta> cuentas;

    private List<CuentaPeriodo> montosPeriodo;

}
