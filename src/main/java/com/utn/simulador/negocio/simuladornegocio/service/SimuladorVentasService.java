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

    Estado simular(Estado estado, Boolean quiebreDeCaja) {

        Forecast forecast = forecastService.obtenerPorProyectoYPeriodo(estado.getProyecto().getId(), estado.getPeriodo());
        long unidadesPosiblesParaVender = forecast != null ? forecast.getCantidadUnidades() : 0;
        long unidadesVendidas = quiebreDeCaja ? 0 : Math.min(Math.max(estado.getStock(), estado.getProduccionMensual()), unidadesPosiblesParaVender);
       
        BigDecimal precio = forecast != null ? forecast.getPrecio() : BigDecimal.ZERO;
        
        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();
        Cuenta cuentaFinanciera = cuentaService.crearCuentaFinanciera(estado.getProyecto().getId(),
                TipoTransaccion.VENTA.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(), 
                TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoBalance.CREDITO_CLIENTES, TipoTransaccion.VENTA, estado.getEsForecast());
        for (ModalidadCobro modalidadCobro : estado.getProyecto().getModalidadCobro()) {
            BigDecimal porcentajeVentas = modalidadCobro.getPorcentaje().divide(new BigDecimal(100));
            BigDecimal montoVendido = precio.multiply(new BigDecimal(unidadesVendidas)).multiply(porcentajeVentas);
            if(montoVendido.compareTo(BigDecimal.ZERO) != 0) {
                cuentasPeriodos.add(cuentaService.crearCuentaFinancieraPeriodo(estado.getPeriodo() + modalidadCobro.getOffsetPeriodo(), montoVendido, cuentaFinanciera));
            }
        }

        cuentaFinanciera.setCuentasPeriodo(cuentasPeriodos);
        cuentaService.guardar(cuentaFinanciera);

        BigDecimal ingresosCaja = calcularIngresosCaja(estado);
        estado.setCaja(estado.getCaja().add(ingresosCaja));

        estado.setStock(estado.getStock() - unidadesVendidas);
        BigDecimal montoEconomicoVendido = precio.multiply(new BigDecimal(unidadesVendidas));
        estado.setVentas(montoEconomicoVendido);
        cuentaService.crearCuentaEconomica(estado.getProyecto().getId(), estado.getPeriodo(), TipoTransaccion.VENTA.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(), estado.getVentas(), TipoTransaccion.VENTA, estado.getEsForecast());

        estado.setDemandaPotencial(precio.multiply(new BigDecimal(unidadesPosiblesParaVender)));

        return estado;
    }

    private BigDecimal calcularIngresosCaja(Estado estado) {
        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoCuentaYTipoTransaccion(estado.getProyecto().getId(), TipoCuenta.FINANCIERO, TipoTransaccion.VENTA,estado.getEsForecast());

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
