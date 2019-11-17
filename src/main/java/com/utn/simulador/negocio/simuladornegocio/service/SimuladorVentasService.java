package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SimuladorVentasService {

    private final CuentaService cuentaService;

    private final ForecastService forecastService;
    private final MercadoService mercadoService;

    Estado simular(Estado estado, Boolean quiebreDeCaja) {

        Forecast forecast = forecastService.obtenerPorProyectoYPeriodo(estado.getProyecto().getId(), estado.getPeriodo());

        long unidadesPosiblesParaVender;

        if (estado.getEsForecast()) {
            unidadesPosiblesParaVender = forecast != null ? forecast.getCantidadUnidades() : 0;
        } else {

            unidadesPosiblesParaVender = mercadoService.obtenerCuotaMercado(estado);
        }

        long unidadesVendidas = quiebreDeCaja ? 0 : Math.min(Math.max(estado.getStock(), estado.getProduccionMensual()), unidadesPosiblesParaVender);

        BigDecimal precio = forecast != null ? forecast.getPrecio() : BigDecimal.ZERO;

        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();
        Cuenta cuentaFinanciera = cuentaService.crearCuentaFinanciera(estado.getProyecto().getId(),
                TipoTransaccion.VENTA.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(),
                TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoBalance.CREDITO_CLIENTES, TipoTransaccion.VENTA, estado.getEsForecast());
        for (ModalidadCobro modalidadCobro : estado.getProyecto().getModalidadCobro()) {
            BigDecimal porcentajeVentas = modalidadCobro.getPorcentaje().divide(new BigDecimal(100));
            BigDecimal montoVendido = precio.multiply(new BigDecimal(unidadesVendidas)).multiply(porcentajeVentas);
            if (montoVendido.compareTo(BigDecimal.ZERO) != 0) {
                cuentasPeriodos.add(cuentaService.crearCuentaFinancieraPeriodo(estado.getPeriodo() + modalidadCobro.getOffsetPeriodo(), montoVendido, cuentaFinanciera));
            }
        }

        cuentaFinanciera.setCuentasPeriodo(cuentasPeriodos);
        cuentaService.guardar(cuentaFinanciera);

        BigDecimal ingresosCaja = montoPeriodo(cuentaFinanciera.getCuentasPeriodo(), estado.getPeriodo());
        estado.setCaja(estado.getCaja().add(ingresosCaja));

        BigDecimal costoMercaderiaVendida = BigDecimal.ZERO;

        if (unidadesVendidas > 0 && estado.getStock() > 0) {
            costoMercaderiaVendida = descontarInventarioYCalcularCostoMercaderiaVendida(estado, unidadesVendidas);
        }
        crearCuentaCostoMercaderiaVendida(estado, costoMercaderiaVendida);

        estado.setStock(estado.getStock() - unidadesVendidas);

        BigDecimal montoEconomicoVendido = precio.multiply(new BigDecimal(unidadesVendidas));
        estado.setVentas(montoEconomicoVendido);
        cuentaService.crearCuentaEconomica(estado.getProyecto().getId(), estado.getPeriodo(), TipoTransaccion.VENTA.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(), estado.getVentas(), TipoTransaccion.VENTA, estado.getEsForecast());

        estado.setDemandaPotencial(precio.multiply(new BigDecimal(unidadesPosiblesParaVender)));

        return estado;
    }

    private void crearCuentaCostoMercaderiaVendida(Estado estado, BigDecimal costoMercaderiaVendida) {
        cuentaService.crearCuentaEconomica(estado.getProyecto().getId(), estado.getPeriodo(), TipoTransaccion.COSTO_PRODUCCION.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(), costoMercaderiaVendida.negate(), TipoTransaccion.COSTO_PRODUCCION, estado.getEsForecast());
    }

    private BigDecimal descontarInventarioYCalcularCostoMercaderiaVendida(Estado estado, long unidadesVendidas) {
        BigDecimal costoProduccionPorUnidad = estado.getInventario().divide(BigDecimal.valueOf(estado.getStock()), RoundingMode.HALF_UP);
        BigDecimal costoMercaderiaVendida = costoProduccionPorUnidad.multiply(BigDecimal.valueOf(unidadesVendidas));

        BigDecimal subtract = estado.getInventario().subtract(costoMercaderiaVendida);

        if (subtract.compareTo(BigDecimal.ZERO) < 1) {
            subtract = BigDecimal.ZERO;
            costoMercaderiaVendida = estado.getInventario();
        }
        estado.setInventario(subtract);

            return costoMercaderiaVendida;
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
