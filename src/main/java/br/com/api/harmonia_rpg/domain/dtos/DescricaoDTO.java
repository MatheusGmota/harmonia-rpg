package br.com.api.harmonia_rpg.domain.dtos;

public class DescricaoDTO {
    public record ResponseDTO(
            String id,
            String aparencia,
            String personalidade,
            String historico,
            String objetivo
    ){}

    public record RequestDTO(
            String aparencia,
            String personalidade,
            String historico,
            String objetivo
    ) {}
}
