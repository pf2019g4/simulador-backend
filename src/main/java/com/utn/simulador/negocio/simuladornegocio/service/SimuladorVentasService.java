package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimuladorVentasService {

    private final CuentaService cuentaService;

    Estado simular(Estado estado) {
        long unidadesVendidas = calcularUnidadesVendidas(estado);
        Integer offsetPeriodo = 0;
        BigDecimal precio = estado.getProducto().getPrecio();

        while (offsetPeriodo < estado.getProyecto().getModalidadCobro().size()) {

            BigDecimal porcentajeVentas = estado.getProyecto().getModalidadCobro().get(offsetPeriodo).getPorcentaje().divide(new BigDecimal(100));
            
            BigDecimal montoVendido = precio.multiply(new BigDecimal(unidadesVendidas)).multiply(porcentajeVentas);
            
            cuentaService.crearCuentaFinancieraVenta(estado.getProyecto().getId(), estado.getMes() + offsetPeriodo, montoVendido);
            offsetPeriodo = offsetPeriodo + 1;
        }
        
        BigDecimal ingresosCaja = calcularIngresosCaja(estado);
        estado.setCaja(estado.getCaja().add(ingresosCaja));

        estado.setStock(estado.getStock() - unidadesVendidas);
        BigDecimal montoEconomicoVendido = precio.multiply(new BigDecimal(unidadesVendidas));
        estado.setVentas(montoEconomicoVendido);
        cuentaService.crearCuentaEconomicaVenta(estado.getProyecto().getId(), estado.getMes(), estado.getVentas());

        return estado;
    }

    private long calcularUnidadesVendidas(Estado estado) {
        return estado.getParametrosVentas().getMedia();
    }

    private BigDecimal calcularIngresosCaja(Estado estado) {
        List<Cuenta> cuentasIngresosAfectosAImpuestos = cuentaService.obtenerPorProyectoYTipoFlujoFondo(estado.getProyecto().getId(), TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);

        return sumaMontoPeriodo(cuentasIngresosAfectosAImpuestos, estado.getMes());
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
