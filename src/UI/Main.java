package UI;


import Interfaces.AuthentificationService;
import Interfaces.BorneService;
import Services.*;
import model.*;



/**
 * Classe principale de l'application.
 * Initialise les services et lance le menu.
 */
public class Main {
    /**
     * méthode principale de l'application.
     * @param args argument de la ligne de commande
     */
    public static void main(String[] args) {
        System.out.println("Démarrage Electricity Business (Basique)...");

        // 1. Création des services
        AuthentificationServiceImplement authService = new AuthentificationServiceImplement();
        BorneServiceImplement borneService = new BorneServiceImplement(); // Besoin de type concret pour setter
        ReservationServiceImplement reservationService = new ReservationServiceImplement(); // Besoin de type concret pour setter
        DocumentServiceImplement documentService = new DocumentServiceImplement(); // Besoin de type concret pour setter

        // 2. Injection des dépendances (via setters)
        borneService.setReservationService(reservationService);
        reservationService.setDocumentService(documentService);
        documentService.setBorneService(borneService); // Important pour le reçu

        // 3. Ajout de données initiales (optionnel)
        ajouterDonneesTest(authService, borneService);

        // 4. Création et lancement du menu
        MenuPrincipal menu = new MenuPrincipal(authService, borneService, reservationService);
        menu.demarrer();

        System.out.println("Arrêt Electricity Business.");
    }

    /**
     * permet les tests de l'application.
     * @param auth service d'authentification à peupler
     * @param borne service borne à peupler
     */
    private static void ajouterDonneesTest(AuthentificationService auth, BorneService borne) {
        System.out.println("\nAjout données de test...");
        // Lieu 1 + Bornes
        LieuRecharge l1 = borne.ajouterLieu("Gare", "1 Place de la Gare");
        if (l1 != null) {
            borne.ajouterBorne(l1.getId(), 0.60); // Borne 1
            borne.ajouterBorne(l1.getId(), 0.60); // Borne 2
        }
        // Lieu 2 + Borne
        LieuRecharge l2 = borne.ajouterLieu("Mairie", "1 Rue de la Paix");
        if (l2 != null) {
            borne.ajouterBorne(l2.getId(), 0.50); // Borne 3
        }
    }
}