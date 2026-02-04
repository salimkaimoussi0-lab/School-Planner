package org.eidd.poa.school.planner.controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eidd.poa.school.planner.donnee.ReferentielMemoire;
import org.eidd.poa.school.planner.donnee.PlanIO;
import org.eidd.poa.school.planner.modele.Eleve;
import org.eidd.poa.school.planner.modele.Place;
import org.eidd.poa.school.planner.modele.PlanDeClasse;
import org.eidd.poa.school.planner.vue.FenetrePrincipal;

import javax.swing.*;
import java.io.*;

public class ControleurPrincipal {

    private static final Logger LOGGER = LogManager.getLogger(ControleurPrincipal.class);

    private final ReferentielMemoire referentiel;
    private PlanDeClasse plan;
    private final FenetrePrincipal vue;
    private File fichierCourant = null;


    private Place placeSelectionnee;

    public ControleurPrincipal(ReferentielMemoire referentiel,
                               PlanDeClasse plan,
                               FenetrePrincipal vue) {
        this.referentiel = referentiel;
        this.plan = plan;
        this.vue = vue;

        LOGGER.info("Initialisation du contrôleur principal avec un plan {}x{}.",
                plan.getRangees(), plan.getColonnes());

        this.vue.setControleur(this);
        rafraichirAffichage();
    }

    private void rafraichirAffichage() {
        LOGGER.debug("Rafraîchissement de l'affichage. Place sélectionnée : {}",
                placeSelectionnee == null ? "aucune"
                        : "(" + placeSelectionnee.getRangee() + "," + placeSelectionnee.getColonne() + ")");

        vue.rafraichirPlan(plan, placeSelectionnee);
        if (placeSelectionnee != null) {
            vue.afficherInfoEleve(placeSelectionnee.getEleve());
        } else {
            vue.afficherInfoEleve(null);
        }
    }

    public void gererClicPlace(Place place) {
        this.placeSelectionnee = place;
        LOGGER.debug("Clic sur place ({}, {}). Élève = {}",
                place.getRangee(),
                place.getColonne(),
                place.getEleve() == null ? "aucun" : place.getEleve().getNomComplet());

        vue.afficherInfoEleve(place.getEleve());
        vue.rafraichirPlan(plan, placeSelectionnee);
    }

    // ---------- Actions ----------

    public void ajouterEleve() {
        if (placeSelectionnee == null) {
            LOGGER.warn("ajouterEleve() appelé sans place sélectionnée.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord une place.");
            return;
        }
        if (!placeSelectionnee.estLibre()) {
            LOGGER.warn("ajouterEleve() : tentative d'ajout sur une place déjà occupée ({}, {}).",
                    placeSelectionnee.getRangee(), placeSelectionnee.getColonne());
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Cette place est déjà occupée !");
            return;
        }

        String nom = JOptionPane.showInputDialog(vue.getFrame(), "Nom de l'élève :");
        String prenom = JOptionPane.showInputDialog(vue.getFrame(), "Prénom de l'élève :");
        if (nom == null || prenom == null || nom.isBlank() || prenom.isBlank()) {
            LOGGER.info("Création d'élève annulée (nom/prénom vide ou annulation).");
            return;
        }

        Eleve e = new Eleve(nom.trim(), prenom.trim(), 15);
        referentiel.ajouterEleve(e);
        placeSelectionnee.affecter(e);

        LOGGER.info("Nouvel élève ajouté : {} sur la place ({}, {}).",
                e.getNomComplet(),
                placeSelectionnee.getRangee(), placeSelectionnee.getColonne());

        rafraichirAffichage();
    }
    
    /** Retire (libère) l'élève de la place sélectionnée, après confirmation. */
    public void retirerEleve() {

        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("retirerEleve() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord un élève.");
            return;
        }

        Eleve e = placeSelectionnee.getEleve();

        int choix = JOptionPane.showConfirmDialog(
                vue.getFrame(),
                "Retirer l'élève " + e.getNomComplet() + " de cette place ?",
                "Retirer l'élève",
                JOptionPane.YES_NO_OPTION
        );

        if (choix != JOptionPane.YES_OPTION) {
            LOGGER.info("Retrait de l'élève {} annulé par l'utilisateur.", e.getNomComplet());
            return;
        }

        LOGGER.info("Élève {} retiré de la place ({}, {}).",
                e.getNomComplet(),
                placeSelectionnee.getRangee(),
                placeSelectionnee.getColonne());

        // On libère uniquement la place, l'élève reste dans le référentiel si besoin
        placeSelectionnee.liberer();
        placeSelectionnee = null;

        rafraichirAffichage();
    }


    public void deplacerEleve() {
        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("deplacerEleve() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez un élève à déplacer.");
            return;
        }

        String input = JOptionPane.showInputDialog(
                vue.getFrame(),
                "Nouvelle place (format : ligne,colonne, à partir de 1) :"
        );
        if (input == null || !input.matches("\\d+,\\d+")) {
            LOGGER.info("Déplacement annulé ou format invalide : '{}'.", input);
            return;
        }

        String[] parts = input.split(",");
        int r = Integer.parseInt(parts[0].trim()) - 1;
        int c = Integer.parseInt(parts[1].trim()) - 1;

        LOGGER.debug("Demande de déplacement vers ({}, {}).", r, c);

        Place nouvellePlace;
        try {
            nouvellePlace = plan.obtenirPlace(r, c);
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.warn("Place cible ({}, {}) inexistante.", r, c);
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Cette place n'existe pas !");
            return;
        }

        Eleve eleveSource = placeSelectionnee.getEleve();
        Eleve eleveCible = nouvellePlace.getEleve();

        // ----------- CAS 1 : nouvelle place libre -> déplacement normal -----------
        if (eleveCible == null) {
            LOGGER.info("Déplacement simple de {} vers ({}, {}).",
                    eleveSource.getNomComplet(), r, c);

            placeSelectionnee.liberer();
            nouvellePlace.affecter(eleveSource);
            placeSelectionnee = nouvellePlace;
        }
        // ----------- CAS 2 : nouvelle place occupée -> ÉCHANGE AUTOMATIQUE -------
        else {
            LOGGER.info("ÉCHANGE entre {} ({},{}) et {} ({},{}).",
                    eleveSource.getNomComplet(),
                    placeSelectionnee.getRangee(), placeSelectionnee.getColonne(),
                    eleveCible.getNomComplet(),
                    nouvellePlace.getRangee(), nouvellePlace.getColonne());

            placeSelectionnee.affecter(eleveCible);
            nouvellePlace.affecter(eleveSource);
            placeSelectionnee = nouvellePlace;
        }

        rafraichirAffichage();
    }

    public void ajouterAbsence() {
        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("ajouterAbsence() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord un élève.");
            return;
        }
        Eleve e = placeSelectionnee.getEleve();
        e.ajouterAbsence();

        LOGGER.info("Absence ajoutée pour {} (total = {}).",
                e.getNomComplet(), e.getAbsences());

        rafraichirAffichage();
    }

    public void retirerAbsence() {
        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("retirerAbsence() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord un élève.");
            return;
        }
        Eleve e = placeSelectionnee.getEleve();
        e.retirerAbsence();

        LOGGER.info("Absence retirée pour {} (total = {}).",
                e.getNomComplet(), e.getAbsences());

        rafraichirAffichage();
    }

    public void ajouterRetard() {
        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("ajouterRetard() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord un élève.");
            return;
        }
        Eleve e = placeSelectionnee.getEleve();
        e.ajouterRetard();

        LOGGER.info("Retard ajouté pour {} (total = {}).",
                e.getNomComplet(), e.getRetards());

        rafraichirAffichage();
    }

    public void retirerRetard() {
        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("retirerRetard() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord un élève.");
            return;
        }
        Eleve e = placeSelectionnee.getEleve();
        e.retirerRetard();

        LOGGER.info("Retard retiré pour {} (total = {}).",
                e.getNomComplet(), e.getRetards());

        rafraichirAffichage();
    }

    public void ajouterRemarque() {
        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("ajouterRemarque() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord un élève.");
            return;
        }
        Eleve e = placeSelectionnee.getEleve();
        String remarque = JOptionPane.showInputDialog(vue.getFrame(), "Remarque :");
        if (remarque == null || remarque.isBlank()) {
            LOGGER.info("Ajout de remarque annulé ou vide pour {}.", e.getNomComplet());
            return;
        }

        String current = e.getRemarques().trim();
        if (current.isEmpty()) {
            e.setRemarques(remarque.trim());
        } else {
            e.setRemarques(current + "\n" + remarque.trim());
        }

        LOGGER.info("Remarque ajoutée pour {}.", e.getNomComplet());

        rafraichirAffichage();
    }

    public void retirerRemarque() {
        if (placeSelectionnee == null || placeSelectionnee.estLibre()) {
            LOGGER.warn("retirerRemarque() appelé sans élève sélectionné.");
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Sélectionnez d'abord un élève.");
            return;
        }

        Eleve e = placeSelectionnee.getEleve();
        String remarquesBrut = e.getRemarques();
        if (remarquesBrut == null || remarquesBrut.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Aucune remarque à supprimer pour cet élève.");
            LOGGER.info("Aucune remarque à supprimer pour {}.", e.getNomComplet());
            return;
        }

        String[] lignes = remarquesBrut.split("\n");
        String[] options = new String[lignes.length];
        for (int i = 0; i < lignes.length; i++) {
            options[i] = (i + 1) + " - " + lignes[i];
        }

        String choix = (String) JOptionPane.showInputDialog(
                vue.getFrame(),
                "Sélectionnez la remarque à supprimer :",
                "Supprimer une remarque",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[options.length - 1]
        );

        if (choix == null) {
            LOGGER.info("Suppression de remarque annulée par l'utilisateur.");
            return;
        }

        int indexChoisi;
        try {
            indexChoisi = Integer.parseInt(choix.split(" - ", 2)[0]) - 1;
        } catch (NumberFormatException ex) {
            LOGGER.warn("Index de remarque invalide dans le choix : {}", choix);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lignes.length; i++) {
            if (i == indexChoisi) continue;
            if (sb.length() > 0) sb.append("\n");
            sb.append(lignes[i]);
        }

        e.setRemarques(sb.toString().trim());

        LOGGER.info("Remarque n°{} supprimée pour {}.", indexChoisi + 1, e.getNomComplet());

        rafraichirAffichage();
    }

    public void sauvegarderPlan() {

        // --- Cas 1 : un fichier est déjà ouvert → sauvegarde directe ---
        if (fichierCourant != null) {
            try {
                PlanIO.sauvegarderCsv(plan, fichierCourant);
                JOptionPane.showMessageDialog(vue.getFrame(),
                        "Plan sauvegardé dans :\n" + fichierCourant.getAbsolutePath());
                return;

            } catch (IOException ex) {
                LOGGER.error("Erreur lors du save direct : {}", fichierCourant.getName(), ex);
                JOptionPane.showMessageDialog(vue.getFrame(),
                        "Erreur lors de la sauvegarde : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // --- Cas 2 : aucun fichier → on demande où enregistrer ---
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Enregistrer le plan de classe dans un fichier");

        int res = chooser.showSaveDialog(vue.getFrame());
        if (res == JFileChooser.APPROVE_OPTION) {
            File fichier = chooser.getSelectedFile();

            try {
                PlanIO.sauvegarderCsv(plan, fichier);

                // On garde ce fichier comme "fichier courant"
                this.fichierCourant = fichier;
                vue.mettreAJourTitrePlan(fichier.getName());

                JOptionPane.showMessageDialog(vue.getFrame(),
                        "Plan sauvegardé dans :\n" + fichier.getAbsolutePath());
            } catch (IOException ex) {
                LOGGER.error("Erreur lors de la sauvegarde : ", ex);
                JOptionPane.showMessageDialog(vue.getFrame(),
                        "Erreur lors de la sauvegarde : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void chargerPlan() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Charger un plan de classe qui existe déjà");

        int res = chooser.showOpenDialog(vue.getFrame());
        if (res == JFileChooser.APPROVE_OPTION) {
            File fichier = chooser.getSelectedFile();
            try {
                PlanDeClasse nouveauPlan = PlanIO.chargerCsv(fichier, referentiel);

                this.plan = nouveauPlan;
                this.placeSelectionnee = null;

                vue.mettreAJourPlan(nouveauPlan);
                rafraichirAffichage();

                LOGGER.info("Plan chargé depuis : {}", fichier.getAbsolutePath());
                JOptionPane.showMessageDialog(vue.getFrame(),
                        "Plan chargé depuis :\n" + fichier.getAbsolutePath());
                this.fichierCourant = fichier;
                vue.mettreAJourTitrePlan(fichier.getName());
            } catch (IOException ex) {
                LOGGER.error("Erreur lors du chargement : {}", ex.getMessage());
                JOptionPane.showMessageDialog(vue.getFrame(),
                        "Erreur lors du chargement : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            LOGGER.info("Chargement de plan annulé par l'utilisateur.");
        }
    }
}
