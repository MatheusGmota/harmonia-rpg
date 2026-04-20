package br.com.api.harmonia_rpg.controllers.v2;

import br.com.api.harmonia_rpg.domain.dtos.CampanhaDTO;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import br.com.api.harmonia_rpg.service.interfaces.CampanhaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v2/campanhas")
public class CampanhaController {

    @Autowired
    private CampanhaService campanhaService;

    private Usuario getUsuarioLogado() {
        try {
            return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Acesso negado.");
        }
    }

    @Operation(
            summary = "Criar campanha",
            description = "Cria uma campanha e define o usuário autenticado como Mestre."
    )
    @PostMapping
    public ResponseEntity<CampanhaDTO.ResponseDTO> criar(
            @RequestBody @Valid CampanhaDTO.RequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campanhaService.criar(getUsuarioLogado().getId(), dto));
    }

    @Operation(
            summary = "Obter campanha por ID",
            description = """
            Retorna os dados de uma campanha.
            - Acesso permitido apenas para Mestre ou jogadores da campanha
        """
    )
    @GetMapping("/{id}")
    public ResponseEntity<CampanhaDTO.ResponseDTO> obter(@PathVariable String id) {
        return ResponseEntity.ok(
                campanhaService.obter(getUsuarioLogado().getId(), id)
        );
    }

    @Operation(
            summary = "Obter fichas dentro da campanha",
            description = """
            Retorna as fichas da campanha.
            - Mestre vê todas; Jogador vê apenas as próprias
        """
    )
    @GetMapping("/{id}/agentes")
    public ResponseEntity<List<CampanhaDTO.AgenteNaCampanhaDTO>> obterAgentes(@PathVariable String id) {
        return ResponseEntity.ok(campanhaService.obterAgentesDaCampanha(getUsuarioLogado().getId(), id));
    }

    @Operation(
            summary = "Listar minhas campanhas",
            description = """
            Lista todas as campanhas em que o usuário participa.
            - Inclui campanhas como Mestre e como Jogador
        """
    )
    @GetMapping("/minhas")
    public ResponseEntity<List<CampanhaDTO.ResponseDTO>> obterMinhas() {
        return ResponseEntity.ok(
                campanhaService.obterMinhas(getUsuarioLogado().getId())
        );
    }

    @Operation(
            summary = "Editar campanha",
            description = """
            Atualiza os dados da campanha.
            Campos possíveis:
            - nome
            - descricao
            - imagemCapaUrl
            - status
            
            - Exclusivo do Mestre
        """
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editar(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {

        return ResponseEntity.ok(
                campanhaService.editar(getUsuarioLogado().getId(), id, updates)
        );
    }

    @Operation(
            summary = "Deletar campanha",
            description = """
            Remove permanentemente uma campanha.
            - Ação irreversível
            - Exclusivo do Mestre
        """
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable String id) {
        return ResponseEntity.ok(
                campanhaService.deletar(getUsuarioLogado().getId(), id)
        );
    }

    // ========================= AGENTES =========================

    @Operation(
            summary = "Adicionar agente à campanha",
            description = """
            Vincula uma ficha (Agente) à campanha.
            - Mestre pode adicionar qualquer ficha
            - Jogador pode adicionar apenas as próprias
        """
    )
    @PostMapping("/{id}/agentes/{idAgente}")
    public ResponseEntity<CampanhaDTO.ResponseDTO> adicionarAgente(
            @PathVariable String id,
            @PathVariable String idAgente) {

        return ResponseEntity.ok(
                campanhaService.adicionarAgente(getUsuarioLogado().getId(), id, idAgente)
        );
    }

    @Operation(
            summary = "Editar agente da campanha",
            description = """
            Atualiza dados de uma ficha vinculada.
            - Mestre pode editar qualquer ficha
            - Jogador apenas as próprias
        """
    )
    @PatchMapping("/{id}/agentes/{idAgente}")
    public ResponseEntity<Map<String, Object>> editarAgente(
            @PathVariable String id,
            @PathVariable String idAgente,
            @RequestBody Map<String, Object> updates) {

        return ResponseEntity.ok(
                campanhaService.editarAgente(getUsuarioLogado().getId(), id, idAgente, updates)
        );
    }

    @Operation(
            summary = "Remover agente da campanha",
            description = """
            Remove o vínculo da ficha com a campanha.
            - Não deleta a ficha
            - Mestre pode remover qualquer ficha
            - Jogador apenas as próprias
        """
    )
    @DeleteMapping("/{id}/agentes/{idAgente}")
    public ResponseEntity<Map<String, Object>> removerAgente(
            @PathVariable String id,
            @PathVariable String idAgente) {

        return ResponseEntity.ok(
                campanhaService.removerAgente(getUsuarioLogado().getId(), id, idAgente)
        );
    }

    // ========================= JOGADORES =========================

    @Operation(
            summary = "Remover jogador da campanha",
            description = """
            Remove um jogador da campanha.
            - Exclusivo do Mestre
        """
    )
    @DeleteMapping("/{id}/jogadores/{idJogador}")
    public ResponseEntity<Map<String, Object>> removerJogador(
            @PathVariable String id,
            @PathVariable String idJogador) {

        return ResponseEntity.ok(
                campanhaService.removerJogador(getUsuarioLogado().getId(), id, idJogador)
        );
    }
}