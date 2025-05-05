package Services;


import Interfaces.BorneService;
import Interfaces.DocumentService;
import model.LieuRecharge;
import model.Reservation;
import model.StatutReservation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Implement de documentService.
 * Permet de générer les reçus txt et la création du dossier export.
 */
public class DocumentServiceImplement implements DocumentService {

    private final String exportDirectory = "exports";
    private BorneService borneService; // Pour obtenir les infos du lieu

    public void setBorneService(BorneService borneService) {
        this.borneService = borneService;
    }

    // Créer le dossier au besoin
    public DocumentServiceImplement() {
        try {
            Path path = Paths.get(exportDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Dossier d'export créé: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erreur critique création dossier export '" + exportDirectory + "': " + e.getMessage());
        }
    }

    /**
     * Génère le contenu du reçu
     * @param reservation validation de la réservation
     * @throws IOException si on ne récupère pas le BorneService, on ne peut pas avoir les infos du lieu
     */
    @Override
    public void genererRecuTxt(Reservation reservation) throws IOException {
        if (reservation == null || reservation.getStatut() != StatutReservation.ACCEPTEE) {
            throw new IllegalArgumentException("Reçu uniquement pour réservation acceptée.");
        }
        if (borneService == null) {
            System.err.println("Erreur: BorneService non disponible pour générer reçu complet.");
            // On pourrait générer un reçu partiel, mais ici, on lance une exception
            // pour signaler le problème de configuration.
            throw new IllegalStateException("BorneService non injecté dans DocumentService.");
        }

        String nomFichier = "recu_" + reservation.getId() + ".txt";
        Path cheminFichier = Paths.get(exportDirectory, nomFichier);

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH:mm");

        long dureeMinutes = ChronoUnit.MINUTES.between(reservation.getDateDebut(), reservation.getDateFin());
        double dureeHeures = dureeMinutes / 60.0;
        double coutEstime = dureeHeures * reservation.getBorne().getTarifHoraire();

        // Récupérer nom et adresse du lieu
        String nomLieu = "Lieu ID " + reservation.getBorne().getLieuId(); // Fallback
        String adresseLieu = "Adresse inconnue";
        Optional<LieuRecharge> lieuOpt = borneService.getLieuById(reservation.getBorne().getLieuId());
        if (lieuOpt.isPresent()) {
            nomLieu = lieuOpt.get().getNom();
            adresseLieu = lieuOpt.get().getAdresse();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier.toFile()))) {
            writer.write("--- RECU RESERVATION ---"); writer.newLine();
            writer.write("ID Reservation: " + reservation.getId()); writer.newLine();
            writer.write("Utilisateur: " + reservation.getUtilisateur().getEmail()); writer.newLine();
            writer.write("Borne ID: " + reservation.getBorne().getId()); writer.newLine();
            writer.write("Lieu: " + nomLieu + " (" + adresseLieu + ")"); writer.newLine();
            writer.write("Debut: " + reservation.getDateDebut().format(formatterDate) + " " + reservation.getDateDebut().format(formatterHeure)); writer.newLine();
            writer.write("Fin: " + reservation.getDateFin().format(formatterDate) + " " + reservation.getDateFin().format(formatterHeure)); writer.newLine();
            writer.write(String.format("Duree: %d min", dureeMinutes)); writer.newLine();
            writer.write(String.format("Tarif horaire: %.2f Eur", reservation.getBorne().getTarifHoraire())); writer.newLine();
            writer.write(String.format("Cout estime: %.2f Eur", coutEstime)); writer.newLine();
            writer.write("Statut: ACCEPTEE"); writer.newLine();
            writer.write("------------------------"); writer.newLine();
        }
        System.out.println("Reçu généré: " + cheminFichier.toAbsolutePath());
    }
}