package br.com.api.harmonia_rpg.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nomeUsuario,

        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha
) {
}
