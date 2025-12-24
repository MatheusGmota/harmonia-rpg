package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.AtributoResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Atributos;
import br.com.api.harmonia_rpg.service.AtributoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/atributos")
@CrossOrigin("*")
public class AtributoController {

    @Autowired
    private AtributoService service;

    @GetMapping("/pericias")
    public ResponseEntity<AtributoResponseDTO> getById(@RequestParam("id-ficha") String idFicha) {
        AtributoResponseDTO response = service.obterPericias(idFicha);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pericias")
    public ResponseEntity<AtributoResponseDTO> create(
            @RequestParam("id-ficha") String idFicha,
            @RequestBody Atributos atributos
    ) {
        AtributoResponseDTO response = service.criarAtributos(idFicha, atributos);
        return ResponseEntity.ok(response);
    }
}
