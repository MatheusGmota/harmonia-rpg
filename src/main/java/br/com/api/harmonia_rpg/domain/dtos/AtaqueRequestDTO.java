package br.com.api.harmonia_rpg.domain.dtos;

public record AtaqueRequestDTO(
        String nome,
        String teste,
        String dano,

        int critico,
        String alcance,
        String especial
) {
}
