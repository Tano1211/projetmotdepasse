package ci.univmetiers.passgen;

import java.security.SecureRandom;

/**
 * Moteur de génération utilisant SecureRandom pour une robustesse cryptographique.
 */
public class Generator {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:,.<>?/";
    
    private final SecureRandom random = new SecureRandom();

    public String generate(int length, boolean useUpper, boolean useLower, boolean useDigits, boolean useSymbols) {
        StringBuilder charPool = new StringBuilder();
        if (useUpper) charPool.append(UPPER);
        if (useLower) charPool.append(LOWER);
        if (useDigits) charPool.append(DIGITS);
        if (useSymbols) charPool.append(SYMBOLS);

        if (charPool.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Sélectionnez au moins un type de caractère.");
        }

        String pool = charPool.toString();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(pool.charAt(random.nextInt(pool.length())));
        }

        return password.toString();
    }
}