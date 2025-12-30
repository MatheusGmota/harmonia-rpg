package br.com.api.harmonia_rpg.domain.wrapper;

import br.com.api.harmonia_rpg.domain.entities.Ritual;

import java.util.ArrayList;
import java.util.List;

public class RituaisWrapper {

    private List<Ritual> rituais = new ArrayList<>();

    public RituaisWrapper() {}

    public List<Ritual> getRituais() {
        return rituais;
    }

    public void setRituais(List<Ritual> rituais) {
        this.rituais = rituais != null ? rituais : new ArrayList<>();
    }
}

