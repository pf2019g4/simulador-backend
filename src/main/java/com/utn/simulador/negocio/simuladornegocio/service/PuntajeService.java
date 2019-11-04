package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionPuntaje;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.PuntajeProyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.PonderacionPuntajeRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.PuntajeProyectoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuntajeService {
    
    private static final BigDecimal PUNTAJE_ESCENARIO_BASE = new BigDecimal(500);

    private final EstadoService estadoService;
    private final BalanceService balanceService;
    private final ProyectoService proyectoService;
    
    private final EscenarioRepository escenarioRepository;
    private final PonderacionPuntajeRepository ponderacionPuntajeRepository;
    private final PuntajeProyectoRepository puntajeProyectoRepository;

    public PonderacionPuntaje obtenerPuntajeEscenario(Long escenarioId) {
        
        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            return ponderacionPuntajeRepository.findByEscenarioId(escenarioId);
        }
        return null;
    }
    
    public PonderacionPuntaje cargarPuntajeEscenario(Long escenarioId, PonderacionPuntaje ponderacionPuntaje) {

        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            return ponderacionPuntajeRepository.save(ponderacionPuntaje);
        }
        return null;
    }
    
    public void calcularPuntajesProyecto(Long proyectoId) {
        List<Estado> estados = estadoService.obtenerPorProyecto(proyectoId, false);
        
        PuntajeProyecto puntajeProyecto = new PuntajeProyecto();
        
        puntajeProyecto.setProyectoId(proyectoId);
        puntajeProyecto.setCajaFinal(estados.stream().filter(estado -> estado.getActivo() == true).findFirst().get().getCaja());
        puntajeProyecto.setVentasTotales(estados.stream().map(estado -> estado.getVentas() != null ? estado.getVentas() : BigDecimal.ZERO).reduce(BigDecimal.ZERO,BigDecimal::add));
        puntajeProyecto.setRenta(balanceService.obtenerPorProyecto(proyectoId, false).getPatrimonioNeto().getResultadoDelEjercicio());
        
        if(puntajeProyecto.getCajaFinal().compareTo(BigDecimal.ZERO) < 0) puntajeProyecto.setCajaFinal(BigDecimal.ZERO);
        if(puntajeProyecto.getRenta().compareTo(BigDecimal.ZERO) < 0) puntajeProyecto.setRenta(BigDecimal.ZERO);
        
        puntajeProyectoRepository.save(puntajeProyecto);
    }
    
    public void calcularPuntajesTotales(Long cursoId, Long escenarioId) {
        
        PonderacionPuntaje ponderacionPuntaje = ponderacionPuntajeRepository.findByEscenarioId(escenarioId);
        List<Proyecto> proyectosEntregados = proyectoService.obtenerEntregadosPorCursoYEscenario(cursoId, escenarioId);
        
        List<PuntajeProyecto> puntajesProyecto = proyectosEntregados.stream().map(p -> puntajeProyectoRepository.findByProyectoId(p.getId())).collect(Collectors.toList());
                
        BigDecimal cajaMax = puntajesProyecto.stream().map(p -> p.getCajaFinal()).max(BigDecimal::compareTo).get();
        BigDecimal ventasMax = puntajesProyecto.stream().map(p -> p.getVentasTotales()).max(BigDecimal::compareTo).get();
        BigDecimal rentaMax = puntajesProyecto.stream().map(p -> p.getRenta()).max(BigDecimal::compareTo).get();
                
        for(PuntajeProyecto puntajeProyecto : puntajesProyecto) {
            BigDecimal porcentajeTotal = BigDecimal.ZERO;
            
            porcentajeTotal = porcentajeTotal.add(puntajeProyecto.getCajaFinal().multiply(ponderacionPuntaje.getPorcentajeCaja()).divide(cajaMax));
            porcentajeTotal = porcentajeTotal.add(puntajeProyecto.getVentasTotales().multiply(ponderacionPuntaje.getPorcentajeVentas()).divide(ventasMax));
            porcentajeTotal = porcentajeTotal.add(puntajeProyecto.getRenta().multiply(ponderacionPuntaje.getPorcentajeRenta()).divide(rentaMax));
            
            BigDecimal puntajePonderado = PUNTAJE_ESCENARIO_BASE.add((PUNTAJE_ESCENARIO_BASE.divide(BigDecimal.valueOf(2)).multiply(ponderacionPuntaje.getPorcentajeEscenario())).divide(BigDecimal.valueOf(100)));
            
            puntajeProyecto.setPuntaje(porcentajeTotal.multiply(puntajePonderado).divide(BigDecimal.valueOf(100)));
            
            puntajeProyectoRepository.save(puntajeProyecto);
        }
        
    }

}
