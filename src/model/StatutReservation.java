package model;

/**
 * énum pour gerer le statut d'acceptation de la réservation.
 */
public enum StatutReservation {
    EN_ATTENTE, // En attente de validation (si nécessaire) ou de début
    ACCEPTEE, // Validée et planifiée
    REFUSEE     // Refusée par l'opérateur (si validation)
}
