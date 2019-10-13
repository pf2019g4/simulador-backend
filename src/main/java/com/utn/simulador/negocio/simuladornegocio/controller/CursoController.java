package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Curso;
import com.utn.simulador.negocio.simuladornegocio.service.CursoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class CursoController {

    private final CursoService cursoService;
    
    @GetMapping("/curso/{cursoId}")
    public Curso obtenerCurso(@PathVariable("cursoId") Long cursoId) {
        return cursoService.obtenerCurso(cursoId);
    }
    
    @GetMapping("/curso")
    public List<Curso> getCursos() {
        return cursoService.getCursos();
    }
    
    @PostMapping("/curso")
    public Curso crearCurso(@RequestBody Curso curso) {
        return cursoService.crearCurso(curso);
    }
    
    @PutMapping("/curso/{cursoId}")
    public void editarCurso(@PathVariable("cursoId") Long cursoId, @RequestBody Curso curso) {
        cursoService.editarCurso(cursoId, curso);
    }
    
    @DeleteMapping("/curso/{cursoId}")
    public void borrarCurso(@PathVariable("cursoId") Long cursoId) {
        cursoService.borrarCurso(cursoId);
    }
}
