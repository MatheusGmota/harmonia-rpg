package br.com.api.harmonia_rpg.domain.dtos;

public class DescricaoDTO {
    public record DescricaoResponseDTO(
            String id,
            String aparencia,
            String personalidade,
            String historico,
            String objetivo
    ){}

    public record DescricaoRequestDTO(
            String aparencia,
            String personalidade,
            String historico,
            String objetivo
    ) {}
}
