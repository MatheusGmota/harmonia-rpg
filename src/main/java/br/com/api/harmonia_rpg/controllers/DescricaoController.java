package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Descricao;
import br.com.api.harmonia_rpg.service.DescricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ficha/{idFicha}/descricao")
public class DescricaoController {

    @Autowired
    private DescricaoService service;

    @GetMapping
    public ResponseEntity<Object> getById(@PathVariable String idFicha) {
        DescricaoResponseDTO response = service.obterPorId(idFicha);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @PathVariable String idFicha,
            @RequestBody Descricao descricao
    ) {
        DescricaoResponseDTO response = service.criar(idFicha, descricao);
        return ResponseEntity.ok(response);
    }

}
