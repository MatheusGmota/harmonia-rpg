package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.enums.TipoUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nomeUsuario,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha,

        @NotNull(message = "O tipo de usuário (JOGADOR, MESTRE) é obrigatório.")
        TipoUsuario tipoUsuario
) {
}
