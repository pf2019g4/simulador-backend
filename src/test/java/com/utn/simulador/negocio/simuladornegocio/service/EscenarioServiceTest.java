package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class EscenarioServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private EscenarioService escenarioService;

    @Autowired
    private EscenarioRepository escenarioRepository;

   
    @Test
    public void abmAltaEscenarios() {
        Escenario escenario = EscenarioBuilder.base().build(em);
        
        Escenario escenarioDB = escenarioService.createEscenario(escenario);
        
        assertThat(escenarioRepository.existsById(escenarioDB.getId())).isEqualTo(true);
        assertThat(escenarioDB.getDescripcion()).isEqualTo(escenario.getDescripcion());
        assertThat(escenarioDB.getMaximosPeriodos()).isEqualTo(escenario.getMaximosPeriodos());
        assertThat(escenarioDB.getTitulo()).isEqualTo(escenario.getTitulo());
        assertThat(escenarioDB.getImpuestoPorcentaje()).isEqualTo(escenario.getImpuestoPorcentaje());
    }
    
    @Test
    public void abmEditarEscenarios() {
    	Escenario escenario = EscenarioBuilder.base().build(em);
         
        Escenario escenarioDB = escenarioService.createEscenario(escenario);
        
        assertThat(escenarioRepository.existsById(escenarioDB.getId())).isEqualTo(true);
        assertThat(escenarioDB.getDescripcion()).isEqualTo(escenario.getDescripcion());
        assertThat(escenarioDB.getMaximosPeriodos()).isEqualTo(escenario.getMaximosPeriodos());
        assertThat(escenarioDB.getTitulo()).isEqualTo(escenario.getTitulo());
        assertThat(escenarioDB.getImpuestoPorcentaje()).isEqualTo(escenario.getImpuestoPorcentaje());
        
        escenarioDB.setDescripcion("Editar Descripcion");
        
        Escenario escenarioDB2 = escenarioService.modifyEscenario(escenarioDB);
        assertThat(escenarioRepository.existsById(escenarioDB2.getId())).isEqualTo(true);
        assertThat(escenarioDB2.getDescripcion()).isEqualTo("Editar Descripcion");
    }
    
    @Test
    public void abmBorrarEscenarios() {
    	Escenario escenario = EscenarioBuilder.base().build(em);
        
        Escenario escenarioDB = escenarioService.createEscenario(escenario);
        
        assertThat(escenarioRepository.existsById(escenarioDB.getId())).isEqualTo(true);
        
        escenarioService.deleteEscenarioById(escenarioDB.getId());
        
        assertThat(!escenarioRepository.existsById(escenarioDB.getId())).isEqualTo(true);
    }

}
