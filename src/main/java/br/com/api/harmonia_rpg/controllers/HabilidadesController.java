package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.entities.Habilidade;
import br.com.api.harmonia_rpg.service.HabilidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ficha/{idFicha}/habilidades")
@CrossOrigin("*")
public class HabilidadesController {
    @Autowired
    private HabilidadeService service;

    @PostMapping
    public ResponseEntity<List<Habilidade>> create(
            @PathVariable String idFicha,
            @RequestBody Habilidade habilidade) throws Exception {

        return ResponseEntity.ok(service.create(idFicha, habilidade));
    }

    @GetMapping
    public ResponseEntity<List<Habilidade>> get(
            @PathVariable String idFicha) throws Exception {

        return ResponseEntity.ok(service.get(idFicha));
    }
}
