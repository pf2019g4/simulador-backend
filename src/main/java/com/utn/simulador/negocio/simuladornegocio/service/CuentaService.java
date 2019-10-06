package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
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

    public List<Cuenta> obtenerPorProyectoYTipoFlujoFondoYTipoBalance(Long idProyecto, TipoFlujoFondo tipoFlujoFondo, TipoBalance tipoBalance) {
        return cuentaRepository.findByProyectoIdAndTipoFlujoFondoAndTipoBalance(idProyecto, tipoFlujoFondo, tipoBalance);
    }

    public List<Cuenta> obtenerPorProyectoYTipoBalance(Long idProyecto, TipoBalance tipoBalance) {
        return cuentaRepository.findByProyectoIdAndTipoBalance(idProyecto, tipoBalance);
    }

    public CuentaPeriodo crearCuentaFinancieraPeriodo(Integer periodo, BigDecimal montoPeriodo, Cuenta cuentaFinanciera) {
        CuentaPeriodo cuentaPeriodo = CuentaPeriodo.builder()
                .cuenta(cuentaFinanciera)
                .monto(montoPeriodo)
                .periodo(periodo).build();
        return cuentaPeriodoRepository.save(cuentaPeriodo);
    }

    public Cuenta crearCuentaFinanciera(Long idProyecto, String desc, TipoFlujoFondo tipoFlujoFondo) {
        Cuenta cuenta = Cuenta.builder()
                .descripcion(desc)
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(tipoFlujoFondo)
                .proyectoId(idProyecto)
                .build();
        return cuentaRepository.save(cuenta);
    }

    public void crearCuentaEconomica(Long idProyecto, Integer periodo, String desc, BigDecimal montoPeriodo) {
        CuentaPeriodo cuentaPeriodo = new CuentaPeriodo();
        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();

        Cuenta cuentaEconomica = Cuenta.builder()
                .descripcion(desc)
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .proyectoId(idProyecto)
                .build();

        cuentasPeriodos.add(cuentaPeriodo.builder()
                .cuenta(cuentaEconomica)
                .monto(montoPeriodo)
                .periodo(periodo).build());

        cuentaEconomica.setCuentasPeriodo(cuentasPeriodos);

        cuentaRepository.save(cuentaEconomica);
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

    public void eliminarCuentasDeProyecto(Long idProyecto) {
        cuentaRepository.deleteByProyectoId(idProyecto);

    }

    private Estado afectarEstadoSiCorresponde(Cuenta cuenta, CuentaPeriodo cuentaPeriodo, Estado estado) {
        if (cuenta.getTipoCuenta().equals(TipoCuenta.FINANCIERO) && cuentaPeriodo.getPeriodo().equals(estado.getPeriodo())) {
            if ((cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS)
                    || cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS))) {
                estado.setCaja(estado.getCaja().add(cuentaPeriodo.getMonto()));
            } else {
                estado.setCaja(estado.getCaja().subtract(cuentaPeriodo.getMonto()));
            }
        }
        return estado;
    }

    void crearPorBalanceInicial(Long proyectoId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
