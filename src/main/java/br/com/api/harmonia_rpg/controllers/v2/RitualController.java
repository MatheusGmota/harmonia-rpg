package br.com.api.harmonia_rpg.controllers.v2;


import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.service.interfaces.RitualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/ficha/{idFicha}/rituais")
public class RitualController {

    @Autowired
    private RitualService service;

    @GetMapping()
    public ResponseEntity<List<Ritual>> get(
            @PathVariable String idFicha) throws Exception {
        return ResponseEntity.ok(service.get(idFicha));
    };

    @PostMapping
    public ResponseEntity<Ritual> create(
            @PathVariable String idFicha,
            @RequestBody Ritual ritual) throws Exception {

        return ResponseEntity.ok(service.create(idFicha, ritual));
    }
}
