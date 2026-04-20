package br.com.api.harmonia_rpg.controllers.v2;

import br.com.api.harmonia_rpg.domain.dtos.ConviteDTO;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import br.com.api.harmonia_rpg.service.interfaces.CampanhaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v2/convites")
public class ConviteController {

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
            summary = "Gera ou reutiliza link de convite",
            description = """
            Gera (ou reutiliza) o link de convite da campanha.
            - O link pode ser compartilhado com qualquer pessoa
            - Apenas o Mestre pode executar esta ação
        """
    )
    @PostMapping("{id}/convites/link")
    public ResponseEntity<ConviteDTO.ResponseDTO> gerarLink(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campanhaService.gerarLinkConvite(getUsuarioLogado().getId(), id));
    }

    @Operation(
            summary = "Entrar na campanha via token",
            description = """
            Jogador autenticado usa o token do link para entrar na campanha.
            - Não requer payload
            - Apenas estar autenticado
        """
    )
    @PostMapping("convites/entrar/{token}")
    public ResponseEntity<ConviteDTO.AceitarResponseDTO> entrarPorToken(@PathVariable String token) {
        return ResponseEntity.ok(
                campanhaService.aceitarPorToken(getUsuarioLogado().getId(), token)
        );
    }

    @Operation(
            summary = "Revoga links de convite",
            description = """
            Invalida todos os links de convite ativos da campanha.
            - Ação exclusiva do Mestre
        """
    )
    @DeleteMapping("{id}/convites/link")
    public ResponseEntity<Map<String, Object>> revogarLink(@PathVariable String id) {
        return ResponseEntity.ok(
                campanhaService.revogarLinkConvite(getUsuarioLogado().getId(), id)
        );
    }
}
