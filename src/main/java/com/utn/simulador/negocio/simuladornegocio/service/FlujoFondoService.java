package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FlujoFondoService {

    private final CuentaService cuentaService;

    //TODO fijarse si tasa de impuesto esta ok por parametro o deberia ser un atributo del proyecto o similar.
    public Map<String, AgrupadorVo> calcularCuentas(Long idProyecto, Integer cantidadPeriodos, Long impuesto) {

        Map<String, AgrupadorVo> cuentas = new HashMap<>();

        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put("INGRESOS_AFECTOS_A_IMPUESTOS", new AgrupadorVo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasIngresosAfectosAImpuestos, null));

        List<Cuenta> cuentasEgresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put("EGRESOS_AFECTOS_A_IMPUESTOS", new AgrupadorVo(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasEgresosAfectosAImpuestos, null));

        List<Cuenta> cuentasGastosNoDesembolsables = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES);
        cuentas.put("GASTOS_NO_DESEMBOLSABLES", new AgrupadorVo(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.getDescripcion(), cuentasGastosNoDesembolsables, null));

        Cuenta cuentaUtilidadAntesDeImpuestos = new Cuenta();
        IntStream.range(0, cantidadPeriodos).forEach(periodo -> {
            cuentaUtilidadAntesDeImpuestos.getCuentasPeriodo().add(
                    new CuentaPeriodo(
                            null,
                            null,
                            montoPeriodo(cuentasIngresosAfectosAImpuestos, periodo).subtract(montoPeriodo(cuentasEgresosAfectosAImpuestos, periodo)).subtract(montoPeriodo(cuentasGastosNoDesembolsables, periodo)),
                            periodo
                    )
            );
        });
        cuentas.put("UTILIDAD_ANTES_DE_IMPUESTOS", new AgrupadorVo(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.getDescripcion(), null, cuentaUtilidadAntesDeImpuestos.getCuentasPeriodo()));


        List<CuentaPeriodo> cuentaImpuestos = cuentaUtilidadAntesDeImpuestos.getCuentasPeriodo().
                stream().
                map(cuentaPeriodo -> new CuentaPeriodo(null, null, cuentaPeriodo.getMonto().multiply(new BigDecimal(impuesto)), cuentaPeriodo.getPeriodo())).
                collect(Collectors.toList());
        cuentas.put("IMPUESTOS", new AgrupadorVo(TipoFlujoFondo.IMPUESTOS.getDescripcion(), null, cuentaImpuestos));

        List<CuentaPeriodo> cuentaUtilidadDespuesDeImpuestos = cuentaUtilidadAntesDeImpuestos.getCuentasPeriodo().
                stream().
                map(cuentaPeriodo -> new CuentaPeriodo(null, null, cuentaPeriodo.getMonto().multiply(new BigDecimal(1 - impuesto)), cuentaPeriodo.getPeriodo())).
                collect(Collectors.toList());
        cuentas.put("UTILIDADA_DESPUES_DE_IMPUESTOS", new AgrupadorVo(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.getDescripcion(), null, cuentaUtilidadDespuesDeImpuestos));

        return null;
    }

    private BigDecimal montoPeriodo(List<Cuenta> cuentas, Integer periodo) {
        return cuentas
                .stream()
                .map(cuenta ->
                        cuenta.getCuentasPeriodo()
                                .stream()
                                .filter(cuentaPeriodo ->
                                        cuentaPeriodo.getPeriodo() == periodo)
                                .findFirst()
                                .map(CuentaPeriodo::getMonto)
                                .orElse(new BigDecimal(0)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
