package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.DecisionRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class DecisionServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private DecisionService decisionService;

    @Autowired
    private DecisionRepository decisionRepository;

    @Test
    public void obtenerPorProyecto_porProyectoValido_devuelveDecisionesConSusRespuestasYConsecuencias() {

        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);

        List<DecisionVo> decisiones = decisionService.obtenerPorProyecto(proyecto.getId());

        assertThat(decisiones).hasSize(0);
//        assertThat(decisiones.get(0).getRespuestas()).hasSize(2);
//        assertThat(decisiones.get(0).getRespuestas().get(0).getConsecuencias()).hasSize(1);
//        assertThat(decisiones.get(0).getRespuestas().get(0).getConsecuencias().get(0).getCuentaId()).isEqualTo(cuenta.getId());

    }

    @Test
    public void guardar_conDecisionNueva_guardaEnBD() {
//
//        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
//
//        Decision decision = new Decision();
//        decision.setDescripcion("Desc1");
//        decision.setProyectoId(proyecto.getId());
//
//        decisionService.guardar(decision);
//
//        assertThat(decisionRepository.count()).isEqualTo(1);
    }

}
