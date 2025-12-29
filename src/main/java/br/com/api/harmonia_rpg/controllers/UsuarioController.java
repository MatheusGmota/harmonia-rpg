package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.UsuarioRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.UsuarioResponseDTO;
import br.com.api.harmonia_rpg.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") String id) {
        UsuarioResponseDTO resposta = service.obterUsuarioPorId(id);
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        Map<String, Object> resposta = service.editarUsuario(id, token,dto);
        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> updates) {
        Map<String, Object> resposta = service.editarParcialUsuario(id, token, updates);
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        Map<String, Object> resposta = service.deletarUsuario(id, token);
        return ResponseEntity.ok(resposta);
    }
}
