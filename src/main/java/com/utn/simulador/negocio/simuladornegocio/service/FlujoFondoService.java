package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor
@Transactional
public class FlujoFondoService {

    private final CuentaService cuentaService;
    private final ProyectoRepository proyectoRepository;
    private final EstadoRepository estadoRepository;

    @Autowired
    protected EntityManager em;

    public Map<String, AgrupadorVo> calcularCuentas(Long idProyecto, boolean esForecast) {

        //TODO: tener en cuenta si es forecast o no
        Optional<Proyecto> proyecto = proyectoRepository.findById(idProyecto);

        if (proyecto.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Estado estado = estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(idProyecto, esForecast);
        Integer periodoActual = estado.getPeriodo();

        Map<String, AgrupadorVo> cuentas = new HashMap<>();

        List<Cuenta> cuentasIngresosAfectosAImpuestos = agregarCuentasIngresosAfectosAImpuestos(idProyecto, cuentas, true, esForecast);

        List<Cuenta> cuentasEgresosAfectosAImpuestos = agregarCuentasEgresosAfectosAImpuestos(idProyecto, cuentas, true, esForecast);

        List<Cuenta> cuentasGastosNoDesembolsables = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES, esForecast);
        cuentas.put(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name(), new AgrupadorVo(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.getDescripcion(), cuentasGastosNoDesembolsables, null));
        cuentas.put(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name(), new AgrupadorVo(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.getDescripcion(), cuentasGastosNoDesembolsables, null));

        List<CuentaPeriodo> cuentaUtilidadAntesDeImpuestos = IntStream.
                range(0, periodoActual + 1).
                mapToObj(periodo -> new CuentaPeriodo(
                null,
                null,
                sumaMontoPeriodo(cuentasIngresosAfectosAImpuestos, periodo).
                        subtract(sumaMontoPeriodo(cuentasEgresosAfectosAImpuestos, periodo)).
                        subtract(sumaMontoPeriodo(cuentasGastosNoDesembolsables, periodo)),
                periodo)).
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
                cuentaPeriodo.getPeriodo())).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.IMPUESTOS.getDescripcion(), null, cuentaImpuestos));

        List<CuentaPeriodo> cuentaUtilidadDespuesDeImpuestos = cuentaUtilidadAntesDeImpuestos.
                stream().
                map(cuentaPeriodo -> new CuentaPeriodo(
                null,
                null,
                cuentaPeriodo.getMonto().
                        subtract(calcularMontoImpuestos(cuentaPeriodo.getMonto(),
                                new BigDecimal(proyecto.get().getEscenario().getImpuestoPorcentaje())
                        )),
                cuentaPeriodo.getPeriodo())).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.getDescripcion(), null, cuentaUtilidadDespuesDeImpuestos));

        List<Cuenta> cuentasIngresosNoAfectosAImpuestos = agregarCuentasIngresosNoAfectosAImpuestos(idProyecto, cuentas, esForecast);

        List<Cuenta> cuentasEgresosNoAfectosAImpuestos = agregarCuentasEgresosNoAfectosAImpuestos(idProyecto, cuentas, esForecast);

        List<Cuenta> cuentasInversiones = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INVERSIONES, esForecast);
        cuentas.put(TipoFlujoFondo.INVERSIONES.name(), new AgrupadorVo(TipoFlujoFondo.INVERSIONES.getDescripcion(), cuentasInversiones, null));

        List<CuentaPeriodo> cuentaFlujoDeFondos = IntStream.
                range(0, periodoActual + 1).
                mapToObj(periodo
                        -> new CuentaPeriodo(
                        null,
                        null,
                        montoPeriodo(cuentaUtilidadDespuesDeImpuestos, periodo).
                                add(sumaMontoPeriodo(cuentasGastosNoDesembolsables, periodo)).
                                add(sumaMontoPeriodo(cuentasIngresosNoAfectosAImpuestos, periodo)).
                                subtract(sumaMontoPeriodo(cuentasEgresosNoAfectosAImpuestos, periodo)).
                                subtract(sumaMontoPeriodo(cuentasInversiones, periodo)),
                        periodo
                )
                ).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.FLUJO_DE_FONDOS.name(), new AgrupadorVo(TipoFlujoFondo.FLUJO_DE_FONDOS.getDescripcion(), null, cuentaFlujoDeFondos));

        return cuentas;
    }

    private List<Cuenta> agregarCuentasEgresosNoAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas, boolean esForecast) {
        List<Cuenta> cuentasEgresosNoAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS, esForecast);
        cuentas.put(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasEgresosNoAfectosAImpuestos, null));
        return cuentasEgresosNoAfectosAImpuestos;
    }

    private List<Cuenta> agregarCuentasIngresosNoAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas, boolean esForecast) {
        List<Cuenta> cuentasIngresosNoAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS, esForecast);
        cuentas.put(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasIngresosNoAfectosAImpuestos, null));
        return cuentasIngresosNoAfectosAImpuestos;
    }

    private List<Cuenta> agregarCuentasEgresosAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas, Boolean agrupadas, boolean esForecast) {
        List<Cuenta> cuentasEgresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, esForecast);
        if (agrupadas) {
            cuentasEgresosAfectosAImpuestos = agruparCuentas(cuentasEgresosAfectosAImpuestos, TipoTransaccion.COSTO_PRODUCCION);
        }

        cuentas.put(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasEgresosAfectosAImpuestos, null));
        return cuentasEgresosAfectosAImpuestos;
    }

    private List<Cuenta> agregarCuentasIngresosAfectosAImpuestos(Long idProyecto, Map<String, AgrupadorVo> cuentas, Boolean agrupadas, boolean esForecast) {
        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, esForecast);

        if (agrupadas) {
            cuentasIngresosAfectosAImpuestos = agruparCuentas(cuentasIngresosAfectosAImpuestos, TipoTransaccion.VENTA);
        }

        cuentas.put(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasIngresosAfectosAImpuestos, null));
        return cuentasIngresosAfectosAImpuestos;
    }

    //TODO: hay que mejorar este agrupador
    private List<Cuenta> agruparCuentas(List<Cuenta> cuentas, TipoTransaccion tipoTransaccion) {
        List<Cuenta> cuentasAgrupadas = new ArrayList<>(cuentas);
        cuentasAgrupadas.removeIf(c -> c.getTipoTransaccion() != null && c.getTipoTransaccion().equals(tipoTransaccion));
        cuentas.removeIf(c -> c.getTipoTransaccion() == null || !c.getTipoTransaccion().equals(tipoTransaccion));

        if (cuentas.size() > 0) {
            Cuenta cuentaAgrupada = cuentas.get(0);
            cuentaAgrupada.setDescripcion(tipoTransaccion.getDescripcion());
            for (Cuenta cuenta : cuentas.subList(1, cuentas.size())) {
                for (CuentaPeriodo cuentaPeriodo : cuenta.getCuentasPeriodo()) {
                    CuentaPeriodo cuentaP = cuentaAgrupada.getCuentasPeriodo().stream().filter(cp -> cp.getPeriodo().equals(cuentaPeriodo.getPeriodo())).findFirst().orElse(null);
                    if (cuentaP != null) {
                        cuentaP.setMonto(cuentaP.getMonto().add(cuentaPeriodo.getMonto()));
                    } else {
                        cuentaAgrupada.agregarCuenta(cuentaPeriodo);
                    }
                }
            }
            cuentasAgrupadas.add(cuentaAgrupada);
        }

        return cuentasAgrupadas;
    }

    private BigDecimal sumaMontoPeriodo(List<Cuenta> cuentas, Integer periodo) {
        return cuentas
                .stream()
                .map(cuenta -> montoPeriodo(cuenta.getCuentasPeriodo(), periodo))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularMontoImpuestos(BigDecimal montoSinImpuesto, BigDecimal impuesto) {

        //if (montoSinImpuesto.signum() < 0) {
        //    return BigDecimal.ZERO;
        //} else {
        return montoSinImpuesto.multiply(impuesto).setScale(2, RoundingMode.FLOOR);
        //}

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

    private enum TipoFinanciero {
        INGRESOS("Ingresos"),
        EGRESOS("Egresos"),
        TOTAL("Total");

        @Getter
        private final String descripcion;

        TipoFinanciero(String descripcion) {
            this.descripcion = descripcion;
        }
    }

    public Map<String, AgrupadorVo> obtenerFlujoFinanciero(Long idProyecto, boolean esForecast) {
        Map<String, AgrupadorVo> cuentas = new HashMap<>();
        Estado estado = estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(idProyecto, esForecast);
        Integer periodoActual = estado.getPeriodo();

        List<Cuenta> cuentasIngresos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, esForecast);
        cuentasIngresos.addAll(cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS, esForecast));
        cuentasIngresos.add(armarCuentaCaja(estado));
        cuentas.put(TipoFinanciero.INGRESOS.name(), new AgrupadorVo(TipoFinanciero.INGRESOS.getDescripcion(), cuentasIngresos, null));

        List<Cuenta> cuentasEgresos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, esForecast);
        cuentasEgresos.addAll(cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS, esForecast));
        cuentas.put(TipoFinanciero.EGRESOS.name(), new AgrupadorVo(TipoFinanciero.EGRESOS.getDescripcion(), cuentasEgresos, null));

        List<CuentaPeriodo> cuentaMovimientosFinancieros = IntStream.
                range(0, periodoActual + 1).
                mapToObj(periodo
                        -> new CuentaPeriodo(
                        null,
                        null,
                        new BigDecimal(BigInteger.ZERO)
                                .add(sumaMontoPeriodo(cuentasIngresos, periodo))
                                .subtract(sumaMontoPeriodo(cuentasEgresos, periodo)),
                        periodo
                )
                ).
                collect(Collectors.toList());
        cuentas.put(TipoFinanciero.TOTAL.name(), new AgrupadorVo(TipoFinanciero.TOTAL.getDescripcion(), null, cuentaMovimientosFinancieros));
        return cuentas;
    }

    private Cuenta armarCuentaCaja(Estado estado) {
        Cuenta cajaInicial = new Cuenta();
        cajaInicial.setDescripcion("Caja");
        cajaInicial.setCuentasPeriodo(new ArrayList<>());
        CuentaPeriodo cp = new CuentaPeriodo();
        cp.setMonto(estado.getProyecto().getEscenario().getBalanceInicial().getActivo().getCaja());
        cp.setPeriodo(1);
        cajaInicial.getCuentasPeriodo().add(cp);
        return cajaInicial;
    }

    public Map<String, AgrupadorVo> obtenerFlujoEconomico(Long idProyecto, boolean esForecast) {
        Map<String, AgrupadorVo> cuentas = new HashMap<>();
        Estado estado = estadoRepository.findByProyectoIdAndActivoTrueAndEsForecast(idProyecto, esForecast);
        Integer periodoActual = estado.getPeriodo();

        List<Cuenta> cuentasVentas = cuentaService.obtenerPorProyectoYTipoCuentaYTipoTransaccion(idProyecto, TipoCuenta.ECONOMICO, TipoTransaccion.VENTA, esForecast);
        cuentas.put(TipoTransaccion.VENTA.name(), new AgrupadorVo(TipoTransaccion.VENTA.getDescripcion(), null, agruparCuentas(cuentasVentas, TipoTransaccion.VENTA).get(0).getCuentasPeriodo()));

        List<Cuenta> cuentasCompras = cuentaService.obtenerPorProyectoYTipoCuentaYTipoTransaccion(idProyecto, TipoCuenta.ECONOMICO, TipoTransaccion.COSTO_PRODUCCION, esForecast);
        cuentas.put(TipoTransaccion.COSTO_PRODUCCION.name(), new AgrupadorVo(TipoTransaccion.COSTO_PRODUCCION.getDescripcion(), null, agruparCuentas(cuentasCompras, TipoTransaccion.COSTO_PRODUCCION).get(0).getCuentasPeriodo()));

        List<CuentaPeriodo> cuentaCostoMarginal = IntStream.
                range(1, periodoActual + 1).
                mapToObj(periodo -> new CuentaPeriodo(
                null,
                null,
                montoPeriodo(cuentas.get(TipoTransaccion.VENTA.toString()).getMontosPeriodo(), periodo).
                        add(montoPeriodo(cuentas.get(TipoTransaccion.COSTO_PRODUCCION.toString()).getMontosPeriodo(), periodo)),
                periodo)).
                collect(Collectors.toList());
        cuentas.put("CM", new AgrupadorVo("CM", null, cuentaCostoMarginal));

        List<Cuenta> cuentasOtras = cuentaService.obtenerPorProyectoYTipoCuentaYTipoTransaccion(idProyecto, TipoCuenta.ECONOMICO, TipoTransaccion.OTROS, esForecast);
        cuentas.put(TipoTransaccion.OTROS.name(), new AgrupadorVo(TipoCuenta.ECONOMICO.name(), cuentasOtras, null));

        List<CuentaPeriodo> cuentaMovimientosEconomicos = IntStream.
                range(1, periodoActual + 1).
                mapToObj(periodo
                        -> new CuentaPeriodo(
                        null,
                        null,
                        new BigDecimal(BigInteger.ZERO)
                                .add(montoPeriodo(cuentas.get(TipoTransaccion.VENTA.name()).getMontosPeriodo(), periodo))
                                .add(montoPeriodo(cuentas.get(TipoTransaccion.COSTO_PRODUCCION.name()).getMontosPeriodo(), periodo))
                                .add(sumaMontoPeriodo(cuentasOtras, periodo)),
                        periodo
                )
                ).
                collect(Collectors.toList());

        cuentas.put("TOTAL", new AgrupadorVo("Total", null, cuentaMovimientosEconomicos));

        return cuentas;

    }
}
