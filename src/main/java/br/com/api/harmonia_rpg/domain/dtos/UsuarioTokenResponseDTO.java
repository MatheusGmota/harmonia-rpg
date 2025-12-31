package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.enums.TipoUsuario;
import com.google.cloud.Timestamp;

public record UsuarioTokenResponseDTO(
        String id,
        String nomeUsuario,
        TipoUsuario tipoUsuario,
        String token,
        Timestamp criadoEm) {
}
