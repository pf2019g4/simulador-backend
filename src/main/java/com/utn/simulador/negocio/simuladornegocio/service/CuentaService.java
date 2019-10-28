package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final ProyectoRepository proyectoRepository;

    public List<Cuenta> obtenerPorProyectoYTipoFlujoFondo(Long idProyecto, TipoFlujoFondo tipoFlujoFondo) {
        return cuentaRepository.findByProyectoIdAndTipoFlujoFondo(idProyecto, tipoFlujoFondo);
    }

    public List<Cuenta> obtenerPorProyectoYTipoFlujoFondoYTipoBalance(Long idProyecto, TipoFlujoFondo tipoFlujoFondo, TipoBalance tipoBalance) {
        return cuentaRepository.findByProyectoIdAndTipoFlujoFondoAndTipoBalance(idProyecto, tipoFlujoFondo, tipoBalance);
    }

    public List<Cuenta> obtenerPorProyectoYTipoBalance( Long idProyecto, TipoBalance tipoBalance) {
        return cuentaRepository.findByProyectoIdAndTipoBalance(idProyecto, tipoBalance);
    }
    
    public List<Cuenta> obtenerPorProyectoYTipoCuenta(Long idProyecto, TipoCuenta tipoCuenta) {
        return cuentaRepository.findByProyectoIdAndTipoCuenta(idProyecto, tipoCuenta);
    }

    public List<Cuenta> obtenerPorProyectoYTipoCuentaYTipoTransaccion(Long idProyecto, TipoCuenta tipoCuenta, TipoTransaccion tipoTransaccion) {
        return cuentaRepository.findByProyectoIdAndTipoCuentaAndTipoTransaccion(idProyecto, tipoCuenta, tipoTransaccion);
    }
    public CuentaPeriodo crearCuentaFinancieraPeriodo(Integer periodo, BigDecimal montoPeriodo, Cuenta cuentaFinanciera) {
        CuentaPeriodo cuentaPeriodo = CuentaPeriodo.builder()
                .cuenta(cuentaFinanciera)
                .monto(montoPeriodo)
                .periodo(periodo)
                .build();
        cuentaPeriodo = cuentaPeriodoRepository.save(cuentaPeriodo);
        cuentaFinanciera.agregarCuenta(cuentaPeriodo);
        cuentaRepository.save(cuentaFinanciera);
        return cuentaPeriodo;
    }

    public Cuenta crearCuentaFinanciera(Long idProyecto, String desc, TipoFlujoFondo tipoFlujoFondo, TipoBalance tipoBalance, TipoTransaccion tipoTransaccion) {
        Cuenta cuenta = Cuenta.builder()
                .descripcion(desc)
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoBalance(tipoBalance)
                .tipoFlujoFondo(tipoFlujoFondo)
                .proyectoId(idProyecto)
                .tipoTransaccion(tipoTransaccion)
                .build();
        return cuentaRepository.save(cuenta);
    }

    public void crearCuentaEconomica(Long idProyecto, Integer periodo, String desc, BigDecimal montoPeriodo, TipoTransaccion tipoTransaccion) {
        CuentaPeriodo cuentaPeriodo = new CuentaPeriodo();
        List<CuentaPeriodo> cuentasPeriodos = new ArrayList<>();

        Cuenta cuentaEconomica = Cuenta.builder()
                .descripcion(desc)
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .proyectoId(idProyecto)
                .tipoTransaccion(tipoTransaccion)
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
                if ((cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS)
                        || cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS))) {
                    estado.setCaja(estado.getCaja().subtract(cuentaPeriodo.getMonto()));
                }
            }
        }
        return estado;
    }

    public void crearPorBalanceInicial(Long proyectoId) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId).get();

        Balance balanceInicial = proyecto.getEscenario().getBalanceInicial();

        crearCuentasFinancierasCobroClientes(balanceInicial, proyectoId);
        crearCuentasFinancierasPagoProveedores(balanceInicial, proyectoId);
        crearCuentasFinancierasPagoBancos(balanceInicial, proyectoId);

    }

    private void crearCuentasFinancierasCobroClientes(Balance balanceInicial, Long proyectoId) {
        if (balanceInicial.getActivo().getCuentasPorCobrarPeriodos() > 0) {
            BigDecimal cuentasPorCobrarPorPeriodo = balanceInicial.getActivo().getCuentasPorCobrar()
                    .divide(new BigDecimal(balanceInicial.getActivo().getCuentasPorCobrarPeriodos()), RoundingMode.HALF_DOWN);

            Cuenta cuentaFinancieraCobroClientes = crearCuentaFinanciera(proyectoId, "Cuentas a Cobrar", TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS, TipoBalance.CREDITO_CLIENTES, TipoTransaccion.OTROS);

            for (int i = 1; i <= balanceInicial.getActivo().getCuentasPorCobrarPeriodos(); i++) {
                crearCuentaFinancieraPeriodo(i, cuentasPorCobrarPorPeriodo, cuentaFinancieraCobroClientes);
            }
        }
    }

    private void crearCuentasFinancierasPagoProveedores(Balance balanceInicial, Long proyectoId) {
        if (balanceInicial.getPasivo().getProveedoresPeriodos() > 0) {
            BigDecimal proveedoresPorPorPeriodo = balanceInicial.getPasivo().getProveedores()
                    .divide(new BigDecimal(balanceInicial.getPasivo().getProveedoresPeriodos()), RoundingMode.HALF_DOWN);

            Cuenta cuentaFinancieraPagoProveedores = crearCuentaFinanciera(proyectoId, "Proveedores", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS, TipoBalance.DEUDA_PROVEEDORES, TipoTransaccion.OTROS);
           
            for (int i = 1; i <= balanceInicial.getPasivo().getProveedoresPeriodos(); i++) {
                crearCuentaFinancieraPeriodo(i, proveedoresPorPorPeriodo, cuentaFinancieraPagoProveedores);
            }
        }

    }

    private void crearCuentasFinancierasPagoBancos(Balance balanceInicial, Long proyectoId) {
        if (balanceInicial.getPasivo().getDeudasBancariasPeriodos() > 0) {
            BigDecimal deudaBancariaPorPeriodo = balanceInicial.getPasivo().getDeudasBancarias()
                    .divide(new BigDecimal(balanceInicial.getPasivo().getDeudasBancariasPeriodos()), RoundingMode.HALF_DOWN);

            Cuenta cuentaFinancieraPagoDeudasBancarias = crearCuentaFinanciera(proyectoId, "Deudas Bancarias", TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS, TipoBalance.DEUDA_BANCARIA, TipoTransaccion.OTROS);

            for (int i = 1; i <= balanceInicial.getPasivo().getDeudasBancariasPeriodos(); i++) {
                crearCuentaFinancieraPeriodo(i, deudaBancariaPorPeriodo, cuentaFinancieraPagoDeudasBancarias);
            }
        }
    }

}
