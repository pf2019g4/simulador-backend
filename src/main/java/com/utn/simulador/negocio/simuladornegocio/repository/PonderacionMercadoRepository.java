package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionMercado;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoPonderacionMercado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface PonderacionMercadoRepository extends JpaRepository<PonderacionMercado, Long> {

    //TODO este transactional no deberia estar
    @Transactional
    void deleteByEscenarioId(Long escenarioId);

    public List<PonderacionMercado> findByEscenarioId(Long escenarioId);

    public List<PonderacionMercado> findByEscenarioIdAndConceptoOrderByValorDesc(Long id, TipoPonderacionMercado tipoPonderacionMercado);
}
