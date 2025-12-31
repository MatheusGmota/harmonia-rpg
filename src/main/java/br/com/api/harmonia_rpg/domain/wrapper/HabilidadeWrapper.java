package br.com.api.harmonia_rpg.domain.wrapper;

import br.com.api.harmonia_rpg.domain.entities.Habilidade;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class HabilidadeWrapper {
    private List<Habilidade> habilidades = new ArrayList<>();

    public HabilidadeWrapper() {}

    public void setHabilidades(List<Habilidade> habilidades) {
        this.habilidades = habilidades != null ? habilidades : new ArrayList<>();
    }
}
