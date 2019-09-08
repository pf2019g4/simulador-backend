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

    void guardar(List<Forecast> listaCompletaForecast){
        forecastRepository.saveAll(listaCompletaForecast);
    }

    Forecast obtenerPorProyectoYPeriodo(Long idProyecto, Integer periodo){
        return forecastRepository.findByProyectoIdAndPeriodo(idProyecto, periodo);
    }

}
