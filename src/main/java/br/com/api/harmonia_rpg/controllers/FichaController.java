package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.FichaRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.FichaResponseDTO;
import br.com.api.harmonia_rpg.domain.dtos.FichaUsuarioDTO;
import br.com.api.harmonia_rpg.service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ficha")
@CrossOrigin("*")
public class FichaController {

    @Autowired
    private FichaService service;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") String id) {
        FichaResponseDTO resposta = service.obterFicha(id);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/usuario")
    public ResponseEntity<Object> getByUser(
            @RequestParam("id-usuario") String idUsuario,
            @RequestParam(value = "nome-campanha", required = false) String nomeCampanha
    ) {
        List<FichaUsuarioDTO> resposta = service.obterFichasDoUsuario(idUsuario, nomeCampanha);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestParam("id-usuario") String idUsuario,
            @RequestBody FichaRequestDTO ficha) {
        FichaResponseDTO resposta = service.criarFicha(idUsuario, ficha);
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable("id") String id,
            @RequestParam("id-usuario") String idUsuario,
            @RequestBody FichaRequestDTO ficha) {
        Object resposta = service.editarFicha(id, idUsuario, ficha);
        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchFicha(
            @PathVariable String id,
            @RequestParam("id-usuario") String idUsuario,
            @RequestBody Map<String, Object> updates) {

        service.patchFicha(id, idUsuario, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(
            @PathVariable("id") String id,
            @RequestParam("id-usuario") String idUsuario) {
        Map<String, Object> resposta = service.deletarFicha(id, idUsuario);
        return ResponseEntity.ok(resposta);
    }
}
