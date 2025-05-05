package Services;


import Interfaces.BorneService;
import Interfaces.ReservationService;
import model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// For Bonus Persistence
// import java.io.*;

/**
 * Implement de BorneService.
 */
public class BorneServiceImplement implements BorneService {

    private final Map<Long, LieuRecharge> lieux = new HashMap<>();
    private final Map<Long, BorneRecharge> bornes = new HashMap<>();
    private ReservationService reservationService; // Pour injection

    /**
     *
     * @param reservationService définit le service de réservation à utiliser pour supprimer les bornes et les recherches.
     */
    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     *
     * @param nom nom du lieu
     * @param adresse adresse du lieu
     * @return le nom avec l'adresse du nouveau lieu.
     */
    @Override
    public LieuRecharge ajouterLieu(String nom, String adresse) {
        LieuRecharge lieu = new LieuRecharge(nom, adresse);
        lieux.put(lieu.getId(), lieu);
        System.out.println("Lieu ajouté: " + lieu);
        return lieu;
    }

    /**
     * Récupère le lieu par son identifiant et modifie son nom et son adresse.
     * @param lieuId identifiant du lieu
     * @param nouveauNom nom du lieu
     * @param nouvelleAdresse adresse du lieu
     */
    @Override
    public void modifierLieu(long lieuId, String nouveauNom, String nouvelleAdresse) {
        LieuRecharge lieu = lieux.get(lieuId);
        if (lieu == null) return;
        if (nouveauNom != null && !nouveauNom.isBlank()) lieu.setNom(nouveauNom);
        if (nouvelleAdresse != null && !nouvelleAdresse.isBlank()) lieu.setAdresse(nouvelleAdresse);
        System.out.println("Lieu modifié: " + lieu);
    }

    /**
     *recherche le lieu par son identifiant.
     * @param lieuId
     * @return
     */
    @Override
    public Optional<LieuRecharge> getLieuById(long lieuId) {
        return Optional.ofNullable(lieux.get(lieuId));
    }

    /**
     *récupère tous les lieux.
     * @return
     */
    @Override
    public List<LieuRecharge> getAllLieux() {
        return new ArrayList<>(lieux.values());
    }

    /**
     *Vérifie l'existence du lieu et que le tarif n'est pas négatif puis ajoute une borne au lieu et dans la liste des bornes.
     * @param lieuId id du lieu auquel ajouter la borne.
     * @param tarifHoraire tarif horaire de la borne.
     */
    @Override
    public void ajouterBorne(long lieuId, double tarifHoraire) {
        LieuRecharge lieu = lieux.get(lieuId);
        if (lieu == null) {
            System.err.println("Erreur: Lieu " + lieuId + " non trouvé pour ajout borne.");
            return;
        }
        if (tarifHoraire < 0) {
            System.err.println("Erreur: Tarif horaire négatif.");
            return;
        }
        BorneRecharge borne = new BorneRecharge(tarifHoraire, lieuId);
        bornes.put(borne.getId(), borne);
        lieu.ajouterBorne(borne); // Ajoute à la liste du lieu aussi
        System.out.println("Borne ajoutée: " + borne + " au lieu " + lieu.getNom());
    }

    /**
     *récupère l'id de la borne et modifie les valeurs si valide.
     * @param borneId id de la borne
     * @param nouvelEtat état de la borne.
     * @param nouveauTarif tarif de la borne.
     */
    @Override
    public void modifierBorne(long borneId, EtatBorne nouvelEtat, Double nouveauTarif) {
        BorneRecharge borne = bornes.get(borneId);
        if (borne == null) return;
        if (nouvelEtat != null) borne.setEtat(nouvelEtat);
        if (nouveauTarif != null && nouveauTarif >= 0) borne.setTarifHoraire(nouveauTarif);
        System.out.println("Borne modifiée: " + borne);
    }

    /**
     *récupère l'id de la borne et retourne la borne.
     * @param borneId id de la borne.
     * @return l'identifiant de la borne
     */
    @Override
    public Optional<BorneRecharge> getBorneById(long borneId) {
        return Optional.ofNullable(bornes.get(borneId));
    }

    /**
     *Vérifie que la borne existe et n'est pas réservé puis Supprime la borne
     * @param borneId l'id de la borne
     */
    @Override
    public void supprimerBorne(long borneId) {
        if (reservationService == null) {
            System.err.println("Erreur critique: ReservationService non défini pour vérifier les réservations.");
            return;
        }
        BorneRecharge borne = bornes.get(borneId);
        if (borne == null) {
            System.err.println("Erreur: Borne " + borneId + " non trouvée pour suppression.");
            return;
        }

        if (reservationService.borneHasFutureReservations(borneId)) {
            System.err.println("Erreur: Borne " + borneId + " a des réservations futures.");
            return;
        }

        bornes.remove(borneId);
        LieuRecharge lieu = lieux.get(borne.getLieuId());
        if (lieu != null) {
            lieu.supprimerBorne(borne);
        }
        System.out.println("Borne " + borneId + " supprimée.");
    }

    /**
     *récupère toutes les bornes disponibles et non réservé.
     * @param debut date début
     * @param fin date fin
     * @return
     */
    @Override
    public List<BorneRecharge> rechercherBornesDisponibles(LocalDateTime debut, LocalDateTime fin) {
        if (reservationService == null) {
            System.err.println("Erreur critique: ReservationService non défini pour rechercher disponibilités.");
            return new ArrayList<>();
        }
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            System.err.println("Erreur: Créneau invalide.");
            return new ArrayList<>();
        }

        List<Reservation> reservationsConflictuelles = reservationService.getAllReservations().stream()
                .filter(r -> r.getStatut() == StatutReservation.ACCEPTEE || r.getStatut() == StatutReservation.EN_ATTENTE)
                .filter(r -> r.chevauche(debut, fin))
                .toList();

        List<Long> idsBornesReservees = reservationsConflictuelles.stream()
                .map(r -> r.getBorne().getId())
                .distinct()
                .toList();

        return bornes.values().stream()
                .filter(b -> b.getEtat() == EtatBorne.DISPONIBLE) // Doit être initialement disponible
                .filter(b -> !idsBornesReservees.contains(b.getId())) // Ne dois pas être réservée
                .collect(Collectors.toList());
    }
}