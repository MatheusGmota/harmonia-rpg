package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.service.RitualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ficha/{idFicha}/rituais")
@CrossOrigin("*")
public class RituaisController {

    @Autowired
    private RitualService service;

    @PostMapping
    public ResponseEntity<List<Ritual>> create(
            @PathVariable String idFicha,
            @RequestBody Ritual ritual) throws Exception {

        return ResponseEntity.ok(service.create(idFicha, ritual));
    }

    @GetMapping
    public ResponseEntity<List<Ritual>> get(
            @PathVariable String idFicha) throws Exception {

        return ResponseEntity.ok(service.get(idFicha));
    }
}
