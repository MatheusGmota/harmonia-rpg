package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.Usuario;
import br.com.api.harmonia_rpg.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") String id) {
        Usuario res = service.obterUsuario(id);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado");
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Usuario usuario) {
        Object res = service.criarUsuario(usuario);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Usuario usuario) {
        String res = service.editarUsuario(id, usuario);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        String res = service.deletarUsuario(id);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado");
        return ResponseEntity.ok(res);
    }
}
