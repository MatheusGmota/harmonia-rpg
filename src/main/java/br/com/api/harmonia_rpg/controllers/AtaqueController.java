package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.AtaqueRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.AtaqueResponseDTO;
import br.com.api.harmonia_rpg.service.AtaqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ficha/{idFicha}/ataques")
@CrossOrigin("*")
public class AtaqueController {
    @Autowired
    private AtaqueService service;

    @GetMapping
    public ResponseEntity<List<AtaqueResponseDTO>> get(
            @PathVariable String idFicha
    ) {
        return ResponseEntity.ok(service.obter(idFicha));
    }

    @PostMapping
    public ResponseEntity<List<AtaqueResponseDTO>> post(
            @PathVariable String idFicha,
            @RequestBody AtaqueRequestDTO request
    ) {
        return ResponseEntity.ok(service.adicionar(idFicha, request));
    }

    @PutMapping
    public ResponseEntity<List<AtaqueResponseDTO>> put(
            @PathVariable String idFicha,
            int index,
            @RequestBody AtaqueRequestDTO request
    ) {
        return ResponseEntity.ok(service.editar(idFicha, index, request));
    }

    @DeleteMapping
    public ResponseEntity<List<AtaqueResponseDTO>> delete(
            @PathVariable String idFicha,
            int index
    ) {
        return ResponseEntity.ok(service.remover(idFicha, index));
    }
}
