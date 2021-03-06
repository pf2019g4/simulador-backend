package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimuladorProduccionService {

    private final CuentaService cuentaService;

    Estado simular(Estado estado, Boolean quiebreDeCaja) {

        imputarGastosProduccion(estado, quiebreDeCaja);
        //TODO aca habria que amortizar la maquinaria para el periodo

        return estado;
    }

    private void imputarGastosProduccion(Estado estado, Boolean quiebreDeCaja) {
        BigDecimal costoProduccionPeriodo = quiebreDeCaja ? BigDecimal.ZERO : calcularCostoProduccionPeriodo(estado);

        if (!quiebreDeCaja) {
            aumentarStockEInventario(estado, costoProduccionPeriodo);
        }

        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();
        Cuenta cuentaFinanciera = cuentaService.crearCuentaFinanciera(estado.getProyecto().getId(),
                TipoTransaccion.COSTO_PRODUCCION.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(),
                TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, null, TipoTransaccion.COSTO_PRODUCCION, estado.getEsForecast());
        cuentaFinanciera.setTipoBalance(TipoBalance.DEUDA_PROVEEDORES);

        for (ModalidadPago modalidadPago : estado.getProyecto().getProveedorSeleccionado().getModalidadPago()) {
            BigDecimal porcentajeGastos = modalidadPago.getPorcentaje().divide(new BigDecimal(100));
            BigDecimal costoPeriodo = costoProduccionPeriodo.multiply(porcentajeGastos);
            if (costoPeriodo.compareTo(BigDecimal.ZERO) != 0) {
                cuentasPeriodos.add(cuentaService.crearCuentaFinancieraPeriodo(estado.getPeriodo() + modalidadPago.getOffsetPeriodo(), costoPeriodo, cuentaFinanciera));
            }
            if (modalidadPago.getOffsetPeriodo() == 0) {
                estado.setCaja(estado.getCaja().subtract(costoPeriodo));
            }
        }

        cuentaFinanciera.setCuentasPeriodo(cuentasPeriodos);
        cuentaService.guardar(cuentaFinanciera);

    }

    private void aumentarStockEInventario(Estado estado, BigDecimal costoProduccionPeriodo) {
        estado.setStock(estado.getStock() + estado.getProduccionMensual());
        estado.setInventario(estado.getInventario().add(costoProduccionPeriodo));
    }

    private BigDecimal calcularCostoProduccionPeriodo(Estado estado) {
        return estado.getCostoVariable()
                .multiply(new BigDecimal(estado.getProduccionMensual()))
                .add(estado.getCostoFijo());
    }
}
