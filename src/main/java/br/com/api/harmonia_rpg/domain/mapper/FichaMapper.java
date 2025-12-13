package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.FichaRequestDTO;
import br.com.api.harmonia_rpg.domain.entities.Ficha;
import org.springframework.stereotype.Component;

@Component
public class FichaMapper {

    public static Ficha toFicha(FichaRequestDTO dto) {
        Ficha ficha = new Ficha();

        // Dados de Identificação
        ficha.setNomeCampanha(dto.nomeCampanha());
        ficha.setPersonagem(dto.personagem());
        ficha.setIdade(dto.idade());

        // Enums / Tipos
        ficha.setOrigem(dto.origem());
        ficha.setClasse(dto.classe());
        ficha.setTrilha(dto.trilha());
        ficha.setAfinidade(dto.afinidade());

        // Recursos e Limites
        ficha.setNivelExposicao(dto.nivelExposicao());
        ficha.setEsforcoPorRodada(dto.esforcoPorRodada());

        // Atributos
        ficha.setAgilidade(dto.agilidade());
        ficha.setForca(dto.forca());
        ficha.setIntelecto(dto.intelecto());
        ficha.setPresenca(dto.presenca());
        ficha.setVigor(dto.vigor());

        // Pontos de Recurso (Objetos Aninhados)
        ficha.setPontosDeVida(dto.pontosDeVida());
        ficha.setPontosDeEsforco(dto.pontosDeEsforco());
        ficha.setPontosDeSanidade(dto.pontosDeSanidade());

        // Defesas e Movimento
        ficha.setDefesa(dto.defesa());
        ficha.setDefesaEsquiva(dto.defesaEsquiva());
        ficha.setRedDanoBloqueando(dto.redDanoBloqueando());
        ficha.setProtecao(dto.protecao());
        ficha.setResistencia(dto.resistencia());
        ficha.setDeslocamento(dto.deslocamento());

        // Campos de ID e Timestamp (id, idUsuario, criadoEm) são setados no Service/Repository
        // ou pelo Firestore, NÃO devem vir do DTO.

        return ficha;
    }
}