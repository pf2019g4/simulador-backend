package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FlujoFondoService {

    private final CuentaService cuentaService;

    //Incompleto
    public Map<String, AgrupadorVo> calcular(Long idProyecto, Integer cantidadPeriodos) {

        List<Cuenta> cuentasIAI = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);
        List<Cuenta> cuentasEAI = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS);
        List<Cuenta> cuentasGND = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES);

        Cuenta cuentaUNI = new Cuenta();
        IntStream.range(0, cantidadPeriodos).forEach(periodo -> {
            cuentaUNI.getCuentasPeriodo().add(new CuentaPeriodo(null, null, montoPeriodo(cuentasIAI, periodo).subtract(montoPeriodo(cuentasEAI, periodo)).subtract(montoPeriodo(cuentasGND, periodo)), periodo));
        });

        Map<String, AgrupadorVo> hashMap = new HashMap<>();


        return null;
    }

    private BigDecimal montoPeriodo(List<Cuenta> cuentas, Integer periodo) {
        return cuentas
                .stream()
                .map(cuenta -> cuenta.getCuentasPeriodo()
                        .stream()
                        .filter(cuentaPeriodo -> cuentaPeriodo.getPeriodo() == periodo)
                        .findFirst()
                        .map(CuentaPeriodo::getMonto)
                        .orElse(new BigDecimal(0)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
