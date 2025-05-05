package UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Classe utilitaire pour gérer les entrées/sorties de la console.
 */
public class ConsoleMain {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Affiche un message dans la console.
     * @param message message à afficher.
     */
    public static void afficher(String message) { System.out.println(message); }

    /**
     * Affiche un message d'erreur dans la console.
     * @param message message à afficher.
     */
    public static void afficherErreur(String message) { System.err.println("ERREUR: " + message); }

    /**
     * Affiche une ligne de séparation dans la console.
     */
    public static void separer() { System.out.println("----------------------------------------"); }

    /**
     * Affiche un message d'information dans la console et lit une saisie utilisateur.
     * @param prompt le message à afficher avant la saisie.
     * @return la saisie utilisateur.
     */
    public static String lireString(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    /**
     * Affiche une invite et lit une ligne de texte non vide entrée par l'utilisateur.
     * Si l'utilisateur entre une ligne vide ou un espace, un message d'erreur
     * est affiché et l'invite est répétée jusqu'à obtenir une entrée non vide.
     * @param prompt Le message à afficher pour demander l'entrée.
     * @return La chaîne de caractères non vide lue (espaces de début/fin retirés).
     */
    public static String lireStringNonVide(String prompt) {
        String input;
        do {
            input = lireString(prompt).trim();//retire les espaces inutiles
            if (input.isEmpty()) afficherErreur("Entrée obligatoire.");
        } while (input.isEmpty());
        return input;
    }

    /**
     * Affiche une invite et lit un int entré par l'utilisateur.
     * @param prompt le message pour afficher l'invite.
     * @return l'int lu.
     */
    public static int lireInt(String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            try {
                int val = scanner.nextInt();
                scanner.nextLine(); // Consomme le newline
                return val;
            } catch (InputMismatchException e) {
                afficherErreur("Nombre entier invalide.");
                scanner.nextLine(); // Consomme l'entrée invalide
            }
        }
    }

    /**
     * Lit un int entre min et max (inclus).
     * @param prompt affichage de l'invite.
     * @param min min valide.
     * @param max max valide.
     * @return l'int lu.
     */
    public static int lireIntDansPlage(String prompt, int min, int max) {
        while (true) {
            int val = lireInt(prompt + " (" + min + "-" + max + "):");
            if (val >= min && val <= max) return val;
            afficherErreur("Choix hors plage.");
        }
    }

    /**
     * Affiche une invite et lit un double entré par l'utilisateur.
     * @param prompt affichage de l'invite.
     * @return le double lu.
     */
    public static double lireDouble(String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            try {
                return Double.parseDouble(scanner.nextLine().replace(',', '.'));
            } catch (NumberFormatException e) {
                afficherErreur("Nombre décimal invalide.");
            }
        }
    }

    /**
     * Affiche une invite et lit un double positif ou nul, redemande si erreur
     * @param prompt le message à afficher.
     * @return le double lu.
     */
    public static double lireDoublePositif(String prompt) {
        while (true) {
            double val = lireDouble(prompt);
            if (val >= 0) return val;
            afficherErreur("La valeur doit être positive ou nulle.");
        }
    }

    /**
     * Affiche une invite et lit une date et une heure.
     * @param prompt le message à afficher.
     * @return la date et l'heure lu si valide.
     */
    public static LocalDateTime lireDateTime(String prompt) {
        while (true) {
            String input = lireString(prompt + " (JJ/MM/AAAA HH:MM):");
            try {
                return LocalDateTime.parse(input, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                afficherErreur("Format date/heure invalide.");
            }
        }
    }

    /**
     * Formate une date et une heure pour affichage.
     * @param dateTime la date et heure a formater ou null si nulle.
     * @return la chaîne formatée ou "N/A" si null.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Pose la question o/n à l'utilisateur
     * @param prompt la question
     * @return true si o, false si n
     */
    public static boolean demanderConfirmation(String prompt) {
        String reponse = lireString(prompt + " (O/N):").trim().toUpperCase();
        return reponse.equals("O");
    }

    /**
     * Met en pause jusqu'à appui de l'utilisateur.
     */
    public static void pause() {
        lireString("\nAppuyez sur Entrée pour continuer...");
        System.out.println();
    }
}