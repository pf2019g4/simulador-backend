package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
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
    private final EstadoRepository estadoRepository;

    public List<Cuenta> obtenerPorProyectoYTipoFlujoFondo(Long idProyecto, TipoFlujoFondo tipoFlujoFondo) {
        return cuentaRepository.findByProyectoIdAndTipoFlujoFondo(idProyecto, tipoFlujoFondo);
    }

    public List<Cuenta> obtenerPorProyectoYOpcion(Long idProyecto, Long idOpcion) {
        return cuentaRepository.findByProyectoIdAndOpcionId(idProyecto, idOpcion);
    }

    public void crearProduccion(Estado estado, BigDecimal costoPeriodo) {
        crearCuentaFinanacieraProduccion(estado, costoPeriodo);
        crearCuentaEconomicaProduccion(estado, costoPeriodo);

    }

    private void crearCuentaFinanacieraProduccion(Estado estado, BigDecimal costoPeriodo) {
        Cuenta cuenta = Cuenta.builder().descripcion("costo produccion periodo " + estado.getPeriodo())
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuenta = cuentaRepository.save(cuenta);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuenta)
                .monto(costoPeriodo.negate())
                .periodo(estado.getPeriodo()).build());
    }

    private void crearCuentaEconomicaProduccion(Estado estado, BigDecimal costoPeriodo) {

        Cuenta cuenta = Cuenta.builder().descripcion("costo produccion periodo " + estado.getPeriodo())
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuenta = cuentaRepository.save(cuenta);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuenta)
                .monto(costoPeriodo.negate())
                .periodo(estado.getPeriodo()).build());
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

    public CuentaPeriodo crearCuentaPeriodoVenta(Long idProyecto, Integer periodo, BigDecimal ventas, Cuenta cuentaFinanciera) {

        return CuentaPeriodo.builder()
                .cuenta(cuentaFinanciera)
                .monto(ventas)
                .periodo(periodo).build();
    }

    public Cuenta crearCuentaFinancieraVenta(Long idProyecto, Integer periodo) {

        return Cuenta.builder()
                .descripcion("venta periodo " + periodo)
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS)
                .proyectoId(idProyecto)
                .build();
    }

    public void guardar(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }

    public void crear(List<Cuenta> cuentasAImputar, Estado estado) {
        for (Cuenta cuenta : cuentasAImputar) {
            cuentaRepository.save(cuenta);
            for (CuentaPeriodo cuentaPeriodo : cuenta.getCuentasPeriodo()) {
                cuentaPeriodoRepository.save(cuentaPeriodo);
            }
            estadoRepository.save(estado);
        }
    }

    public Estado inputarCuetasNuevoPeriodo(Estado estado) {
        List<CuentaPeriodo> cuentasPeriodo = cuentaPeriodoRepository.findByProyectoAndPeriodo(estado.getProyecto().getId(), estado.getPeriodo());

        for (CuentaPeriodo cuentaPeriodo : cuentasPeriodo) {
            estado = afectarEstadoSiCorresponde(cuentaPeriodo.getCuenta(), cuentaPeriodo, estado);
        }
        return estado;
    }

    private Estado afectarEstadoSiCorresponde(Cuenta cuenta, CuentaPeriodo cuentaPeriodo, Estado estado) {
        if (cuenta.getTipoCuenta().equals(TipoCuenta.FINANCIERO)
                && cuentaPeriodo.getPeriodo().equals(estado.getPeriodo())) {
            if ((cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS)
                    || cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS))) {
                estado.setCaja(estado.getCaja().add(cuentaPeriodo.getMonto()));
            } else {
                estado.setCaja(estado.getCaja().subtract(cuentaPeriodo.getMonto()));
            }
        }

        return estado;
    }

}
