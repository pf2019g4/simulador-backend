package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.RespuestaRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    //TODO fijarse si tasa de impuesto esta ok por parametro o deberia ser un atributo del proyecto o similar.
    public Map<String, AgrupadorVo> calcularCuentas(Long idProyecto) {

        Optional<Proyecto> proyecto = proyectoRepository.findById(idProyecto);

        if (proyecto.isEmpty()) {
            //TODO devolver 403 o un optional null
            return null;
        }

        Estado estado = estadoRepository.findByProyectoId(idProyecto);
        Integer periodoActual = estado.getMes();

        Map<String, AgrupadorVo> cuentas = new HashMap<>();

        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasIngresosAfectosAImpuestos, null));

        List<Cuenta> cuentasEgresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS);
        cuentas.put(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS.getDescripcion(), cuentasEgresosAfectosAImpuestos, null));

        List<Cuenta> cuentasGastosNoDesembolsables = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES);
        cuentas.put(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.name(), new AgrupadorVo(TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES.getDescripcion(), cuentasGastosNoDesembolsables, null));

        List<CuentaPeriodo> cuentaUtilidadAntesDeImpuestos = IntStream.
                range(0, periodoActual).
                mapToObj(periodo -> new CuentaPeriodo(null, null, sumaMontoPeriodo(cuentasIngresosAfectosAImpuestos, periodo).subtract(sumaMontoPeriodo(cuentasEgresosAfectosAImpuestos, periodo)).subtract(sumaMontoPeriodo(cuentasGastosNoDesembolsables, periodo)), periodo)).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.UTILIDAD_ANTES_DE_IMPUESTOS.getDescripcion(), null, cuentaUtilidadAntesDeImpuestos));

        //TODO los impuestos podrian sumar en vez de restar si la utilidad antes de impuestos es negativa? Eso impactaria en el calculo de la utilidad despues de impuestos?
        List<CuentaPeriodo> cuentaImpuestos = cuentaUtilidadAntesDeImpuestos.
                stream().
                map(cuentaPeriodo -> new CuentaPeriodo(null, null, cuentaPeriodo.getMonto().multiply(new BigDecimal(proyecto.get().getImpuestoPorcentaje())), cuentaPeriodo.getPeriodo())).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.IMPUESTOS.getDescripcion(), null, cuentaImpuestos));

        List<CuentaPeriodo> cuentaUtilidadDespuesDeImpuestos = cuentaUtilidadAntesDeImpuestos.
                stream().
                map(cuentaPeriodo -> new CuentaPeriodo(null, null, cuentaPeriodo.getMonto().multiply(new BigDecimal(1 - proyecto.get().getImpuestoPorcentaje())), cuentaPeriodo.getPeriodo())).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.name(), new AgrupadorVo(TipoFlujoFondo.UTILIDAD_DESPUES_DE_IMPUESTOS.getDescripcion(), null, cuentaUtilidadDespuesDeImpuestos));

        //TODO esto se va a levantar como cuentasGastosNoDesembolsables, es decir, de la base? O lo vamos a armar en base a cuentasGastosNoDesembolsables?
        List<Cuenta> cuentasAjusteGastosNoDesembolsables = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES);
        cuentas.put(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.name(), new AgrupadorVo(TipoFlujoFondo.AJUSTE_DE_GASTOS_NO_DESEMBOLSABLES.getDescripcion(), cuentasAjusteGastosNoDesembolsables, null));

        //TODO falta inversiones!

        //TODO falta restar inversiones para calcualr el flujo de fondos!
        List<CuentaPeriodo> cuentaFlujoDeFondos = IntStream.
                range(0, periodoActual).
                mapToObj(periodo -> new CuentaPeriodo(null, null, montoPeriodo(cuentaUtilidadDespuesDeImpuestos, periodo).add(sumaMontoPeriodo(cuentasAjusteGastosNoDesembolsables, periodo)), periodo)).
                collect(Collectors.toList());
        cuentas.put(TipoFlujoFondo.FLUJO_DE_FONDOS.name(), new AgrupadorVo(TipoFlujoFondo.FLUJO_DE_FONDOS.getDescripcion(), null, cuentaFlujoDeFondos));

        return cuentas;
    }

    private BigDecimal sumaMontoPeriodo(List<Cuenta> cuentas, Integer periodo) {
        return cuentas
                .stream()
                .map(cuenta -> montoPeriodo(cuenta.getCuentasPeriodo(), periodo))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal montoPeriodo(List<CuentaPeriodo> cuentaPeriodos, Integer periodo) {
        return cuentaPeriodos
                .stream()
                .filter(cuentaPeriodo ->
                        cuentaPeriodo.getPeriodo() == periodo)
                .findFirst()
                .map(CuentaPeriodo::getMonto)
                .orElse(new BigDecimal(0));
    }


}
