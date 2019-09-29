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
                "costo produccion periodo " + estado.getPeriodo(), TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS);
        cuentaFinanciera.setTipoBalance(TipoBalance.DEUDA_PROVEEDORES);
        for (Integer offsetPeriodo = 0; offsetPeriodo < estado.getProyecto().getProveedorSeleccionado().getModalidadPago().size(); offsetPeriodo += 1) {
            
            BigDecimal porcentajeGastos = estado.getProyecto().getProveedorSeleccionado().getModalidadPago().get(offsetPeriodo).getPorcentaje().divide(new BigDecimal(100));
            
            BigDecimal costoPeriodo = costoProduccionPeriodo.multiply(porcentajeGastos);
            cuentasPeriodos.add(cuentaService.crearCuentaFinancieraPeriodo(estado.getPeriodo() + offsetPeriodo, costoPeriodo, cuentaFinanciera));
        }
        
        cuentaFinanciera.setCuentasPeriodo(cuentasPeriodos);
        cuentaService.guardar(cuentaFinanciera);

        estado.setCaja(estado.getCaja().subtract(costoProduccionPeriodo));
        cuentaService.crearCuentaEconomica(estado.getProyecto().getId(), estado.getPeriodo(), "costo produccion periodo " + estado.getPeriodo(), costoProduccionPeriodo.negate());
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
