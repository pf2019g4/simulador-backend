package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimuladorVentasService {

    private final CuentaService cuentaService;

    private final ForecastService forecastService;

    Estado simular(Estado estado) {

        Forecast forecast = forecastService.obtenerPorProyectoYPeriodo(estado.getProyecto().getId(), estado.getPeriodo());
        long unidadesPosiblesParaVender = forecast.getCantidadUnidades();
        long unidadesVendidas = Math.min(Math.max(estado.getStock(), estado.getProduccionMensual()), unidadesPosiblesParaVender);
        BigDecimal precio = forecast.getPrecio();
        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();
        Cuenta cuentaFinanciera = cuentaService.crearCuentaFinanciera(estado.getProyecto().getId(),
                "ventas periodo " + estado.getPeriodo(), TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoBalance.CREDITO_CLIENTES);
        for (Integer offsetPeriodo = 0; offsetPeriodo < estado.getProyecto().getModalidadCobro().size(); offsetPeriodo += 1) {

            BigDecimal porcentajeVentas = estado.getProyecto().getModalidadCobro().get(offsetPeriodo).getPorcentaje().divide(new BigDecimal(100));

            BigDecimal montoVendido = precio.multiply(new BigDecimal(unidadesVendidas)).multiply(porcentajeVentas);

            cuentasPeriodos.add(cuentaService.crearCuentaFinancieraPeriodo(estado.getPeriodo() + offsetPeriodo, montoVendido, cuentaFinanciera));
        }

        cuentaFinanciera.setCuentasPeriodo(cuentasPeriodos);
        cuentaService.guardar(cuentaFinanciera);

        BigDecimal ingresosCaja = calcularIngresosCaja(estado);
        estado.setCaja(estado.getCaja().add(ingresosCaja));

        estado.setStock(estado.getStock() - unidadesVendidas);
        BigDecimal montoEconomicoVendido = precio.multiply(new BigDecimal(unidadesVendidas));
        estado.setVentas(montoEconomicoVendido);
        cuentaService.crearCuentaEconomica(estado.getProyecto().getId(), estado.getPeriodo(), "ventas periodo " + estado.getPeriodo(), estado.getVentas());

        estado.setDemandaInsatisfecha(precio.multiply(new BigDecimal(unidadesPosiblesParaVender - unidadesVendidas)));

        return estado;
    }

    private BigDecimal calcularIngresosCaja(Estado estado) {
        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(estado.getProyecto().getId(), TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);

        return sumaMontoPeriodo(cuentasIngresosAfectosAImpuestos, estado.getPeriodo());
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
                .filter(cuentaPeriodo
                        -> cuentaPeriodo.getPeriodo() == periodo)
                .findFirst()
                .map(CuentaPeriodo::getMonto)
                .orElse(new BigDecimal(0));
    }

}
