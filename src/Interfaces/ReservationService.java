package Interfaces;

import model.BorneRecharge;
import model.Reservation;
import model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface pour la gestion des réservations.
 */
public interface ReservationService {
    void creerReservation(Utilisateur utilisateur, BorneRecharge borne, LocalDateTime debut, LocalDateTime fin);
    void accepterReservation(long reservationId);
    void refuserReservation(long reservationId);
    List<Reservation> getReservationsUtilisateur(Utilisateur utilisateur); // Pour le menu "Gérer mes réservations"
    List<Reservation> getAllReservations(); // Utile pour l'admin et la vérification des disponibilités/suppressions
    boolean borneHasFutureReservations(long borneId); // Nécessaire pour BorneService.supprimerBorne
}