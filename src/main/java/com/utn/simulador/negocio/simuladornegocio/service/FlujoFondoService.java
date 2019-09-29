package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FlujoFondoService {

    private final CuentaService cuentaService;
    private final ProyectoRepository proyectoRepository;
    private final EstadoRepository estadoRepository;

    public Map<String, AgrupadorVo> calcularCuentas(Long idProyecto, boolean esForecast) {

        //TODO: tener en cuenta si es forecast o no
        Optional<Proyecto> proyecto = proyectoRepository.findById(idProyecto);

        if (proyecto.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Estado estado = estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(idProyecto, esForecast);
        Integer periodoActual = estado.getPeriodo();

        Map<String, AgrupadorVo> cuentas = new HashMap<>();

        List<Cuenta> cuentasIngresosAfectosAImpuestos = agregarCuentasIngresosAfectosAImpuestos(idProyecto, cuentas);

        List<Cuenta> cuentasEgresosAfectosAImpuestos = agregarCuentasEgresosAfectosAImpuestos(idProyecto, cuentas);

        List<Cuenta> cuentasGastosNoDesembolsables = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES);
        cuentas.put(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name(), new AgrupadorVo(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.getDescripcion(), cuentasGastosNoDesembolsables, null));

        List<CuentaPeriodo> cuentaUtilidadAntesDeImpuestos = IntStream.
                range(0, periodoActual + 1).
                mapToObj(periodo -> new CuentaPeriodo(
                null,
                null,
                sumaMontoPeriodo(cuentasIngresosAfectosAImpuestos, periodo).
                        subtract(sumaMontoPeriodo(cuentasEgresosAfectosAImpuestos, periodo)).
                        subtract(sumaMontoPeriodo(cuentasGastosNoDesembolsables, periodo)),
                periodo, true)).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.getDescripcion(), null, cuentaUtilidadAntesDeImpuestos));

        List<CuentaPeriodo> cuentaImpuestos = cuentaUtilidadAntesDeImpuestos.
                stream().
                map(cuentaPeriodo -> new CuentaPeriodo(
                null,
                null,
                calcularMontoImpuestos(cuentaPeriodo.getMonto(),
                        new BigDecimal(proyecto.get().getEscenario().getImpuestoPorcentaje())
                        ),
                cuentaPeriodo.getPeriodo(), true)).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.IMPUESTOS.getDescripcion(), null, cuentaImpuestos));

        List<CuentaPeriodo> cuentaUtilidadDespuesDeImpuestos = cuentaUtilidadAntesDeImpuestos.
                stream().
                map(cuentaPeriodo -> new CuentaPeriodo(
                null,
                null,
                cuentaPeriodo.getMonto().
                        subtract(cuentaPeriodo.getMonto().
                                multiply(new BigDecimal(proyecto.get().getEscenario().getImpuestoPorcentaje()))
                        ),
                cuentaPeriodo.getPeriodo(), true)).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.getDescripcion(), null, cuentaUtilidadDespuesDeImpuestos));

        List<Cuenta> cuentasAjusteGastosNoDesembolsables = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES);
        cuentas.put(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name(), new AgrupadorVo(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.getDescripcion(), cuentasAjusteGastosNoDesembolsables, null));

        List<Cuenta> cuentasIngresosNoAfectosAImpuestos = agregarCuentasIngresosNoAfectosAImpuestos(idProyecto, cuentas);

        List<Cuenta> cuentasEgresosNoAfectosAImpuestos = agregarCuentasEgresosNoAfectosAImpuestos(idProyecto, cuentas);

        List<Cuenta> cuentasInversiones = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INVERSIONES);
        cuentas.put(TipoFlujoFondo.INVERSIONES.name(), new AgrupadorVo(TipoFlujoFondo.INVERSIONES.getDescripcion(), cuentasInversiones, null));

        List<CuentaPeriodo> cuentaFlujoDeFondos = IntStream.
                range(0, periodoActual + 1).
                mapToObj(periodo
                        -> new CuentaPeriodo(
                        null,
                        null,
                        montoPeriodo(cuentaUtilidadDespuesDeImpuestos, periodo).
                                add(sumaMontoPeriodo(cuentasAjusteGastosNoDesembolsables, periodo)).
                                add(sumaMontoPeriodo(cuentasIngresosNoAfectosAImpuestos, periodo)).
                                subtract(sumaMontoPeriodo(cuentasEgresosNoAfectosAImpuestos, periodo)).
                                subtract(sumaMontoPeriodo(cuentasInversiones, periodo)),
                        periodo,
                        true
                )
                ).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.FLUJO_DE_FONDOS.name(), new AgrupadorVo(TipoFlujoFondo.FLUJO_DE_FONDOS.getDescripcion(), null, cuentaFlujoDeFondos));

        return cuentas;
    }


    private List<Cuenta> agregarCuentasEgresosNoAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas) {
        List<Cuenta> cuentasEgresosNoAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS);
        cuentas.put(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasEgresosNoAfectosAImpuestos, null));
        return cuentasEgresosNoAfectosAImpuestos;
    }

    private List<Cuenta> agregarCuentasIngresosNoAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas) {
        List<Cuenta> cuentasIngresosNoAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS);
        cuentas.put(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasIngresosNoAfectosAImpuestos, null));
        return cuentasIngresosNoAfectosAImpuestos;
    }

    private List<Cuenta> agregarCuentasEgresosAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas) {
        List<Cuenta> cuentasEgresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasEgresosAfectosAImpuestos, null));
        return cuentasEgresosAfectosAImpuestos;
    }

    private List<Cuenta> agregarCuentasIngresosAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas) {
        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasIngresosAfectosAImpuestos, null));
        return cuentasIngresosAfectosAImpuestos;
    }

    private BigDecimal sumaMontoPeriodo(List<Cuenta> cuentas, Integer periodo) {
        return cuentas
                .stream()
                .map(cuenta -> montoPeriodo(cuenta.getCuentasPeriodo(), periodo))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularMontoImpuestos(BigDecimal montoSinImpuesto, BigDecimal impuesto) {

        if(montoSinImpuesto.signum() < 0){
            return BigDecimal.ZERO;
        } else {
            return montoSinImpuesto.multiply(impuesto).setScale(2, RoundingMode.FLOOR);
        }

    }



    private BigDecimal montoPeriodo(List<CuentaPeriodo> cuentaPeriodos, Integer periodo) {
        return cuentaPeriodos
                .stream()
                .filter(cuentaPeriodo
                        -> cuentaPeriodo.getPeriodo() == periodo)
                .findFirst()
                .map(CuentaPeriodo::getMonto)
                .orElse(new BigDecimal(0));
    }

    public Map<String, AgrupadorVo> obtenerFlujoFinanciero(Long idProyecto, boolean esForecast) {
        Map<String, AgrupadorVo> cuentas = new HashMap<>();
        Estado estado = estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(idProyecto, esForecast);
        Integer periodoActual = estado.getPeriodo();

        List<Cuenta> cuentasIngresos = agregarCuentasIngresosAfectosAImpuestos(idProyecto, cuentas);
        cuentasIngresos.addAll(agregarCuentasIngresosNoAfectosAImpuestos(idProyecto, cuentas));

        List<Cuenta> cuentasEgresos = agregarCuentasEgresosAfectosAImpuestos(idProyecto, cuentas);
        cuentasEgresos.addAll(agregarCuentasEgresosNoAfectosAImpuestos(idProyecto, cuentas));

        List<CuentaPeriodo> cuentaMovimientosFinancieros = IntStream.
                range(0, periodoActual + 1).
                mapToObj(periodo
                        -> new CuentaPeriodo(
                        null,
                        null,
                        new BigDecimal(BigInteger.ZERO).
                                add(sumaMontoPeriodo(cuentasIngresos, periodo)).
                                subtract(sumaMontoPeriodo(cuentasEgresos, periodo)),
                        periodo,
                        false
                )
                ).
                collect(Collectors.toList());
        cuentas.put("TOTAL", new AgrupadorVo("Total", null, cuentaMovimientosFinancieros));

        return cuentas;

    }

}
