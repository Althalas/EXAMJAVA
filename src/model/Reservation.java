package model;


import java.time.LocalDateTime; // Pour gérer dates et heures
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Représente une réservation d'une {@link BorneRecharge} par un {@link Utilisateur}
 * pour un créneau horaire spécifique (défini par une date/heure de début et de fin).
 * Une réservation possède un statut qui évolue ({@link StatutReservation}).
 */
public class Reservation {
    private static long compteurId = 0;

    private final long id;
    private final Utilisateur utilisateur;
    private final BorneRecharge borne;
    private final LocalDateTime dateDebut;
    private final LocalDateTime dateFin;
    private StatutReservation statut;
    /**
     * Construit une nouvelle instance de Reservation.
     * La réservation est initialisée avec le statut EN_ATTENTE et un ID unique lui est assigné.
     *
     * @param utilisateur L'{@link Utilisateur} qui réserve. Ne doit pas être null.
     * @param borne La {@link BorneRecharge} réservée. Ne doit pas être null.
     * @param dateDebut La date et heure de début de la réservation. Ne doit pas être null.
     * @param dateFin La date et heure de fin de la réservation. Ne doit pas être null et doit être postérieure à {@code dateDebut}.
     */
    public Reservation(Utilisateur utilisateur, BorneRecharge borne, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.id = ++compteurId;
        this.utilisateur = utilisateur;
        this.borne = borne;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = StatutReservation.EN_ATTENTE; // Statut initial obligatoire
    }

    /**
     * Retourne l'identifiant unique de la réservation.
     * @return L'ID de la réservation.
     */
    public long getId() { return id; }
    /**
     * Retourne l'utilisateur associé à cette réservation.
     * @return L'{@link Utilisateur} de la réservation.
     */
    public Utilisateur getUtilisateur() { return utilisateur; }
    /**
     * Retourne la borne de recharge associée à cette réservation.
     * @return La {@link BorneRecharge} réservée.
     */
    public BorneRecharge getBorne() { return borne; }
    /**
     * Retourne la date et l'heure de début du créneau réservé.
     * @return Le {@link LocalDateTime} de début.
     */
    public LocalDateTime getDateDebut() { return dateDebut; }
    /**
     * Retourne la date et l'heure de fin du créneau réservé.
     * @return Le {@link LocalDateTime} de fin.
     */
    public LocalDateTime getDateFin() { return dateFin; }
    /**
     * Retourne le statut actuel de la réservation.
     * @return Le {@link StatutReservation} de la réservation.
     */
    public StatutReservation getStatut() { return statut; }

    /**
     * Met à jour le statut de la réservation.
     * Typiquement utilisé par l'administrateur pour accepter ou refuser une réservation.
     * @param statut Le nouveau {@link StatutReservation}.
     */
    public void setStatut(StatutReservation statut) { this.statut = statut; }

    /** Vérifie si cette réservation chevauche un créneau donné. */
    public boolean chevauche(LocalDateTime debutTest, LocalDateTime finTest) {
        return this.dateDebut.isBefore(finTest) && debutTest.isBefore(this.dateFin);
    }
    /**
     * Retourne une représentation textuelle de l'objet Reservation,
     * incluant son ID, l'email de l'utilisateur, l'ID de la borne,
     * les dates/heures de début et fin formatées, et le statut.
     * @return Une chaîne de caractères décrivant la réservation.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Reservation{id=" + id +
                ", user=" + (utilisateur != null ? utilisateur.getEmail() : "null") +
                ", borneId=" + (borne != null ? borne.getId() : "null") +
                ", debut=" + (dateDebut != null ? dateDebut.format(formatter) : "null") +
                ", fin=" + (dateFin != null ? dateFin.format(formatter) : "null") +
                ", statut=" + statut + '}';
    }
    /**
     * Compare cette réservation à un autre objet pour vérifier l'égalité.
     * Deux réservations sont considérées égales s'ils ont le même ID.
     * @param o L'objet a comparé avec cette réservation.
     * @return true si les objets sont égaux (même ID), false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id;
    }
    /**
     * Retourne un code de hachage pour cette réservation, basé sur son ID unique.
     * @return Le code de hachage de la réservation.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}