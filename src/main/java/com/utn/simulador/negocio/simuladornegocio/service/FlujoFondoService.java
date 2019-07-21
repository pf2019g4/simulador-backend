package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.vo.AgrupadorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlujoFondoService {

    private final CuentaService cuentaService;

    public Map<String, AgrupadorVo> calcular(Long idProyecto, Integer cantidadPeriodos){

        List<Cuenta> cuentasIAI = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS);
        List<Cuenta> cuentasEAI = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS);
        List<Cuenta> cuentasGND = cuentaService.obtenerPorProyectoYTipoFlujoFondo(idProyecto, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES);

        List<CuentaPeriodo> cuentasPeriodoUNAI = new ArrayList<>();


        Map<String, AgrupadorVo> hashMap = new HashMap<>();




        return null;
    }

}
