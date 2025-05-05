package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Représente un lieu géographique (ex: parking, centre commercial) où se trouvent
 * une ou plusieurs {@link BorneRecharge}.
 * Contient les informations descriptives du lieu et la liste des bornes associées.
 */
public class LieuRecharge {
    private static long compteurId = 0;

    private final long id;
    private String nom;
    private String adresse;
    // La spécification demande List<BorneRecharge>
    private final List<BorneRecharge> bornes;
    /**
     * Construit une nouvelle instance de LieuRecharge avec un nom et une adresse.
     * Le lieu est initialisé avec une liste de bornes vide et un ID unique lui est assigné.
     *
     * @param nom Le nom du lieu. Ne doit pas être null or vide.
     * @param adresse L'adresse du lieu. Ne doit pas être null or vide.
     */
    public LieuRecharge(String nom, String adresse) {
        this.id = ++compteurId;
        this.nom = nom;
        this.adresse = adresse;
        this.bornes = new ArrayList<>();
    }

    /**
     * Retourne l'identifiant unique du lieu.
     * @return L'ID du lieu.
     */
    public long getId() { return id; }
    /**
     * Retourne le nom du lieu.
     * @return Le nom du lieu.
     */
    public String getNom() { return nom; }
    /**
     * Retourne l'adresse du lieu.
     * @return L'adresse du lieu.
     */
    public String getAdresse() { return adresse; }
    /**
     * Retourne une copie de la liste des bornes de recharge associées à ce lieu.
     * La modification de la liste retournée n'affecte pas la liste interne du lieu.
     * @return Une {@code List} contenant les {@link BorneRecharge} du lieu.
     */
    // Retourne une copie pour éviter modification externe non contrôlée
    public List<BorneRecharge> getBornes() { return new ArrayList<>(bornes); }

    /**
     * Met à jour le nom du lieu.
     * @param nom Le nouveau nom du lieu.
     */
    public void setNom(String nom) { this.nom = nom; }
    /**
     * Met à jour l'adresse du lieu.
     * @param adresse La nouvelle adresse du lieu.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }
    /**
     * Ajoute une borne à la liste des bornes de ce lieu.
     * La borne n'est ajoutée que si elle n'est pas null, si son {@code lieuId}
     * correspond à l'ID de ce lieu, et si elle n'est pas déjà présente dans la liste.
     * @param borne La {@link BorneRecharge} à ajouter.
     */
    // --- Gestion des bornes (maintenue ici pour coller au modèle) ---
    public void ajouterBorne(BorneRecharge borne) {
        if (borne != null && borne.getLieuId() == this.id && !this.bornes.contains(borne)) {
            this.bornes.add(borne);
        }
    }
    /**
     * Supprime une borne de la liste des bornes de ce lieu.
     * @param borne La {@link BorneRecharge} à supprimer.
     * @return true si la borne a été trouvée et supprimée, false sinon.
     */
    public void supprimerBorne(BorneRecharge borne) {
        if (borne != null) {
            this.bornes.remove(borne);
        }
    }
    /**
     * Retourne une représentation textuelle de l'objet LieuRecharge,
     * incluant son ID, nom, adresse et le nombre de bornes qu'il contient.
     * @return Une chaîne de caractères décrivant le lieu.
     */
    @Override
    public String toString() {
        return "LieuRecharge{id=" + id + ", nom='" + nom + "', adresse='" + adresse + "', nbBornes=" + bornes.size() + '}';
    }
    /**
     * Compare ce lieu à un autre objet pour vérifier l'égalité.
     * Deux lieux sont considérés égaux s'ils ont le même ID.
     * @param o L'objet à comparer avec ce lieu.
     * @return true si les objets sont égaux (même ID), false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LieuRecharge that = (LieuRecharge) o;
        return id == that.id;
    }
    /**
     * Retourne un code de hachage pour ce lieu, basé sur son ID unique.
     * @return Le code de hachage du lieu.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}