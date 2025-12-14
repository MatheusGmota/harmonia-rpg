package br.com.api.harmonia_rpg.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String nomeUsuario,

        @NotBlank(message = "A senha é obrigatória.")
        String senha
) {
}
