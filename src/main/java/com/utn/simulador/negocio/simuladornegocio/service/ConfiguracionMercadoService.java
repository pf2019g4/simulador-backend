package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.EmpresasCompetidorasRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.MercadoPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.PonderacionMercadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.RestriccionPrecioRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.MercadoEscenarioVo;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfiguracionMercadoService {

    private final EmpresasCompetidorasRepository empresasCompetidorasRepository;
    private final MercadoPeriodoRepository mercadoPeriodoRepository;
    private final PonderacionMercadoRepository ponderacionMercadoRepository;
    private final RestriccionPrecioRepository restriccionPrecioRepository;
    private final EscenarioRepository escenarioRepository;

    public MercadoEscenarioVo obtenerMercadoEscenario(Long escenarioId) {
        
        MercadoEscenarioVo mercadoEscenarioVo = new MercadoEscenarioVo();
        
        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            mercadoEscenarioVo.setEmpresasCompetidoras(empresasCompetidorasRepository.findByEscenarioId(escenarioId));
            mercadoEscenarioVo.setMercadosPeriodo(mercadoPeriodoRepository.findByEscenarioId(escenarioId));
            mercadoEscenarioVo.setPonderacionesMercado(ponderacionMercadoRepository.findByEscenarioId(escenarioId));
            List<RestriccionPrecio> restriccionPrecio = restriccionPrecioRepository.findByEscenarioId(escenarioId);
            if(restriccionPrecio.size() > 0){
                mercadoEscenarioVo.setRestriccionPrecio(restriccionPrecio.get(0));
            }
        }
        
        return mercadoEscenarioVo;
    }
    
    public MercadoEscenarioVo cargarMercadoEscenario(Long escenarioId, MercadoEscenarioVo mercadoEscenarioVo) {

        Escenario escenario = escenarioRepository.findById(escenarioId).orElseThrow(() -> new IllegalArgumentException("Escenario inexistente"));

        if(escenario != null) {
            empresasCompetidorasRepository.saveAll(mercadoEscenarioVo.getEmpresasCompetidoras());
            mercadoPeriodoRepository.saveAll(mercadoEscenarioVo.getMercadosPeriodo());
            ponderacionMercadoRepository.saveAll(mercadoEscenarioVo.getPonderacionesMercado());
            restriccionPrecioRepository.save(mercadoEscenarioVo.getRestriccionPrecio());
        }
        
        return mercadoEscenarioVo;
    }

}
