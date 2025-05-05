package Services;


import Interfaces.DocumentService;
import Interfaces.ReservationService;
import model.BorneRecharge;
import model.Reservation;
import model.Utilisateur;
import model.StatutReservation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// For Bonus Persistence
// import java.io.*;

/**
 * Implement de ReservationService.
 */
public class ReservationServiceImplement implements ReservationService {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private DocumentService documentService; // Pour injection

    /**
     *Définit le service de document à utiliser pour les reçus.
     * @param documentService l'instance de DocumentService à utiliser
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     *Vérifie la validité de l'utilisateur et des données puis créer la réservation.
     * @param utilisateur identifiant de l'utilisateur.
     * @param borne identifiant de la borne.
     * @param debut heure de début de la réservation.
     * @param fin heure de fin de la réservation.
     */
    @Override
    public void creerReservation(Utilisateur utilisateur, BorneRecharge borne, LocalDateTime debut, LocalDateTime fin) {
        if (utilisateur == null || !utilisateur.isEstValide() || borne == null || debut == null || fin == null || !fin.isAfter(debut)) {
            System.err.println("Erreur: Données de réservation invalides ou utilisateur non validé.");
            return;
        }
        boolean conflit = reservations.values().stream()
                .filter(r -> r.getBorne().equals(borne))
                .filter(r -> r.getStatut() == StatutReservation.ACCEPTEE || r.getStatut() == StatutReservation.EN_ATTENTE)
                .anyMatch(r -> r.chevauche(debut, fin));

        if (conflit) {
            System.err.println("Erreur: Conflit détecté lors de la création de la réservation.");
            return;
        }


        Reservation resa = new Reservation(utilisateur, borne, debut, fin);
        reservations.put(resa.getId(), resa);
        System.out.println("Réservation créée (EN_ATTENTE): " + resa);
    }

    /**
     * Vérifie le statut de la réservation et lui change d'état en ACCEPTEE si c'est le cas.
     * @param reservationId l'id de la réservation
     */
    @Override
    public void accepterReservation(long reservationId) {
        Reservation resa = reservations.get(reservationId);
        if (resa != null && resa.getStatut() == StatutReservation.EN_ATTENTE) {
            resa.setStatut(StatutReservation.ACCEPTEE);
            System.out.println("Réservation " + reservationId + " acceptée.");
            if (documentService != null) {
                try {
                    documentService.genererRecuTxt(resa);// génère le Reçu
                } catch (IOException e) {
                    System.err.println("Erreur génération reçu pour " + reservationId + ": " + e.getMessage());
                }
            } else {
                System.err.println("Avertissement: DocumentService non configuré, reçu non généré.");
            }
            return;
        }
        System.err.println("Erreur: Impossible d'accepter réservation " + reservationId);
    }

    /**
     * Vérifie le statut et le change en REFUSEE si c'est le cas.
     * @param reservationId l'id de la réservation
     */
    @Override
    public void refuserReservation(long reservationId) {
        Reservation resa = reservations.get(reservationId);
        if (resa != null && resa.getStatut() == StatutReservation.EN_ATTENTE) {
            resa.setStatut(StatutReservation.REFUSEE);
            System.out.println("Réservation " + reservationId + " refusée.");
            return;
        }
        System.err.println("Erreur: Impossible de refuser réservation " + reservationId);
    }

    /**
     * Filtre-les réservation pour l'utilisateur donné.
     * @param utilisateur identifiant de l'utilisateur. Si null, retourne une liste vide.
     */
    @Override
    public List<Reservation> getReservationsUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) return new ArrayList<>();
        return reservations.values().stream()
                .filter(r -> r.getUtilisateur().equals(utilisateur))
                .collect(Collectors.toList());
    }

    /**
     * Retourne une nouvelle liste avec toutes les réservations
     * @return nouvelle liste de réservations
     */
    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    /**
     * Récupère les réservations pour la borne donnée qui sont ACCEPTEE ou EN ATTENTE.
     * Avec une date postérieure à maintenant.
     * @param borneId l'id de la borne
     */
    @Override
    public boolean borneHasFutureReservations(long borneId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservations.values().stream()
                .filter(r -> r.getBorne().getId() == borneId)
                .filter(r -> r.getStatut() == StatutReservation.ACCEPTEE || r.getStatut() == StatutReservation.EN_ATTENTE)
                .anyMatch(r -> r.getDateFin().isAfter(maintenant));
    }
}
