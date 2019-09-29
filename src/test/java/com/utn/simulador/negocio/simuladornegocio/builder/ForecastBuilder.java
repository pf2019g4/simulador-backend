package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;

import java.math.BigDecimal;

public class ForecastBuilder extends AbstractPersistenceBuilder<Forecast> {

    private ForecastBuilder() {
        instance = new Forecast();
    }

    public static ForecastBuilder baseDeProyectoYPeriodo(Proyecto proyecto, Integer periodo) {
        ForecastBuilder forecastBuilder = new ForecastBuilder();

        forecastBuilder.instance.setCantidadUnidades(5000L);
        forecastBuilder.instance.setPeriodo(periodo);
        forecastBuilder.instance.setProyectoId(proyecto.getId());
        forecastBuilder.instance.setPrecio( new BigDecimal(1000));

        return forecastBuilder;
    }
    
    

}
