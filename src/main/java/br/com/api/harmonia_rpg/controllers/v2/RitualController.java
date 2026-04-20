package br.com.api.harmonia_rpg.controllers.v2;

import br.com.api.harmonia_rpg.domain.dtos.RitualDTO;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import br.com.api.harmonia_rpg.service.interfaces.RitualService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/ficha/{idFicha}/rituais")
public class RitualController {

    @Autowired
    private RitualService service;

    private Usuario getUsuarioLogado() {
        try {
            return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Acesso negado.");
        }
    }

    @Operation(
            summary = "Listar rituais da ficha",
            description = """
            Retorna todos os rituais vinculados a uma ficha.
            - Apenas o dono da ficha pode acessar
        """
    )
    @GetMapping
    public ResponseEntity<List<RitualDTO.RitualResponseDTO>> get(@PathVariable String idFicha) throws Exception {
        return ResponseEntity.ok(service.obter(getUsuarioLogado().getId(), idFicha));
    }

    @Operation(
            summary = "Criar ritual",
            description = """
            Adiciona um novo ritual à ficha.
            - Apenas o dono da ficha pode adicionar
        """
    )
    @PostMapping
    public ResponseEntity<RitualDTO.RitualResponseDTO> create(
            @PathVariable String idFicha,
            @RequestBody RitualDTO.RitualRequestDTO ritual) throws Exception {

        return ResponseEntity.ok(service.criar(getUsuarioLogado().getId(), idFicha, ritual));
    }

    @Operation(
            summary = "Editar ritual",
            description = """
            Atualiza parcialmente um ritual da ficha.
            
            - Atualização via PATCH
            - Apenas campos enviados serão alterados
            - Campos inválidos são ignorados
            
            - Apenas o dono da ficha pode editar
        """
    )
    @PatchMapping("/{idRitual}")
    public ResponseEntity<Map<String, Object>> patch(
            @PathVariable String idFicha,
            @PathVariable("idRitual") String idRitual,
            @RequestBody Map<String, Object> updates) {

        return ResponseEntity.ok(
                service.editar(getUsuarioLogado().getId(), idFicha, idRitual, updates)
        );
    }

    @Operation(
            summary = "Remover ritual",
            description = """
            Remove um ritual da ficha.
            - Não afeta outros dados da ficha
            - Apenas o dono pode remover
        """
    )
    @DeleteMapping("/{idRitual}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable String idFicha,
            @PathVariable("idRitual") String idRitual) {

        return ResponseEntity.ok(
                service.deletar(getUsuarioLogado().getId(), idFicha, idRitual)
        );
    }
}