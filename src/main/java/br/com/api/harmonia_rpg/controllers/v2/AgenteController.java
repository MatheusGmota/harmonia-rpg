package br.com.api.harmonia_rpg.controllers.v2;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.service.interfaces.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/ficha")
public class AgenteController {

    @Autowired
    private AgenteService service;

    @GetMapping("/{idFicha}")
    public ResponseEntity<AgenteDTO.ResponseDTO> get(
            @RequestParam("id-usuario") String idUsuario,
            @PathVariable String idFicha) throws Exception {
        return ResponseEntity.ok(service.obter(idUsuario, idFicha));
    };

    @GetMapping("/")
    public ResponseEntity<List<AgenteDTO.ResponseDTO>> getAllByUser(
            @RequestParam(value = "id-usuario") String idUsuario) throws Exception {
        return ResponseEntity.ok(service.obterAgentesDoUsuario(idUsuario));
    };

    @PostMapping("/{idFicha}")
    public ResponseEntity<AgenteDTO.ResponseDTO> create(
            @RequestParam("id-usuario") String idUsuario,
            @RequestBody AgenteDTO.RequestDTO agente) throws Exception {

        return ResponseEntity.ok(service.criar(idUsuario, agente));
    }

    @PatchMapping("/{idFicha}")
    public ResponseEntity<Map<String, Object>> patch(
            @RequestParam("id-usuario") String idUsuario,
            @PathVariable String idFicha,
            @RequestBody Map<String, Object> updates) throws Exception {
        return ResponseEntity.ok(service.editar(idUsuario, idFicha, updates));
    }

    @DeleteMapping("/{idFicha}")
    public ResponseEntity<Map<String, Object>> delete(
            @RequestParam("id-usuario") String idUsuario,
            @PathVariable String idFicha) throws Exception {
        return ResponseEntity.ok(service.deletar(idUsuario, idFicha));
    };
}
