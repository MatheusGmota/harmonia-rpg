package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.InventarioRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.InventarioResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Item;
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

    @PostMapping("/itens")
    public ResponseEntity<InventarioResponseDTO> adicionarItem(
            @PathVariable String idFicha,
            @RequestBody Item novoItem) {

        return ResponseEntity.ok(service.adicionarItem(idFicha, novoItem));
    }

    @PutMapping("/itens/{nomeItem}")
    public ResponseEntity<InventarioResponseDTO> editarItem(
            @PathVariable String idFicha,
            @PathVariable String nomeItem,
            @RequestBody Item item) {
        return ResponseEntity.ok(service.editarItem(idFicha, nomeItem, item));
    }

    @DeleteMapping("/itens/{nomeItem}")
    public ResponseEntity<InventarioResponseDTO> removerItem(
            @PathVariable String idFicha,
            @PathVariable String nomeItem) {
        return ResponseEntity.ok(service.removerItem(idFicha, nomeItem));
    }
}
