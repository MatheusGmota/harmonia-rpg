package br.com.api.harmonia_rpg.domain.dtos;

public record DescricaoResponseDTO(
        String aparencia,
        String personalidade,
        String historico,
        String objetivo
) {
}
