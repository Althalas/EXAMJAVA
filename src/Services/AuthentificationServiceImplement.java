package Services;


import Interfaces.AuthentificationService;
import model.Utilisateur;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * implement de authentificationService.
 * données volatiles
 */
public class AuthentificationServiceImplement implements AuthentificationService {

    private final Map<String, Utilisateur> utilisateurs = new HashMap<>();

    /**
     *
     * @param email L'adresse e-mail souhaitée pour le nouveau compte.
     * @param motDePasse Le mot de passe choisi pour le nouveau compte.
     */
    @Override
    public Optional<Utilisateur> inscrire(String email, String motDePasse) {
        if (utilisateurs.containsKey(email)) {
            System.err.println("Erreur: Email déjà utilisé.");
            return Optional.empty();
        }
        Utilisateur user = new Utilisateur(email, motDePasse);
        utilisateurs.put(email, user);
        System.out.println("Inscription réussie pour " + email + ".");
        return Optional.of(user);
    }

    /**
     *
     * @param utilisateur L'{@link Utilisateur} pour lequel générer et stocker le code.
     * @return le code de validation
     */
    @Override
    public String genererEtStockerCodeValidation(Utilisateur utilisateur) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        utilisateur.setCodeValidation(code);
        // Pas d'affichage ici, le menu s'en chargera
        return code;
    }

    /**
     *
     * @param email L'email de l'utilisateur tentant de valider son compte.
     * @param code Le code de validation fourni par l'utilisateur.
     */
    @Override
    public void validerCompte(String email, String code) {
        Utilisateur user = utilisateurs.get(email);
        if (user != null && !user.isEstValide() && code != null && code.equals(user.getCodeValidation())) {
            user.setEstValide(true);
            user.setCodeValidation(null); // Code utilisé
            System.out.println("Compte " + email + " validé.");
            return;
        }
        System.err.println("Erreur de validation pour " + email + ".");
    }

    /**
     *
     * @param email L'email fourni pour la connexion.
     * @param motDePasse Le mot de passe fourni pour la connexion.
     */
    @Override
    public Optional<Utilisateur> connecter(String email, String motDePasse) {
        Utilisateur user = utilisateurs.get(email);
        if (user != null && user.isEstValide() && user.getMotDePasse().equals(motDePasse)) {
            System.out.println("Connexion réussie: " + email);
            return Optional.of(user);
        }
        System.err.println("Echec connexion pour " + email + ".");
        return Optional.empty();
    }
}