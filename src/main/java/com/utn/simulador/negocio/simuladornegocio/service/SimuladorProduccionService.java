package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
class SimuladorProduccionService {

    Estado simular(Estado estado) {

        aumentarStock(estado);
        imputarGastosProduccion(estado);

        return estado;
    }

    private void imputarGastosProduccion(Estado estado) {
        estado.setCaja(estado.getCaja().subtract(estado.getCostoFijo()).subtract(estado.getCostoVariable().multiply(new BigDecimal(estado.getProduccionMensual()))));
    }

    private void aumentarStock(Estado estado) {
        estado.setStock(estado.getStock() + estado.getProduccionMensual());
    }

}
