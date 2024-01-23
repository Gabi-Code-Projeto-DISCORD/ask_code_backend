package br.com.askcode.askcode.Util;

import java.util.UUID;

public class PasswordGeneratorUtil {
    private PasswordGeneratorUtil() {
    }

    public static String generateNewSecretKey() {
        return UUID.randomUUID().toString();
    }
}
