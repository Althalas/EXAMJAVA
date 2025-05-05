# README - Projet Electricity Business EXAM JAVA

Ce projet implémente les exigences du système de gestion de réservation de bornes de recharge électrique. Il utilise une structure Java avec interfaces et services en mémoire. Cette version inclut une documentation Javadoc en français.
Il est réalisé dans le cadre de l'évaluation du module Java SE de la CDA_R5.
Ce projet est fait par PETIT Martin.

## Structure du Projet

Le code source (src/) suit la structure de packages demandée :

+ model/ : Entités (Utilisateur, LieuRecharge, BorneRecharge, Reservation) et énumérations (EtatBorne, StatutReservation).
+ Interfaces/ : Contrats des services (AuthentificationService, BorneService, ReservationService, DocumentService).
+ Services/ : Implémentations des interfaces, utilisant les collections Java.
+ UI/ : Interface utilisateur console (MenuPrincipal, ConsoleMain).
+ UI/Main : Classe Main pour l'initialisation et le lancement.
  
### Fonctionnalités Implémentées

+ Comptes : Inscription (code affiché), Validation par code, Connexion/Déconnexion.
+ Bornes & Lieux : Ajouter/Modifier Lieu, Ajouter/Modifier Borne, Supprimer Borne (si pas de réservation future). Ces actions sont accessibles via le menu Administration (option 6) qui requiert d'être connecté.
+ Réservation : Chercher bornes DISPONIBLES pour un créneau, Créer réservation (statut EN_ATTENTE), Accepter/Refuser réservation (via menu Administration).
+ Documents : Générer un reçu texte (.txt) dans /exports lors de l'acceptation.
+ Console : Menu principal, validation simple des entrées.
  
### Fonctionnalités Bonus

+ Pas de bonus réalisé
