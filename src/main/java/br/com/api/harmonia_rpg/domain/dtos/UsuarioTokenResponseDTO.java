package br.com.api.harmonia_rpg.domain.dtos;

import com.google.cloud.Timestamp;

public record UsuarioTokenResponseDTO(
        String id,
        String nomeUsuario,
        String token,
        Timestamp criadoEm) {
}
