package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.InventarioRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.InventarioResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Inventario;
import br.com.api.harmonia_rpg.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ficha/{idFicha}/inventario")
@CrossOrigin("*")
public class InventarioController {
    @Autowired
    private InventarioService service;

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> create(
            @PathVariable String idFicha,
            @RequestBody InventarioRequestDTO request) {

        return ResponseEntity.ok(service.create(idFicha, request));
    }

    @GetMapping
    public ResponseEntity<InventarioResponseDTO> get(
            @PathVariable String idFicha) {

        return ResponseEntity.ok(service.get(idFicha));
    }
}
