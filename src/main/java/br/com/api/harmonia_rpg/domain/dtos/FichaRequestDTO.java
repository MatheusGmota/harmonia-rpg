package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.Pontos;
import br.com.api.harmonia_rpg.domain.enums.TipoClasse;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;
import br.com.api.harmonia_rpg.domain.enums.TipoOrigem;
import br.com.api.harmonia_rpg.domain.enums.TipoTrilha;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FichaRequestDTO(
        // Campos de String
        @NotBlank(message = "O nome da campanha é obrigatório.")
        String nomeCampanha,

        String imgPersonagem,

        @NotBlank(message = "O nome do personagem é obrigatório.")
        String personagem,

        // Campos Numéricos Simples
        @Min(value = 1, message = "A idade deve ser maior que zero.")
        int idade,

        // Campos Enum (devem ser NotNull)
        @NotNull(message = "A origem é obrigatória.")
        TipoOrigem origem,

        @NotNull(message = "A classe é obrigatória.")
        TipoClasse classe,

        @NotNull(message = "A trilha é obrigatória.")
        TipoTrilha trilha,

        @NotNull(message = "A afinidade é obrigatória.")
        TipoElemento afinidade,

        // Recursos e Atributos
        @Min(value = 0, message = "O Nível de Exposição não pode ser negativo.")
        int nivelExposicao,

        @Min(value = 0, message = "O Esforço por Rodada não pode ser negativo.")
        int esforcoPorRodada,

        int agilidade,
        int forca,
        int intelecto,
        int presenca,
        int vigor,

        // Objetos Aninhados (Recursos)
        @NotNull(message = "Os Pontos de Vida são obrigatórios.")
        Pontos pontosDeVida,

        @NotNull(message = "Os Pontos de Esforço são obrigatórios.")
        Pontos pontosDeEsforco,

        @NotNull(message = "Os Pontos de Sanidade são obrigatórios.")
        Pontos pontosDeSanidade,

        // Defesas e Movimento
        int defesa,
        int defesaEsquiva,
        int redDanoBloqueando,
        String protecoes,
        String resistencia,
        String deslocamento
) {}