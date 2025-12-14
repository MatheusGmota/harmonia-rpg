package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.CadastroRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.UsuarioTokenResponseDTO;
import br.com.api.harmonia_rpg.domain.dtos.LoginRequestDTO;
import br.com.api.harmonia_rpg.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/entrar")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDTO data) {
        UsuarioTokenResponseDTO login = usuarioService.login(data);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> create(@Valid @RequestBody CadastroRequestDTO dto) {
        UsuarioTokenResponseDTO resposta = usuarioService.registrarUsuario(dto);
        return ResponseEntity.status(201).body(resposta);
    }
}
