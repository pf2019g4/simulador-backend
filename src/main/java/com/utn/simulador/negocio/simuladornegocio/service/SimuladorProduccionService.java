package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimuladorProduccionService {

    private final CuentaService cuentaService;

    Estado simular(Estado estado) {
        aumentarStock(estado);
        imputarGastosProduccion(estado);
        //TODO aca habria que amortizar la maquinaria para el periodo
        return estado;
    }

    private void imputarGastosProduccion(Estado estado) {
        BigDecimal costoProduccionPeriodo = calcularCostoProduccionPeriodo(estado);
        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();
        Cuenta cuentaFinanciera = cuentaService.crearCuentaFinanciera(estado.getProyecto().getId(), 
                TipoTransaccion.COMPRA.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(), TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, null);
        cuentaFinanciera.setTipoBalance(TipoBalance.DEUDA_PROVEEDORES);

        for(ModalidadPago modalidadPago : estado.getProyecto().getProveedorSeleccionado().getModalidadPago()) {
            BigDecimal porcentajeGastos = modalidadPago.getPorcentaje().divide(new BigDecimal(100));
            BigDecimal costoPeriodo = costoProduccionPeriodo.multiply(porcentajeGastos);
            if(costoPeriodo.compareTo(BigDecimal.ZERO) != 0){
                cuentasPeriodos.add(cuentaService.crearCuentaFinancieraPeriodo(estado.getPeriodo() + modalidadPago.getOffsetPeriodo(), costoPeriodo, cuentaFinanciera));
            }
        }
        
        cuentaFinanciera.setCuentasPeriodo(cuentasPeriodos);
        cuentaService.guardar(cuentaFinanciera);

        estado.setCaja(estado.getCaja().subtract(costoProduccionPeriodo));
        cuentaService.crearCuentaEconomica(estado.getProyecto().getId(), estado.getPeriodo(), TipoTransaccion.COMPRA.getDescripcion() + " " + estado.getProyecto().getEscenario().getNombrePeriodos() + " " + estado.getPeriodo(), costoProduccionPeriodo.negate());
    }

    private void aumentarStock(Estado estado) {
        estado.setStock(estado.getStock() + estado.getProduccionMensual());
    }
    
    private BigDecimal calcularCostoProduccionPeriodo(Estado estado) {
        return estado.getCostoVariable()
            .add(estado.getProyecto().getProveedorSeleccionado().getVariacionCostoVariable())
            .multiply(new BigDecimal(estado.getProduccionMensual()))
            .add(estado.getCostoFijo());
    }
}
