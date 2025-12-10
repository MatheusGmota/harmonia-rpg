package br.com.api.harmonia_rpg.domain.dtos;

import com.google.cloud.Timestamp;

public record CadastroResponseDTO(
        String id,
        String nomeUsuario,
        Timestamp criadoEm) {
}
