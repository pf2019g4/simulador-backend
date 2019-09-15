package com.utn.simulador.negocio.simuladornegocio.repository;

import com.utn.simulador.negocio.simuladornegocio.domain.Consecuencia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ConsecuenciaRepository extends JpaRepository<Consecuencia, Long> {

    @Transactional
    List<Consecuencia> getByOpcionId(Long opcionId);
}
