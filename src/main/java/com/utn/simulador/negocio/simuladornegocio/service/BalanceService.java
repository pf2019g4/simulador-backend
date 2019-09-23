package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.vo.BalanceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final EstadoService estadoService;
    private final CuentaService cuentaService;

    public BalanceVo obtenerPorProyecto(Long proyectoId) {
        Estado estado = estadoService.obtenerActual(proyectoId, false);
        Activo activo = new Activo(
                estado.getCaja(),
                sumaProximosPeriodos(cuentaService.obtenerPorProyectoYTipoBalance(proyectoId, TipoBalance.CREDITO_CLIENTES), estado.getPeriodo()),
                estado.getStock(),
                estado.getMaquinarias(),
                estado.getAmortizacionAcumulada()
        );
        Pasivo pasivo = new Pasivo(
                sumaProximosPeriodos(cuentaService.obtenerPorProyectoYTipoBalance(proyectoId, TipoBalance.DEUDA_PROVEEDORES), estado.getPeriodo()),
                sumaProximosPeriodos(cuentaService.obtenerPorProyectoYTipoBalance(proyectoId, TipoBalance.DEUDA_BANCARIA), estado.getPeriodo())
        );
        PatrimonioNeto patrimonioNeto = new PatrimonioNeto();
        return new BalanceVo(activo, pasivo, patrimonioNeto);
    }

    private BigDecimal sumaProximosPeriodos(List<Cuenta> cuentas, Integer periodo) {
        return cuentas
                .stream()
                .map(cuenta -> montoProximosPeriodo(cuenta.getCuentasPeriodo(), periodo))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private BigDecimal montoProximosPeriodo(List<CuentaPeriodo> cuentaPeriodos, Integer periodo) {
        return cuentaPeriodos
                .stream()
                .filter(cuentaPeriodo -> cuentaPeriodo.getPeriodo() > periodo)
                .map(CuentaPeriodo::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
