package UI;

import Interfaces.ReservationService;
import Services.AuthentificationServiceImplement;
import Services.BorneServiceImplement;
import model.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Gère le menu principal et les interactions utilisateur en console.
 */
public class MenuPrincipal {

    private final AuthentificationServiceImplement authService;
    private final BorneServiceImplement borneService;
    private final ReservationService reservationService;
    // DocumentService est utilisé par ReservationService

    private Utilisateur utilisateurConnecte = null;
    // Le "mode opérateur" est simulé par l'accès au menu d'administration (6)
    // qui n'est proposé que si l'utilisateur est connecté.

    /**
     * Constructeur.
     * @param auth Service d'authentification.
     * @param borne Service de gestion des bornes.
     * @param resa service de reservation.
     */
    public MenuPrincipal(AuthentificationServiceImplement auth, BorneServiceImplement borne, ReservationService resa) {
        this.authService = auth;
        this.borneService = borne;
        this.reservationService = resa;
    }

    /**
     * lance la boucle principal du menu principal.
     */
    public void demarrer() {
        int choix;
        do {
            afficherMenu();
            choix = ConsoleMain.lireIntDansPlage("Votre choix:", 0, 6); // 0 à 6 selon specs
            ConsoleMain.separer();

            switch (choix) {
                case 1: gererInscription(); break;
                case 2: gererValidationCompte(); break;
                case 3: gererConnexionDeconnexion(); break;
                case 4: gererRechercheEtReservation(); break;
                case 5: gererMesReservations(); break;
                case 6: gererAdministration(); break;
                case 0: ConsoleMain.afficher("Au revoir !"); break;
                default: ConsoleMain.afficherErreur("Choix invalide."); // Ne dois pas arriver
            }
            if (choix != 0) ConsoleMain.pause();

        } while (choix != 0);
    }

    /**
     * Affiche le menu principal.
     */
    private void afficherMenu() {
        ConsoleMain.separer();
        ConsoleMain.afficher("=== Electricity Business ===");
        if (utilisateurConnecte != null) {
            ConsoleMain.afficher("Connecté: " + utilisateurConnecte.getEmail());
            ConsoleMain.separer();
            ConsoleMain.afficher("4. Rechercher & réserver une borne");
            ConsoleMain.afficher("5. Gérer mes réservations");
            ConsoleMain.afficher("6. Administration (lieux / bornes / approbations)"); // Option Admin
            ConsoleMain.afficher("---");
            ConsoleMain.afficher("3. Se déconnecter");
        } else {
            ConsoleMain.afficher("1. S'inscrire");
            ConsoleMain.afficher("2. Valider l'inscription");
            ConsoleMain.afficher("3. Se connecter");
            // Option 6 non affichée si non connectée
        }
        ConsoleMain.afficher("0. Quitter");
        ConsoleMain.separer();
    }


    /**
     * Gère l'inscription d'un utilisateur.
     */
    private void gererInscription() {
        if (utilisateurConnecte != null) { ConsoleMain.afficherErreur("Déjà connecté."); return; }
        ConsoleMain.afficher("--- Inscription ---");
        String email = ConsoleMain.lireStringNonVide("Email:");
        String mdp = ConsoleMain.lireStringNonVide("Mot de passe:");
        String mdpConf = ConsoleMain.lireStringNonVide("Confirmer mot de passe:");
        if (!mdp.equals(mdpConf)) { ConsoleMain.afficherErreur("Mots de passe différents."); return; }

        Optional<Utilisateur> userOpt = authService.inscrire(email, mdp);
        if (userOpt.isPresent()) {
            String code = authService.genererEtStockerCodeValidation(userOpt.get());
            ConsoleMain.afficher(">>> CODE DE VALIDATION A NOTER: " + code + " <<<"); // Affichage obligatoire
            ConsoleMain.afficher("Utilisez l'option 2 pour valider.");
        } // Erreur gérée dans le service
    }

    /**
     * Gère la validation d'un compte utilisateur.
     */
    private void gererValidationCompte() {
        if (utilisateurConnecte != null) { ConsoleMain.afficherErreur("Déjà connecté."); return; }
        ConsoleMain.afficher("--- Validation Compte ---");
        String email = ConsoleMain.lireStringNonVide("Email du compte à valider:");
        String code = ConsoleMain.lireStringNonVide("Code de validation:");
        authService.validerCompte(email, code); // Message succès/erreur dans le service
    }

    /**
     * Gère les connexions utilisateurs.
     */
    private void gererConnexionDeconnexion() {
        if (utilisateurConnecte != null) {
            utilisateurConnecte = null;
            ConsoleMain.afficher("Déconnexion réussie.");
        } else {
            ConsoleMain.afficher("--- Connexion ---");
            String email = ConsoleMain.lireStringNonVide("Email:");
            String mdp = ConsoleMain.lireStringNonVide("Mot de passe:");
            utilisateurConnecte = authService.connecter(email, mdp).orElse(null);
            // Message succès/erreur dans le service
        }
    }

    /**
     * Gère la recherche pour la réservation d'une borne.
     */
    private void gererRechercheEtReservation() {
        if (utilisateurConnecte == null) { ConsoleMain.afficherErreur("Connexion requise."); return; }
        ConsoleMain.afficher("--- Recherche & Réservation ---");
        LocalDateTime debut = ConsoleMain.lireDateTime("Début créneau");
        LocalDateTime fin = ConsoleMain.lireDateTime("Fin créneau");

        if (!fin.isAfter(debut)) { ConsoleMain.afficherErreur("Fin doit être après début."); return; }
        if (debut.isBefore(LocalDateTime.now())) { ConsoleMain.afficherErreur("Début dans le passé."); return; }

        List<BorneRecharge> disponibles = borneService.rechercherBornesDisponibles(debut, fin);
        if (disponibles.isEmpty()) { ConsoleMain.afficher("Aucune borne disponible."); return; }

        ConsoleMain.afficher("Bornes DISPONIBLES:");
        for (int i = 0; i < disponibles.size(); i++) {
            BorneRecharge b = disponibles.get(i);
            Optional<LieuRecharge> lieuOpt = borneService.getLieuById(b.getLieuId());
            String lieuNom = lieuOpt.map(LieuRecharge::getNom).orElse("Lieu ID " + b.getLieuId());
            ConsoleMain.afficher((i + 1) + ". Borne " + b.getId() + " [" + lieuNom + "] (" + String.format("%.2f", b.getTarifHoraire()) + " Eur/h)");
        }
        ConsoleMain.separer();
        int choix = ConsoleMain.lireIntDansPlage("Choisir borne (ou 0 pour annuler)", 0, disponibles.size());
        if (choix == 0) { ConsoleMain.afficher("Annulé."); return; }

        BorneRecharge borneChoisie = disponibles.get(choix - 1);
        if (ConsoleMain.demanderConfirmation("Réserver borne " + borneChoisie.getId() + " ?")) {
            reservationService.creerReservation(utilisateurConnecte, borneChoisie, debut, fin);
            // Message succès/erreur dans le service
        } else {
            ConsoleMain.afficher("Annulé.");
        }
    }

    /**
     * Affiche les reservations de l'utilisateur connecté
     */
    private void gererMesReservations() {
        if (utilisateurConnecte == null) { ConsoleMain.afficherErreur("Connexion requise."); return; }
        ConsoleMain.afficher("--- Mes Réservations ---");
        List<Reservation> mesResas = reservationService.getReservationsUtilisateur(utilisateurConnecte);
        if (mesResas.isEmpty()) { ConsoleMain.afficher("Aucune réservation."); return; }

        mesResas.sort(Comparator.comparing(Reservation::getDateDebut));
        for (Reservation r : mesResas) {
            Optional<LieuRecharge> lieuOpt = borneService.getLieuById(r.getBorne().getLieuId());
            String lieuNom = lieuOpt.map(LieuRecharge::getNom).orElse("Lieu ID " + r.getBorne().getLieuId());
            ConsoleMain.afficher(
                    "ID:" + r.getId() +
                            " Borne:" + r.getBorne().getId() + " [" + lieuNom + "]" +
                            " Du:" + ConsoleMain.formatDateTime(r.getDateDebut()) +
                            " Au:" + ConsoleMain.formatDateTime(r.getDateFin()) +
                            " Statut:" + r.getStatut()
            );
        }
    }
    /**
     * Affiche le sous-menu d'administration et gère les actions correspondantes
     * (gestion lieux, bornes, approbation réservations).
     * Requiert qu'un utilisateur soit connecté (simule le "mode opérateur").
     */
    private void gererAdministration() {
        if (utilisateurConnecte == null) { ConsoleMain.afficherErreur("Connexion requise pour admin."); return; }
        // Ici, on simule le "mode opérateur" par l'accès à ce menu
        ConsoleMain.afficher("--- Administration ---");
        ConsoleMain.afficher("1. Gérer Lieux");
        ConsoleMain.afficher("2. Gérer Bornes");
        ConsoleMain.afficher("3. Approuver Réservations");
        ConsoleMain.afficher("0. Retour");
        ConsoleMain.separer();
        int choix = ConsoleMain.lireIntDansPlage("Choix admin:", 0, 3);
        ConsoleMain.separer();

        switch (choix) {
            case 1: adminGererLieux(); break;
            case 2: adminGererBornes(); break;
            case 3: adminGererReservations(); break;
            case 0: break; // Retour
        }
    }

    /**
     * Gère les opérations CRUD pour les lieux de recharge (Ajouter, Modifier, Lister).
     */
    private void adminGererLieux() {
        ConsoleMain.afficher("--- Admin: Lieux ---");
        ConsoleMain.afficher("1. Ajouter Lieu");
        ConsoleMain.afficher("2. Modifier Lieu");
        ConsoleMain.afficher("3. Lister Lieux");
        ConsoleMain.afficher("0. Retour");
        int choix = ConsoleMain.lireIntDansPlage("Choix:", 0, 3);
        ConsoleMain.separer();
        switch (choix) {
            case 1:
                String nom = ConsoleMain.lireStringNonVide("Nom nouveau lieu:");
                String adr = ConsoleMain.lireStringNonVide("Adresse nouveau lieu:");
                borneService.ajouterLieu(nom, adr);
                break;
            case 2:
                long idMod = ConsoleMain.lireInt("ID lieu à modifier:");
                Optional<LieuRecharge> lieuOpt = borneService.getLieuById(idMod);
                if (lieuOpt.isEmpty()) { ConsoleMain.afficherErreur("Lieu non trouvé."); break; }
                ConsoleMain.afficher("Actuel: " + lieuOpt.get());
                String nNom = ConsoleMain.lireString("Nouveau nom (vide=inchangé):");
                String nAdr = ConsoleMain.lireString("Nouvelle adresse (vide=inchangé):");
                borneService.modifierLieu(idMod, nNom, nAdr);
                break;
            case 3:
                List<LieuRecharge> lieux = borneService.getAllLieux();
                if (lieux.isEmpty()) ConsoleMain.afficher("Aucun lieu.");
                else lieux.forEach(l -> ConsoleMain.afficher(l.toString()));
                break;
            case 0: break;
        }
    }
    /**
     * Gère les opérations CRUD pour les bornes de recharge (Ajouter, Modifier, Supprimer, Lister).
     */
    private void adminGererBornes() {
        ConsoleMain.afficher("--- Admin: Bornes ---");
        ConsoleMain.afficher("1. Ajouter Borne");
        ConsoleMain.afficher("2. Modifier Borne");
        ConsoleMain.afficher("3. Supprimer Borne");
        ConsoleMain.afficher("4. Lister Bornes (par lieu)");
        ConsoleMain.afficher("0. Retour");
        int choix = ConsoleMain.lireIntDansPlage("Choix:", 0, 4);
        ConsoleMain.separer();
        switch (choix) {
            case 1:
                long idLieu = ConsoleMain.lireInt("ID du lieu pour la nouvelle borne:");
                if (borneService.getLieuById(idLieu).isEmpty()) { ConsoleMain.afficherErreur("Lieu non trouvé."); break; }
                double tarif = ConsoleMain.lireDoublePositif("Tarif horaire:");
                borneService.ajouterBorne(idLieu, tarif);
                break;
            case 2:
                long idBorneMod = ConsoleMain.lireInt("ID borne à modifier:");
                Optional<BorneRecharge> borneOpt = borneService.getBorneById(idBorneMod);
                if (borneOpt.isEmpty()) { ConsoleMain.afficherErreur("Borne non trouvée."); break; }
                ConsoleMain.afficher("Actuel: " + borneOpt.get());
                ConsoleMain.afficher("Nouvel état: 1=DISPO, 2=OCCUPEE, 3=HORS_SERVICE (0=inchangé)");
                int etatChoix = ConsoleMain.lireIntDansPlage("Choix état:", 0, 3);
                EtatBorne nEtat = switch(etatChoix) {
                    case 1 -> EtatBorne.DISPONIBLE;
                    case 2 -> EtatBorne.OCCUPEE;
                    case 3 -> EtatBorne.HORS_SERVICE;
                    default -> null; // Inchangé
                };
                String tarifStr = ConsoleMain.lireString("Nouveau tarif (vide=inchangé):");
                Double nTarif = null;
                if (!tarifStr.isBlank()) {
                    try {
                        nTarif = Double.parseDouble(tarifStr.replace(',', '.'));
                        if (nTarif < 0) { ConsoleMain.afficherErreur("Tarif négatif ignoré."); nTarif = null;}
                    } catch (NumberFormatException e) { ConsoleMain.afficherErreur("Tarif invalide ignoré."); }
                }
                borneService.modifierBorne(idBorneMod, nEtat, nTarif);
                break;
            case 3:
                long idBorneSup = ConsoleMain.lireInt("ID borne à supprimer:");
                if (ConsoleMain.demanderConfirmation("Confirmer suppression borne " + idBorneSup + " ?")) {
                    borneService.supprimerBorne(idBorneSup); // Message succès/erreur dans le service
                } else { ConsoleMain.afficher("Annulé."); }
                break;
            case 4:
                List<LieuRecharge> lieux = borneService.getAllLieux();
                if (lieux.isEmpty()) ConsoleMain.afficher("Aucun lieu (donc aucune borne).");
                else {
                    lieux.forEach(l -> {
                        ConsoleMain.afficher("--- Lieu: " + l.getNom() + " (ID:" + l.getId() + ") ---");
                        List<BorneRecharge> bornesLieu = l.getBornes(); // Utilise la liste du lieu
                        if (bornesLieu.isEmpty()) ConsoleMain.afficher("  (aucune borne)");
                        else bornesLieu.forEach(b -> ConsoleMain.afficher("  -> " + b));
                    });
                }
                break;
            case 0: break;
        }
    }
    /**
     * Gère l'acceptation ou le refus des réservations en attente.
     * Affiche les réservations avec le statut EN_ATTENTE, permet à l'admin d'en choisir une
     * et de l'accepter ou de la refuser.
     */
    private void adminGererReservations() {
        ConsoleMain.afficher("--- Admin: Approuver Réservations ---");
        List<Reservation> enAttente = reservationService.getAllReservations().stream()
                .filter(r -> r.getStatut() == StatutReservation.EN_ATTENTE)
                .sorted(Comparator.comparing(Reservation::getDateDebut))
                .toList();

        if (enAttente.isEmpty()) { ConsoleMain.afficher("Aucune réservation en attente."); return; }

        ConsoleMain.afficher("Réservations EN_ATTENTE:");
        for (int i = 0; i < enAttente.size(); i++) {
            Reservation r = enAttente.get(i);
            Optional<LieuRecharge> lieuOpt = borneService.getLieuById(r.getBorne().getLieuId());
            String lieuNom = lieuOpt.map(LieuRecharge::getNom).orElse("Lieu ID " + r.getBorne().getLieuId());
            ConsoleMain.afficher(
                    (i + 1) + ". ID:" + r.getId() +
                            " User:" + r.getUtilisateur().getEmail() +
                            " Borne:" + r.getBorne().getId() + " [" + lieuNom + "]" +
                            " Du:" + ConsoleMain.formatDateTime(r.getDateDebut()) +
                            " Au:" + ConsoleMain.formatDateTime(r.getDateFin())
            );
        }
        ConsoleMain.separer();
        int choix = ConsoleMain.lireIntDansPlage("Choisir réservation à traiter (ou 0 pour annuler)", 0, enAttente.size());
        if (choix == 0) { ConsoleMain.afficher("Annulé."); return; }

        Reservation resaChoisie = enAttente.get(choix - 1);
        ConsoleMain.afficher("Action pour résa ID " + resaChoisie.getId() + ": 1=Accepter, 2=Refuser, 0=Annuler");
        int action = ConsoleMain.lireIntDansPlage("Action:", 0, 2);
        ConsoleMain.separer();
        switch (action) {
            case 1: reservationService.accepterReservation(resaChoisie.getId()); break;
            case 2: reservationService.refuserReservation(resaChoisie.getId()); break;
            case 0: ConsoleMain.afficher("Action annulée."); break;
        }
        // Messages succès/erreur dans le service
    }
}
