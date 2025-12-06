package br.com.api.harmonia_rpg.controllers;

import br.com.api.harmonia_rpg.domain.dtos.FichaUsuarioDTO;
import br.com.api.harmonia_rpg.domain.entities.Ficha;
import br.com.api.harmonia_rpg.service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ficha")
@CrossOrigin("*")
public class FichaController {

    @Autowired
    private FichaService service;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") String id) {
        Ficha res = service.obterFicha(id);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma ficha encontrada");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/usuario")
    public ResponseEntity<Object> getByUser(@RequestParam("id-usuario") String idUsuario) {
        List<FichaUsuarioDTO> res = service.obterFichasDoUsuario(idUsuario);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma ficha encontrada");
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestParam("id-usuario") String idUsuario, @RequestBody Ficha ficha) {
        Object res = service.criarFicha(idUsuario, ficha);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Ficha ficha) {
        Object res = service.editarFicha(id, ficha);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma ficha encontrada");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        Object res = service.deletarFicha(id);
        if (res == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma ficha encontrada");
        return ResponseEntity.ok(res);
    }
}
