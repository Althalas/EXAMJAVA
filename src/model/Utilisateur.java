package model;


import java.util.Objects;
public class Utilisateur {
    private static long compteurId = 0;

    private final long id;
    private final String email;
    private final String motDePasse;
    private String codeValidation;
    private boolean estValide;

    /**
     * contient tout ce qui sert pour l'identification, l'authentification et la validation
     * @param email permet de faire la connexion
     * @param motDePasse permet de faire la connexion
     */
    public Utilisateur(String email, String motDePasse) {
        this.id = ++compteurId;
        this.email = email;
        this.motDePasse = motDePasse;
        this.estValide = false;
        // codeValidation défini par le service
    }

    /**
     * Retourne l'id de l'utilisateur
     * @return l'id de l'utilisateur
     */
    public String getEmail() { return email; }

    /**
     * Retourne le mot de passe de l'utilisateur
     * @return le mot de passe de l'utilisateur
     */
    public String getMotDePasse() { return motDePasse; }

    /**
     * Retourne le code de validation de l'utilisateur.
     * @return le code de validation de l'utilisateur.
     */
    public String getCodeValidation() { return codeValidation; }

    /**
     * indique si l'utilisateur est valide ou non.
     * @return true si valide sinon false.
     */
    public boolean isEstValide() { return estValide; }

    /**
     * définit le code de validation de l'utilisateur.
     * @param codeValidation le code de validation de l'utilisateur.
     */
    public void setCodeValidation(String codeValidation) { this.codeValidation = codeValidation; }

    /**
     * Change le statut de validation de l'utilisateur.
     * @param estValide true si valide sinon false.
     */
    public void setEstValide(boolean estValide) { this.estValide = estValide; }

    /**
     * Représentation textuelle de l'utilisateur.
     * @return une représentation textuelle de l'utilisateur.
     */
    @Override
    public String toString() {
        return "Utilisateur{id=" + id + ", email='" + email + "', estValide=" + estValide + '}';
    }

    /**
     * Comparaison de deux utilisateurs par leur id.
     * @param o L'objet a comparé avec cet utilisateur.
     * @return true si les utilisateurs ont le même id sinon false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id == that.id;
    }

    /**
     * Hashcode de l'utilisateur.
     * @return le hashcode de l'utilisateur.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}