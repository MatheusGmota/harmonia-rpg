package br.com.api.harmonia_rpg.domain.enums;

import lombok.Getter;

@Getter
public enum TipoUsuario {
    MESTRE("Mestre"),
    JOGADOR("Jogador");

    private final String valor;

    TipoUsuario(String valor) {
        this.valor = valor;
    }
}
