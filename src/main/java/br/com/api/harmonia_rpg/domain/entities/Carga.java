package br.com.api.harmonia_rpg.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carga {
    private int atual;
    private int total;
}
