package Interfaces;

import model.BorneRecharge;
import model.EtatBorne;
import model.LieuRecharge;
import model.StatutReservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour la gestion des lieux et bornes.
 */
public interface BorneService {
    /**
     * Ajoute un nouveau lieu de recharge au système.
     *
     * @param nom Le nom du nouveau lieu.
     * @param adresse L'adresse du nouveau lieu.
     * @return Le {@link LieuRecharge} créé et ajouté.
     */
    LieuRecharge ajouterLieu(String nom, String adresse);
    /**
     * Modifie les informations (nom et/ou adresse) d'un lieu de recharge existant.
     * Si un paramètre (nouveauNom ou nouvelleAdresse) est null ou vide,
     * l'attribut correspondant du lieu n'est pas modifié.
     *
     * @param lieuId L'identifiant du lieu à modifier.
     * @param nouveauNom Le nouveau nom souhaité (ou null/vide pour ne pas changer).
     * @param nouvelleAdresse La nouvelle adresse souhaitée (ou null/vide pour ne pas changer).
     */
    void modifierLieu(long lieuId, String nouveauNom, String nouvelleAdresse);
    /**
     * Récupère un lieu de recharge par son identifiant unique.
     *
     * @param lieuId L'ID du lieu à rechercher.
     * @return Un {@link Optional} contenant le {@link LieuRecharge} trouvé, ou un {@code Optional} vide
     * si aucun lieu ne correspond à cet ID.
     */
    Optional<LieuRecharge> getLieuById(long lieuId); // Utile pour l'admin et l'affichage
    /**
     * Récupère la liste de tous les lieux de recharge enregistrés dans le système.
     *
     * @return Une {@code List} contenant tous les {@link LieuRecharge}. Peut-être vide.
     */
    List<LieuRecharge> getAllLieux(); // Utile pour l'admin

    /**
     * Ajoute une nouvelle borne de recharge à un lieu existant spécifié par son ID.
     *
     * @param lieuId L'identifiant du {@link LieuRecharge} auquel ajouter la borne.
     * @param tarifHoraire Le tarif horaire pour l'utilisation de cette nouvelle borne.
     */
    void ajouterBorne(long lieuId, double tarifHoraire);
    /**
     * Modifie les informations (état et/ou tarif) d'une borne de recharge existante.
     * Si un paramètre (nouvelEtat ou nouveauTarif) est null, l'attribut correspondant
     * de la borne n'est pas modifié. Un tarif négatif est ignoré.
     *
     * @param borneId L'identifiant de la borne à modifier.
     * @param nouvelEtat Le nouvel {@link EtatBorne} souhaité (ou null pour ne pas changer).
     * @param nouveauTarif Le nouveau tarif horaire souhaité (ou null pour ne pas changer).
     */
    void modifierBorne(long borneId, EtatBorne nouvelEtat, Double nouveauTarif);
    /**
     * Récupère une borne de recharge par son identifiant unique.
     *
     * @param borneId L'ID de la borne à rechercher.
     * @return Un {@link Optional} contenant la {@link BorneRecharge} trouvée, ou un {@code Optional} vide
     * si aucune borne ne correspond à cet ID.
     */
    Optional<BorneRecharge> getBorneById(long borneId); // Utile pour l'admin et l'affichage
    /**
     * Supprime une borne de recharge du système.
     * La suppression n'est effectuée que si la borne existe et si elle n'a aucune
     * réservation future ({@link StatutReservation#ACCEPTEE} ou {@link StatutReservation#EN_ATTENTE})
     * dont l'heure de fin est postérieure à l'heure actuelle.
     * Cette méthode nécessite une interaction avec le {@link ReservationService}.
     *
     * @param borneId L'identifiant de la borne à supprimer.
     */
    void supprimerBorne(long borneId); // Doit vérifier les réservations futures
    /**
     * Recherche et retourne la liste des bornes de recharge qui sont disponibles
     * (état {@link EtatBorne#DISPONIBLE}) pendant l'intégralité du créneau horaire spécifié.
     * Une borne est considérée comme non disponible si elle a une réservation
     * ({@link StatutReservation#ACCEPTEE} ou {@link StatutReservation#EN_ATTENTE})
     * qui chevauche le créneau demandé.
     * Cette méthode nécessite une interaction avec le {@link ReservationService}.
     *
     * @param debut La date et heure de début du créneau de recherche.
     * @param fin La date et heure de fin du créneau de recherche.
     * @return Une {@code List} contenant les {@link BorneRecharge} disponibles. Peut-être vide.
     */
    List<BorneRecharge> rechercherBornesDisponibles(LocalDateTime debut, LocalDateTime fin); // Exigence clé
}