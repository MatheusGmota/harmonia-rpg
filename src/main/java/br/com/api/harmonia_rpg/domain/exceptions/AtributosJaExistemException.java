package br.com.api.harmonia_rpg.domain.exceptions;

public class AtributosJaExistemException extends RuntimeException {
    public AtributosJaExistemException(String idFicha) {
        super("Atributos já cadastrados para a ficha: " + idFicha);
    }
}
