package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import com.utn.simulador.negocio.simuladornegocio.repository.CursoEscenarioRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.CursoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CursoEscenarioRepository cursoEscenarioRepository;

    public Curso obtenerCurso(Long idCurso) {
        return cursoRepository.findById(idCurso).orElseThrow(() -> new IllegalArgumentException("Curso inexistente"));
    }

    public List<Curso> getCursos() {
        return cursoRepository.findAll();
    }

    public Curso crearCurso(Curso curso) {

        return cursoRepository.save(curso);
    }

    public Curso editarCurso(Long cursoId, Curso curso) {

        Curso cursoToUpdate = cursoRepository.getOne(cursoId);
        cursoToUpdate.setNombre(curso.getNombre());
        cursoToUpdate.setClave(curso.getClave());

        return cursoRepository.save(cursoToUpdate);
    }

    public void borrarCurso(Long cursoId) {
        cursoEscenarioRepository.deleteByCursoId(cursoId);
        cursoRepository.deleteById(cursoId);
    }

}
