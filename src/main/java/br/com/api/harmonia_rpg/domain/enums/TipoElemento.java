package br.com.api.harmonia_rpg.domain.enums;

import lombok.Getter;

@Getter
public enum TipoElemento {
    ENERGIA("Energia"),
    SANGUE("Sangue"),
    MORTE("Morte"),
    CONHECIMENTO("Conhecimento");

    private final String elemento;

    TipoElemento(String value) {
        this.elemento = value;
    }
}
