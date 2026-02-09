package br.com.api.harmonia_rpg.domain.dtos;

public record AtaqueEditRequestDTO(
        int index,
        String nome,
        String teste,
        String dano,

        int critico,
        String alcance,
        String especial
) {
}
