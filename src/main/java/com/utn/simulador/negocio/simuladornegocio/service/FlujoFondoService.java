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
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FlujoFondoService {

    private final CuentaService cuentaService;

    //Incompleto
    public Map<String, AgrupadorVo> calcularCuentas(Long idProyecto, Integer cantidadPeriodos) {

        Map<String, AgrupadorVo> cuentas  = new HashMap<>();

        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put("INGRESOS_AFECTOS_A_IMPUESTOS", new AgrupadorVo("Ingresos Afectos a Impuestos", cuentasIngresosAfectosAImpuestos, null));

        List<Cuenta> cuentasEgresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put("EGRESOS_AFECTOS_A_IMPUESTOS", new AgrupadorVo("Egresos Afectos a Impuestos", cuentasEgresosAfectosAImpuestos, null));

        List<Cuenta> cuentasGastosNoDesembolsables = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES);
        cuentas.put("GASTOS_NO_DESEMBOLSABLES", new AgrupadorVo("Gastos No Desembolsables", cuentasGastosNoDesembolsables, null));

        Cuenta cuentaUtilidadNetaAntesDeImpuestos = new Cuenta();
        IntStream.range(0, cantidadPeriodos).forEach(periodo -> {
            cuentaUtilidadNetaAntesDeImpuestos.getCuentasPeriodo().add(
                    new CuentaPeriodo(
                            null,
                            null,
                            montoPeriodo(cuentasIngresosAfectosAImpuestos, periodo).subtract(montoPeriodo(cuentasEgresosAfectosAImpuestos, periodo)).subtract(montoPeriodo(cuentasGastosNoDesembolsables, periodo)),
                            periodo
                    )
            );
        });
        cuentas.put("UTILIDAD_NETA_ANTES_DE_IMPUESTOS", new AgrupadorVo("Utilidad Neta Antes de Impuestos", null, cuentaUtilidadNetaAntesDeImpuestos.getCuentasPeriodo()));






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
