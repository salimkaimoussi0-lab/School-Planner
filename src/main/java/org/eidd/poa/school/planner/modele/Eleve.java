package org.eidd.poa.school.planner.modele;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Eleve {

    private static final Logger LOGGER = LogManager.getLogger(Eleve.class);

    private String nom;
    private String prenom;
    private int age;
    private int noteDiscipline;
    private int absences;
    private int retards;
    private String remarques;

    public Eleve(String nom, String prenom, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.noteDiscipline = 10; // Valeur par défaut
        this.absences = 0;
        this.retards = 0;
        this.remarques = "";

        LOGGER.info("Nouvel élève créé : {} {}, âge {}", prenom, nom, age);
    }

    // --------------------
    //       GETTERS
    // --------------------

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public int getAge() {
        return age;
    }

    public int getNoteDiscipline() {
        return noteDiscipline;
    }

    public int getAbsences() {
        return absences;
    }

    public int getRetards() {
        return retards;
    }

    public String getRemarques() {
        return remarques;
    }

    // --------------------
    //       SETTERS
    // --------------------

    public void setNoteDiscipline(int note) {
        LOGGER.debug("Modification note discipline pour {} : {} → {}", 
                getNomComplet(), this.noteDiscipline, note);

        this.noteDiscipline = note;
    }

    public void setAbsences(int absences) {
        int old = this.absences;
        this.absences = Math.max(0, absences);

        LOGGER.debug("Modification absences pour {} : {} → {}", 
                getNomComplet(), old, this.absences);
    }

    public void setRetards(int retards) {
        int old = this.retards;
        this.retards = Math.max(0, retards);

        LOGGER.debug("Modification retards pour {} : {} → {}", 
                getNomComplet(), old, this.retards);
    }

    public void setRemarques(String remarques) {
        LOGGER.debug("Mise à jour remarques pour {} : '{}'", 
                getNomComplet(), remarques);

        if (remarques == null) {
            this.remarques = "";
        } else {
            this.remarques = remarques;
        }
    }

    // --------------------
    //   ACTIONS
    // --------------------

    /** Incrémente le nombre d’absences. */
    public void ajouterAbsence() {
        absences++;
        LOGGER.info("{} a désormais {} absence(s).", getNomComplet(), absences);
    }

    /** Incrémente le nombre de retards. */
    public void ajouterRetard() {
        retards++;
        LOGGER.info("{} a désormais {} retard(s).", getNomComplet(), retards);
    }
    /** Décrémente le nombre d’absences (sans passer sous 0). */
    public void retirerAbsence() {
        if (absences > 0) {
            absences--;
            LOGGER.info("Absence retirée pour {} (total = {}).", getNomComplet(), absences);
        }
    }

    /** Décrémente le nombre de retards (sans passer sous 0). */
    public void retirerRetard() {
        if (retards > 0) {
            retards--;
            LOGGER.info("Retard retiré pour {} (total = {}).", getNomComplet(), retards);
        }
    }


    @Override
    public String toString() {
        return getNomComplet() + " (Absences: " + absences + ", Retards: " + retards + ")";
    }
}
