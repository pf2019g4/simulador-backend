package com.utn.simulador.negocio.simuladornegocio.vo;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import lombok.Data;

import java.util.List;

@Data
public class AgrupadorVo {

    private String descripcion;

    private List<Cuenta> cuentas;

    private List<CuentaPeriodo> montosPeriodo;

}