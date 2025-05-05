package Interfaces;

import model.BorneRecharge;
import model.Reservation;
import model.StatutReservation;
import model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface pour la gestion des réservations.
 */
public interface ReservationService {
    /**
     * Tente de créer une nouvelle réservation pour un utilisateur sur une borne spécifique
     * et pour un créneau donné.
     * La création échoue si l'utilisateur n'est pas valide, si les données sont invalides,
     * ou si la borne est déjà réservée (statut ACCEPTEE ou EN_ATTENTE) sur un créneau
     * qui chevauche celui demandé.
     * La réservation est créée avec le statut {@link StatutReservation#EN_ATTENTE}.
     *
     * @param utilisateur L'{@link Utilisateur} (doit être valide) effectuant la réservation.
     * @param borne La {@link BorneRecharge} à réserver.
     * @param debut La date et heure de début du créneau souhaité.
     * @param fin La date et heure de fin du créneau souhaité.
     */
    void creerReservation(Utilisateur utilisateur, BorneRecharge borne, LocalDateTime debut, LocalDateTime fin);
    /**
     * Accepte une réservation existante qui est actuellement en attente.
     * Met à jour le statut de la réservation à {@link StatutReservation#ACCEPTEE}.
     * Déclenche également la génération du document de reçu via le {@link DocumentService}.
     *
     * @param reservationId L'identifiant de la réservation à accepter.
     */
    void accepterReservation(long reservationId);

    /**
     * Refuse une réservation existante qui est actuellement en attente.
     * Met à jour le statut de la réservation à {@link StatutReservation#REFUSEE}.
     *
     * @param reservationId L'identifiant de la réservation à refuser.
     */
    void refuserReservation(long reservationId);
    /**
     * Récupère toutes les réservations associées à un utilisateur donné.
     *
     * @param utilisateur L'{@link Utilisateur} dont on souhaite récupérer les réservations.
     * @return Une {@code List} contenant toutes les {@link Reservation} de cet utilisateur.
     * Peut-être vide si l'utilisateur n'a aucune réservation avec laquelle est null.
     */
    List<Reservation> getReservationsUtilisateur(Utilisateur utilisateur); // Pour le menu "Gérer mes réservations"
    /**
     * Récupère toutes les réservations enregistrées dans le système, quel que soit
     * leur statut ou l'utilisateur associé.
     * Principalement utile pour les opérations d'administration ou les vérifications internes.
     *
     * @return Une {@code List} contenant toutes les {@link Reservation}. Peut-être vide.
     */
    List<Reservation> getAllReservations(); // Utile pour l'admin et la vérification des disponibilités/suppressions
    /**
     * Vérifie si une borne spécifique a des réservations futures.
     * Une réservation est considérée comme future si son statut est
     * {@link StatutReservation#ACCEPTEE} ou {@link StatutReservation#EN_ATTENTE}
     * et si son heure de fin est postérieure à l'heure actuelle.
     * Utilisé par {@link BorneService#supprimerBorne(long)}.
     *
     * @param borneId L'identifiant de la borne à vérifier.
     * @return true si au moins une réservation future existe pour cette borne, false sinon.
     */
    boolean borneHasFutureReservations(long borneId); // Nécessaire pour BorneService.supprimerBorne
}