package ci.univmetiers.passgen;

import java.util.Scanner;

/**
 * Point d'entrée interactif en ligne de commande.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Generator generator = new Generator();
        DockerClient dockerClient = new DockerClient();

        System.out.println("============================================");
        System.out.println("  PASSGEN - SÉCURITÉ CLI (100% JAVA)        ");
        System.out.println("============================================");

        try {
            System.out.print("Longueur du mot de passe : ");
            int length = Integer.parseInt(scanner.nextLine());

            System.out.print("Inclure des majuscules ? (o/n) : ");
            boolean useUpper = scanner.nextLine().equalsIgnoreCase("o");

            System.out.print("Inclure des minuscules ? (o/n) : ");
            boolean useLower = scanner.nextLine().equalsIgnoreCase("o");

            System.out.print("Inclure des chiffres ? (o/n) : ");
            boolean useDigits = scanner.nextLine().equalsIgnoreCase("o");

            System.out.print("Inclure des symboles ? (o/n) : ");
            boolean useSymbols = scanner.nextLine().equalsIgnoreCase("o");

            System.out.print("Mode rafale : Nombre de mots de passe (1 par défaut) : ");
            String qtyInput = scanner.nextLine();
            int quantity = qtyInput.isBlank() ? 1 : Integer.parseInt(qtyInput);

            System.out.println("\n--- RÉSULTATS ---");
            for (int i = 0; i < quantity; i++) {
                String pwd = generator.generate(length, useUpper, useLower, useDigits, useSymbols);
                String strength = dockerClient.evaluatePassword(pwd);
                System.out.printf("[%d] %s -> Robustesse : %s%n", (i + 1), pwd, strength);
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur de saisie. Le programme s'est arrêté.");
        } finally {
            scanner.close();
        }
    }
}