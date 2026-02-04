package org.eidd.poa.school.planner;

/**
 * Classe représentant un élève
 */
public class Eleve {
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
        this.noteDiscipline = 10; // Valeur par défaut pour éviter les erreurs
        this.absences = 0;
        this.retards = 0;
        this.remarques = "";
    }

    // Getters nécessaires pour l'affichage
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getNomComplet() {return prenom + " " + nom;}
    public int getAge() { return age; }
    public int getNoteDiscipline() { return noteDiscipline; }
    public int getAbsences() {return absences;}
    public int getRetards() {return retards;}
    public String getRemarques() {return remarques;}
    
    // Setter nécessaire pour le bouton "Modifier Note"
    public void setNoteDiscipline(int note) { this.noteDiscipline = note; }
    
    //Math.max pour assurer un nombre positif
    public void setAbsences(int absences) {
        this.absences = Math.max(0, absences);
    }

    public void setRetards(int retards) {
        this.retards = Math.max(0, retards);
    }

    public void setRemarques(String remarques) {
        if (remarques == null) {
            this.remarques = "";
        } else {
            this.remarques = remarques;
        }
    }

    /** Incrémente le nombre d'absences. */
    public void ajouterAbsence() {
        absences++;
    }

    /** Incrémente le nombre de retards. */
    public void ajouterRetard() {
        retards++;
    }
    
    @Override
    public String toString() {
        return getNomComplet() + " (Absences: " + absences + ", Retards: " + retards + ")";
    }
}