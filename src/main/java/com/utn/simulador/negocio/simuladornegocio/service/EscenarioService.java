package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Usuario;
import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import com.utn.simulador.negocio.simuladornegocio.domain.CursoEscenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Proveedor;
import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import com.utn.simulador.negocio.simuladornegocio.domain.Consecuencia;
import com.utn.simulador.negocio.simuladornegocio.domain.Financiacion;
import com.utn.simulador.negocio.simuladornegocio.repository.CursoEscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.UsuarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.DecisionRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.FinanciacionRepository;
import com.utn.simulador.negocio.simuladornegocio.bo.CursoEscenarioBo;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EscenarioService {

    private final EscenarioRepository escenarioRepository;
    private final CursoEscenarioRepository cursoEscenarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DecisionRepository decisionRepository;
    private final FinanciacionRepository financiacionRepository;
    
    @Autowired
    protected EntityManager em;

    public List<Escenario> getEscenarios() {
        return escenarioRepository.findAll();
    }
    
    public List<Escenario> getEscenariosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        
        if(usuario.getCurso() == null) {
            return new ArrayList<>();
        }
        
        return cursoEscenarioRepository.findByCursoId(usuario.getCurso().getId()).stream().map(ce -> ce.getEscenario()).collect(Collectors.toList());
    }
    
    public List<Curso> getCursosEscenario(Long escenarioId) {
        return cursoEscenarioRepository.findByEscenarioId(escenarioId).stream().map(ce -> ce.getCurso()).collect(Collectors.toList());
    }
    
    public void setCursosEscenario(Long escenarioId, List<Curso> cursos) {
        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));
        
        List<Long> idCursos = cursos.stream().map(c -> c.getId()).collect(Collectors.toList());
        List<CursoEscenario> cursosEscenarioBase = cursoEscenarioRepository.findByEscenarioId(escenarioId);
        List<Long> idCursosBase = cursosEscenarioBase.stream().map(c -> c.getCurso().getId()).collect(Collectors.toList());
        
        for(CursoEscenario cursoEscenario : cursosEscenarioBase.stream().filter(c -> !idCursos.contains(c.getCurso().getId())).collect(Collectors.toList())) {
            cursoEscenarioRepository.delete(cursoEscenario);
        }
        for(Curso curso : cursos.stream().filter(c -> !idCursosBase.contains(c.getId())).collect(Collectors.toList())) {
            cursoEscenarioRepository.save(new CursoEscenario(curso, escenario, true));
        }
    }
    
    public Escenario getEscenarioById(Long id) {
        return escenarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));
    }
    
    public Escenario createEscenario(Escenario escenario) {
    	return escenarioRepository.save(escenario);
    }
    
    public Escenario modifyEscenario(Escenario escenario) {
    	Escenario escenarioToUpdate = escenarioRepository.getOne(escenario.getId());
    	escenarioToUpdate.setTitulo(escenario.getTitulo());
        escenarioToUpdate.setDescripcion(escenario.getDescripcion());
    	escenarioToUpdate.setMaximosPeriodos(escenario.getMaximosPeriodos());
        escenarioToUpdate.setNombrePeriodos(escenario.getNombrePeriodos());
    	escenarioToUpdate.setImpuestoPorcentaje(escenario.getImpuestoPorcentaje());
        escenarioToUpdate.setEstadoInicial(escenario.getEstadoInicial());
    	escenarioToUpdate.setBalanceInicial(escenario.getBalanceInicial());
    	return escenarioRepository.save(escenarioToUpdate);
    }
    
    public void deleteEscenarioById(Long id) {
    	escenarioRepository.deleteById(id);
    }
    
    public CursoEscenarioBo getDetalleEscenarioJugadoresPorCurso(Long id, Long cursoId) {
        CursoEscenarioBo cursoEscenarioBo = new CursoEscenarioBo(cursoEscenarioRepository.findByCursoIdAndEscenarioId(cursoId, id));
        cursoEscenarioBo.setJugadores(escenarioRepository.getDetalleEscenarioJugadoresPorCurso(id, cursoId));
        return cursoEscenarioBo;
    }
    
    public void cerrarCursoEscenario(Long cursoId, Long id) {
        CursoEscenario cursoEscenario = cursoEscenarioRepository.findByCursoIdAndEscenarioId(cursoId, id);
        cursoEscenario.setAbierto(Boolean.FALSE);
        cursoEscenarioRepository.save(cursoEscenario);
    }
    
    public Escenario duplicateEscenario(Escenario escenario) {
        Long idOriginal = escenario.getId();
        Escenario escenarioCopia = new Escenario();
        escenarioCopia.setTitulo("");
        escenarioCopia.setDescripcion("");
        escenarioCopia.setImpuestoPorcentaje(0d);
        escenarioCopia.setMaximosPeriodos(0);
        escenarioCopia = escenarioRepository.save(escenarioCopia);
        escenario.setId(escenarioCopia.getId());
        escenario.setTitulo(escenario.getTitulo().concat(" - Copia"));
        for(Proveedor proveedor : escenario.getProveedores()) {
            proveedor.setId(null);
            proveedor.setEscenarioId(escenario.getId());
            for(ModalidadPago modalidadPago : proveedor.getModalidadPago()) {
                modalidadPago.setId(null);
                modalidadPago.setProveedor(proveedor);
            }
        }
        escenario.getBalanceInicial().setId(null);
        List<Decision> decisiones = decisionRepository.findByEscenarioId(idOriginal);
        for(Decision decision : decisiones) {
            decision.setId(null);
            decision.setEscenarioId(escenario.getId());
            for(Opcion opcion : decision.getOpciones()) {
                opcion.setId(null);
                opcion.setDecision(decision);
                for(Consecuencia consecuencia : opcion.getConsecuencias()){
                    consecuencia.setId(null);
                    consecuencia.setOpcion(opcion);
                }
            }
            em.detach(decision);
        }
        decisionRepository.saveAll(decisiones);
        List<Financiacion> financiaciones = financiacionRepository.findByEscenarioId(idOriginal);
        for(Financiacion financiacion : financiaciones) {
            financiacion.setId(null);
            financiacion.setEscenarioId(escenario.getId());
            em.detach(financiacion);
        }
        financiacionRepository.saveAll(financiaciones);
    	return escenarioRepository.save(escenario);
    }

}
