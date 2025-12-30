package br.com.api.harmonia_rpg.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustoRitual {
    private int normal = 1;
    private int discente = 0;
    private int verdadeiro = 0;

}
