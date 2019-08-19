package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaPeriodoRepository cuentaPeriodoRepository;

    public List<Cuenta> obtenerPorProyectoYTipoFlujoFondo(Long idProyecto, TipoFlujoFondo tipoFlujoFondo) {
        return cuentaRepository.findByProyectoIdAndTipoFlujoFondo(idProyecto, tipoFlujoFondo);
    }


    public void crearProduccion(Estado estado, BigDecimal costoPeriodo) {
        crearCuentaFinanacieraProduccion(estado, costoPeriodo);
        crearCuentaEconomicaProduccion(estado, costoPeriodo);

    }

    private void crearCuentaFinanacieraProduccion(Estado estado, BigDecimal costoPeriodo) {
        Cuenta cuenta = Cuenta.builder().descripcion("costo produccion periodo " + estado.getMes())
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuenta = cuentaRepository.save(cuenta);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuenta)
                .monto(costoPeriodo.negate())
                .periodo(estado.getMes()).build());
    }

    private void crearCuentaEconomicaProduccion(Estado estado, BigDecimal costoPeriodo) {

        Cuenta cuenta = Cuenta.builder().descripcion("costo produccion periodo " + estado.getMes())
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuenta = cuentaRepository.save(cuenta);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuenta)
                .monto(costoPeriodo.negate())
                .periodo(estado.getMes()).build());
    }

    public void crearCuentaEconomicaVenta(Long idProyecto, Integer periodo, BigDecimal ventas) {
        CuentaPeriodo cuentaPeriodo = new CuentaPeriodo();
        
        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();
        
        Cuenta cuentaEconomica = Cuenta.builder()
                .descripcion("venta periodo " + periodo)
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .proyectoId(idProyecto)
                .build();

        cuentasPeriodos.add(cuentaPeriodo.builder()
                .cuenta(cuentaEconomica)
                .monto(ventas)
                .periodo(periodo).build());
        
        cuentaEconomica.setCuentasPeriodo(cuentasPeriodos);
        
        cuentaRepository.save(cuentaEconomica);
    }

    public void crearCuentaFinancieraVenta(Long idProyecto, Integer periodo, BigDecimal ventas) {
        CuentaPeriodo cuentaPeriodo = new CuentaPeriodo();
        
        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();
        
        Cuenta cuentaFinanciera = Cuenta.builder()
                .descripcion("venta periodo " + periodo)
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS)
                .proyectoId(idProyecto)
                .build();
        
        cuentasPeriodos.add(cuentaPeriodo.builder()
                .cuenta(cuentaFinanciera)
                .monto(ventas)
                .periodo(periodo).build());
        
        cuentaFinanciera.setCuentasPeriodo(cuentasPeriodos);
        
        cuentaRepository.save(cuentaFinanciera);
    }

    public void guardar(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }
}
