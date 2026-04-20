package br.com.api.harmonia_rpg.controllers.v2;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import br.com.api.harmonia_rpg.service.interfaces.AgenteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/ficha")
public class AgenteController {

    @Autowired
    private AgenteService service;

    private Usuario getUsuarioLogado() {
        try {
            return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Acesso negado.");
        }
    }

    @Operation(
            summary = "Obter ficha por ID",
            description = """
            Retorna os dados de uma ficha específica.
            - Apenas o dono da ficha pode acessar
        """
    )
    @GetMapping("/{idFicha}")
    public ResponseEntity<AgenteDTO.ResponseDTO> get(@PathVariable String idFicha) throws Exception {
        return ResponseEntity.ok(service.obter(getUsuarioLogado().getId(), idFicha));
    }

    @Operation(
            summary = "Listar fichas do usuário",
            description = """
            Retorna todas as fichas pertencentes ao usuário autenticado.
        """
    )
    @GetMapping("/")
    public ResponseEntity<List<AgenteDTO.ResponseDTO>> getAllByUser() throws Exception {
        return ResponseEntity.ok(service.obterAgentesDoUsuario(getUsuarioLogado().getId()));
    }

    @Operation(
            summary = "Criar ficha",
            description = """
            Cria uma nova ficha vinculada ao usuário autenticado.
        """
    )
    @PostMapping
    public ResponseEntity<AgenteDTO.ResponseDTO> create(
            @RequestBody AgenteDTO.RequestDTO agente) throws Exception {

        return ResponseEntity.ok(service.criar(getUsuarioLogado().getId(), agente));
    }

    @Operation(
            summary = "Editar ficha",
            description = """
            Atualiza parcialmente os dados da ficha.
            
            Campos são atualizados via PATCH:
            - Apenas campos enviados serão modificados
            - Campos inexistentes são ignorados
            
            - Apenas o dono da ficha pode editar
        """
    )
    @PatchMapping("/{idFicha}")
    public ResponseEntity<Map<String, Object>> patch(
            @PathVariable String idFicha,
            @RequestBody Map<String, Object> updates) throws Exception {

        return ResponseEntity.ok(service.editar(getUsuarioLogado().getId(), idFicha, updates));
    }

    @Operation(
            summary = "Deletar ficha",
            description = """
            Remove permanentemente uma ficha.
            - Ação irreversível
            - Apenas o dono pode deletar
        """
    )
    @DeleteMapping("/{idFicha}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String idFicha) throws Exception {
        return ResponseEntity.ok(service.deletar(getUsuarioLogado().getId(), idFicha));
    }
}