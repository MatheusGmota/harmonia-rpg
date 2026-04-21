package br.com.api.harmonia_rpg.controllers.v2;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import br.com.api.harmonia_rpg.service.interfaces.DescricaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("DescricaoControllerV2")
@RequestMapping("api/v2/ficha/{idFicha}/descricao")
public class DescricaoController {
    @Autowired
    private DescricaoService service;

    private Usuario getUsuarioLogado() {
        try {
            return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Acesso negado.");
        }
    }

    @Operation(
            summary = "Obter descrição da ficha",
            description = """
            Retorna a descrição vinculada a uma ficha.
            - Apenas o dono da ficha pode acessar
        """
    )
    @GetMapping
    public ResponseEntity<DescricaoDTO.DescricaoResponseDTO> get(@PathVariable String idFicha) throws Exception {
        return ResponseEntity.ok(service.obter(getUsuarioLogado().getId(), idFicha));
    }

    @Operation(
            summary = "Criar descrição",
            description = """
            Adiciona um novo descrição à ficha.
            - Apenas o dono da ficha pode adicionar
        """
    )
    @PostMapping
    public ResponseEntity<DescricaoDTO.DescricaoResponseDTO> create(
            @PathVariable String idFicha,
            @RequestBody DescricaoDTO.DescricaoRequestDTO descricao) throws Exception {

        return ResponseEntity.ok(service.criar(getUsuarioLogado().getId(), idFicha, descricao));
    }

    @Operation(
            summary = "Editar descrição",
            description = """
            Atualiza parcialmente um descrição da ficha.
            
            - Atualização via PATCH
            - Apenas campos enviados serão alterados
            - Campos inválidos são ignorados
            
            - Apenas o dono da ficha pode editar
        """
    )
    @PatchMapping("/{idDescricao}")
    public ResponseEntity<Map<String, Object>> patch(
            @PathVariable String idFicha,
            @PathVariable String idDescricao,
            @RequestBody Map<String, Object> updates) {

        return ResponseEntity.ok(
                service.editar(getUsuarioLogado().getId(), idFicha, idDescricao, updates)
        );
    }

    @Operation(
            summary = "Remover descrição",
            description = """
            Remove descrição da ficha.
            - Não afeta outros dados da ficha
            - Apenas o dono pode remover
        """
    )
    @DeleteMapping("idDescricao")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable String idFicha,
            @PathVariable String idDescricao
    ) {

        return ResponseEntity.ok(
                service.deletar(getUsuarioLogado().getId(), idFicha, idDescricao)
        );
    }
}
