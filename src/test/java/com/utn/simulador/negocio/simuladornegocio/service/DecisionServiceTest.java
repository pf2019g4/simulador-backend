package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class DecisionServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private DecisionService decisionService;

    @Test
    public void obtenerPorProyecto_porProyectoValido_devuelveDecisionesConSusRespuestasYConsecuencias() {

        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);

        List<DecisionVo> decisiones = decisionService.obtenerPorProyecto(proyecto.getId());

        assertThat(decisiones).hasSize(0);

    }

}
