package org.eidd.poa.school.planner.modele;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Professeur {
    private int id;
    private String nom;
    private String prenom;
    private String matiere;
    private List<String> classesEnseignees;
    
    
    public Professeur(int id, String nom, String prenom, String matiere) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID doit être positif");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (matiere == null || matiere.trim().isEmpty()) {
            throw new IllegalArgumentException("La matière ne peut pas être vide");
        }
        
        this.id = id;
        this.nom = nom.trim();
        this.prenom = prenom.trim();
        this.matiere = matiere.trim();
        this.classesEnseignees = new ArrayList<>();
    }
    
    
    public int getId() { 
        return id; 
    }
    
    
    public String getNom() { 
        return nom; 
    }
    
    
    public String getPrenom() { 
        return prenom; 
    }
    
    
    public String getMatiere() { 
        return matiere; 
    }
    
    
    public List<String> getClassesEnseignees() { 
        return new ArrayList<>(classesEnseignees);
    }
    
   
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.nom = nom.trim();
    }
    
    
    public void setPrenom(String prenom) {
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        this.prenom = prenom.trim();
    }
    
    
    public void setMatiere(String matiere) {
        if (matiere == null || matiere.trim().isEmpty()) {
            throw new IllegalArgumentException("La matière ne peut pas être vide");
        }
        this.matiere = matiere.trim();
    }
    
    // ========== MÉTHODES MÉTIER ==========
    
    
    public boolean ajouterClasse(String classe) {
        if (classe == null || classe.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la classe ne peut pas être vide");
        }
        
        String classeTrimmed = classe.trim();
        if (!classesEnseignees.contains(classeTrimmed)) {
            classesEnseignees.add(classeTrimmed);
            return true;
        }
        return false;
    }
    
    
    public boolean supprimerClasse(String classe) {
        if (classe == null || classe.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la classe ne peut pas être vide");
        }
        return classesEnseignees.remove(classe.trim());
    }
    
    
    public boolean enseigneClasse(String classe) {
        if (classe == null) return false;
        return classesEnseignees.contains(classe.trim());
    }
    
   
    public int getNombreClasses() {
        return classesEnseignees.size();
    }
    
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    
    public String getDescription() {
        return String.format("Professeur %s - Spécialité: %s (%d classe(s) enseignée(s))", 
                           getNomComplet(), matiere, getNombreClasses());
    }
    
   
    public void clearClasses() {
        classesEnseignees.clear();
    }
    
    
    
    @Override
    public String toString() {
        return String.format("Professeur{id=%d, nom='%s', prenom='%s', matiere='%s', classes=%s}",
                           id, nom, prenom, matiere, classesEnseignees);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Professeur that = (Professeur) obj;
        return id == that.id && 
               Objects.equals(nom, that.nom) && 
               Objects.equals(prenom, that.prenom) && 
               Objects.equals(matiere, that.matiere);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, matiere);
    }
}