package br.com.api.harmonia_rpg.controllers.v2;


import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.service.interfaces.RitualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PatchMapping
    public ResponseEntity<Void> patch(
            @PathVariable String idFicha,
            @RequestParam("id-ritual") String idRitual,
            @RequestBody Map<String, Object> updates) {

        service.partialUpdate(idFicha, idRitual, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @PathVariable String idFicha,
            @RequestParam("id-ritual") String idRitual) {
        service.delete(idFicha, idRitual);
        return ResponseEntity.ok().build();
    }
}
