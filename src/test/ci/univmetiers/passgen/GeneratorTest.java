package ci.univmetiers.passgen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires avec JUnit 5 pour le générateur de mots de passe.
 */
public class GeneratorTest {

    @Test
    public void testLongueurMotDePasse() {
        Generator generator = new Generator();
        String pwd = generator.generate(16, true, true, true, true);
        
        assertEquals(16, pwd.length(), "La longueur du mot de passe doit être exactement de 16 caractères.");
    }

    @Test
    public void testInclusionMajuscules() {
        Generator generator = new Generator();
        String pwd = generator.generate(10, true, false, false, false);
        
        assertTrue(pwd.matches("^[A-Z]+$"), "Le mot de passe devrait contenir uniquement des majuscules.");
    }

    @Test
    public void testInclusionChiffres() {
        Generator generator = new Generator();
        String pwd = generator.generate(8, false, false, true, false);
        
        assertTrue(pwd.matches("^[0-9]+$"), "Le mot de passe devrait contenir uniquement des chiffres.");
    }

    @Test
    public void testErreurAucunTypeSelectionne() {
        Generator generator = new Generator();
        
        // Vérifie qu'une IllegalArgumentException est bien levée
        assertThrows(IllegalArgumentException.class, () -> {
            generator.generate(12, false, false, false, false);
        }, "Une exception doit être levée si aucun jeu de caractères n'est sélectionné.");
    }
}