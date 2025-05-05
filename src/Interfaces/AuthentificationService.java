package Interfaces;

import model.Utilisateur;

import java.util.Optional;

/**
 * Interface pour l'authentification et la gestion des comptes.
 */
public interface AuthentificationService {
    /**
     * Tente d'inscrire un nouvel utilisateur dans le système.
     * Vérifie si l'email est déjà utilisé.
     *
     * @param email L'adresse e-mail souhaitée pour le nouveau compte.
     * @param motDePasse Le mot de passe choisi pour le nouveau compte.
     * @return Un {@link Optional} contenant l'{@link Utilisateur} créé (non encore validé)
     * si l'inscription réussit (email unique), ou un {@code Optional} vide sinon.
     */
    Optional<Utilisateur> inscrire(String email, String motDePasse);
    /**
     * Génère un code de validation unique, l'associe à l'utilisateur spécifié
     * (typiquement en le stockant dans l'objet Utilisateur) et le retourne.
     * Dans une application réelle, ce service enverrait aussi le code par email.
     *
     * @param utilisateur L'{@link Utilisateur} pour lequel générer et stocker le code.
     * @return Le code de validation généré, ou null en cas d'erreur.
     */
    String genererEtStockerCodeValidation(Utilisateur utilisateur); // Renommé pour clarté
    /**
     * Tente de valider un compte utilisateur en utilisant l'email et le code de validation fournis.
     * Vérifie si l'utilisateur existe, s'il n'est pas déjà validé, et si le code correspond.
     * Si la validation réussit, le statut de l'utilisateur est mis à jour (devient valide)
     * et le code de validation est généralement supprimé ou invalidé.
     *
     * @param email L'email de l'utilisateur tentant de valider son compte.
     * @param code Le code de validation fourni par l'utilisateur.
     * @return true si la validation est réussie, false sinon (utilisateur non trouvé, déjà validé, code incorrect).
     */
    void validerCompte(String email, String code);
    /**
     * Tente de connecter un utilisateur en vérifiant son email et son mot de passe.
     * La connexion n'est possible que si l'utilisateur existe, si son compte est validé
     * et si le mot de passe fourni correspond à celui enregistré.
     *
     * @param email L'email fourni pour la connexion.
     * @param motDePasse Le mot de passe fourni pour la connexion.
     * @return Un {@link Optional} contenant l'{@link Utilisateur} connecté si l'authentification réussit,
     * ou un {@code Optional} vide sinon.
     */
    Optional<Utilisateur> connecter(String email, String motDePasse);
    // Pas d'autres méthodes requises par les specs obligatoires
}