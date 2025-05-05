package Interfaces;

import model.BorneRecharge;
import model.EtatBorne;
import model.LieuRecharge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour la gestion des lieux et bornes.
 */
public interface BorneService {
    // Lieux
    LieuRecharge ajouterLieu(String nom, String adresse);
    void modifierLieu(long lieuId, String nouveauNom, String nouvelleAdresse);
    Optional<LieuRecharge> getLieuById(long lieuId); // Utile pour l'admin et l'affichage
    List<LieuRecharge> getAllLieux(); // Utile pour l'admin

    // Bornes
    void ajouterBorne(long lieuId, double tarifHoraire);
    void modifierBorne(long borneId, EtatBorne nouvelEtat, Double nouveauTarif);
    Optional<BorneRecharge> getBorneById(long borneId); // Utile pour l'admin et l'affichage
    void supprimerBorne(long borneId); // Doit vérifier les réservations futures
    List<BorneRecharge> rechercherBornesDisponibles(LocalDateTime debut, LocalDateTime fin); // Exigence clé
}