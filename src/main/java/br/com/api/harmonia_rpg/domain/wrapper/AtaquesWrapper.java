package br.com.api.harmonia_rpg.domain.wrapper;

import br.com.api.harmonia_rpg.domain.entities.Ataque;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class AtaquesWrapper {
    private List<Ataque> ataques = new ArrayList<>();

    public AtaquesWrapper() {}

    public void setAtaques(List<Ataque> ataques) {
        this.ataques = ataques != null ? ataques : new ArrayList<>();
    }
}
