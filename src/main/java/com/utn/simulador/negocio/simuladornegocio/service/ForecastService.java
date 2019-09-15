package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import com.utn.simulador.negocio.simuladornegocio.repository.ForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final ForecastRepository forecastRepository;

    public void guardar(List<Forecast> listaCompletaForecast){
        forecastRepository.saveAll(listaCompletaForecast);
    }

    public void eliminarViejoForecast(Long idProyecto){
        forecastRepository.deleteByProyectoId(idProyecto);
    }

    public Forecast obtenerPorProyectoYPeriodo(Long idProyecto, Integer periodo){
        return forecastRepository.findByProyectoIdAndPeriodo(idProyecto, periodo);
    }
    
    public List<Forecast> obtenerPorProyecto(Long idProyecto){
        return forecastRepository.findByProyectoId(idProyecto);
    }

}
