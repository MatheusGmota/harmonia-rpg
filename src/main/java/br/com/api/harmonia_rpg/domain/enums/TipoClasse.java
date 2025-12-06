package br.com.api.harmonia_rpg.domain.enums;

import lombok.Getter;

@Getter
public enum TipoClasse {
    COMBATENTE("Combatente"),
    ESPECIALISTA("Especialista"),
    OCULTISTA("Ocultista");

    private final String valor;

    TipoClasse(String valor) {
        this.valor = valor;
    }
}
