package model;


import java.util.Objects;

/**
 * Représente une borne de recharge électrique physique.
 * Chaque borne est associée à un lieuRecharge, possède un état
 * et un tarif horaire.
 */
public class BorneRecharge {
    private static long compteurId = 0;

    private final long id;
    private EtatBorne etat;
    private double tarifHoraire;
    // On a besoin de savoir à quel lieu elle appartient pour l'administration
    private final long lieuId;

    /**
     * Constructeur de BorneRecharge.
     * la borne est init en dispo et elle a un id unique.
     * @param tarifHoraire tarif de l'utilisation de la borne en heure.
     * @param lieuId l'id du lieu où se trouve la borne.
     */
    public BorneRecharge(double tarifHoraire, long lieuId) {
        this.id = ++compteurId;
        this.etat = EtatBorne.DISPONIBLE; // État initial
        this.tarifHoraire = tarifHoraire;
        this.lieuId = lieuId;
    }

    /**
     * Retourne l'identifiant unique de la borne.
     * @return L'ID de la borne.
     */
    public long getId() { return id; }
    /**
     * Retourne l'état actuel de la borne.
     * @return L'état de la borne ({@link EtatBorne}).
     */
    public EtatBorne getEtat() { return etat; }
    /**
     * Retourne le tarif horaire appliqué pour la recharge sur cette borne.
     * @return Le tarif horaire en euros (ou autre unité monétaire).
     */
    public double getTarifHoraire() { return tarifHoraire; }
    /**
     * Retourne l'identifiant du lieu de recharge auquel cette borne appartient.
     * @return L'ID du {@link LieuRecharge} associé.
     */
    public long getLieuId() { return lieuId; }

    /**
     * Change l'état de la borne.
     * @param etat Le nouvel état de la borne ({@link EtatBorne}).
     */
    public void setEtat(EtatBorne etat) { this.etat = etat; }
    /**
     * Met à jour le tarif horaire de la borne.
     * @param tarifHoraire Le nouveau tarif horaire (doit être positif ou nul).
     */
    public void setTarifHoraire(double tarifHoraire) { this.tarifHoraire = tarifHoraire; }
    /**
     * Retourne une représentation textuelle de l'objet BorneRecharge,
     * incluant son ID, son état, son tarif formaté et l'ID du lieu associé.
     * @return Une chaîne de caractères décrivant la borne.
     */
    @Override
    public String toString() {
        return "BorneRecharge{id=" + id + ", etat=" + etat + ", tarifHoraire=" + String.format("%.2f", tarifHoraire) + ", lieuId=" + lieuId + '}';
    }

    /**
     * Compare cette borne à un autre objet pour vérifier l'égalité.
     * Deux bornes sont considérées égales s'ils ont le même ID.
     * @param o L'objet à comparer avec cette borne.
     * @return true si les objets sont égaux (même ID), false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorneRecharge that = (BorneRecharge) o;
        return id == that.id;
    }
    /**
     * Retourne un code de hachage pour cette borne, basé sur son ID unique.
     * @return Le code de hachage de la borne.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
