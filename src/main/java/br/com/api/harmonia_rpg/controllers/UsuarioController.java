package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.CadastroRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.CadastroResponseDTO;
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

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CadastroRequestDTO dto) {
        CadastroResponseDTO resposta = service.registrarUsuario(dto);
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @Valid @RequestBody UsuarioRequestDTO dto) {
        Map<String, Object> resposta = service.editarUsuario(id, dto);
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        Map<String, Object> resposta = service.deletarUsuario(id);
        return ResponseEntity.ok(resposta);
    }
}
