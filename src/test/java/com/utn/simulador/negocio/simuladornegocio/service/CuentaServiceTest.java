package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CuentaServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    public void obtenerPorProyectoYTipo_conIdProyectoYTipoValido_devuelveCuentas() {
        TipoCuenta tipoCuentaTest = TipoCuenta.FINANCIERO;
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        CuentaBuilder.deProyecto(proyecto, tipoCuentaTest, 1).build(em);
        CuentaBuilder.deProyecto(proyecto, tipoCuentaTest, 2).build(em);

        List<Cuenta> cuentas = cuentaService.obtenerPorProyectoYTipo(proyecto.getId(), tipoCuentaTest);
        assertThat(cuentas).hasSize(2);

    }

    @Test
    public void guardar_conCuentaNueva_guardaEnBD() {
        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Cuenta cuenta = CuentaBuilder.deProyecto(proyecto, TipoCuenta.FINANCIERO, 1).build();
        cuentaService.guardar(cuenta);

        assertThat(cuentaRepository.count()).isEqualTo(1);
    }
}
