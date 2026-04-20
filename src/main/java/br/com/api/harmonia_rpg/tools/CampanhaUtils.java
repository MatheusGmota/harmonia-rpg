package br.com.api.harmonia_rpg.tools;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CampanhaUtils {
    private static final String ALFABETO = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String gerarCodigoConvite() {
        return IntStream.range(0, 12)
                .mapToObj(i -> String.valueOf(ALFABETO.charAt(RANDOM.nextInt(ALFABETO.length()))))
                .collect(Collectors.joining());
    }
}
