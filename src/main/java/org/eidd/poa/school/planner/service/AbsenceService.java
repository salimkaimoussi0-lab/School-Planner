package org.eidd.poa.school.planner.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.eidd.poa.school.planner.modele.*;


/**
 * Service dédié à la gestion des absences et des retards des élèves.
 * <p>
 * Ce service gère l'historique des événements et met à jour automatiquement
 * la note de discipline de l'élève en appliquant des pénalités:
 * <ul>
 * <li>-2 points par absence</li>
 * <li>-1 point par retard</li>
 * </ul>
 * </p>
 *
 * @author [Votre Nom/Groupe ici]
 * @version 1.0
 */
public class AbsenceService {
    private Map<String, Map<LocalDate, String>> historiqueAbsences; // nomComplet -> (date -> motif)
    
    /**
     * Constructeur par défaut. Initialise l'historique des absences.
     */
    public AbsenceService() {
        this.historiqueAbsences = new HashMap<>();
    }
    
    
    /**
     * Incrémente le compteur d'absence de l'élève d'une unité.
     * Enregistre l'absence avec un motif par défaut ("Absence non justifiée") et met à jour la note de discipline.
     *
     * @param eleve L'élève concerné. Ne doit pas être null.
     * @throws IllegalArgumentException si l'élève est null.
     */
    public void incrementerAbsence(Eleve eleve) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        eleve.ajouterAbsence();
        enregistrerAbsence(eleve.getNomComplet(), "Absence non justifiée");
        mettreAJourNoteDiscipline(eleve);
    }
    
     
    /**
     * Incrémente le compteur d'absence de l'élève et enregistre un motif spécifique.
     * Met à jour la note de discipline.
     *
     * @param eleve L'élève concerné. Ne doit pas être null.
     * @param motif Le motif de l'absence.
     * @throws IllegalArgumentException si l'élève est null.
     */
    public void incrementerAbsence(Eleve eleve, String motif) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        eleve.ajouterAbsence();
        enregistrerAbsence(eleve.getNomComplet(), motif);
        mettreAJourNoteDiscipline(eleve);
    }
    
    
    /**
     * Incrémente le compteur de retard de l'élève d'une unité.
     * Met à jour la note de discipline.
     *
     * @param eleve L'élève concerné. Ne doit pas être null.
     * @throws IllegalArgumentException si l'élève est null.
     */
    public void incrementerRetard(Eleve eleve) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        eleve.ajouterRetard();
        mettreAJourNoteDiscipline(eleve);
    }
    
    
    /**
     * Décrémente le compteur d'absence de l'élève d'une unité, sans descendre en dessous de zéro.
     * Met à jour la note de discipline.
     *
     * @param eleve L'élève concerné. Ne doit pas être null.
     * @throws IllegalArgumentException si l'élève est null.
     */
    public void decrementerAbsence(Eleve eleve) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        eleve.setAbsences(Math.max(0, eleve.getAbsences() - 1));
        mettreAJourNoteDiscipline(eleve);
    }
    
    
    /**
     * Décrémente le compteur de retard de l'élève d'une unité, sans descendre en dessous de zéro.
     * Met à jour la note de discipline.
     *
     * @param eleve L'élève concerné. Ne doit pas être null.
     * @throws IllegalArgumentException si l'élève est null.
     */
    public void decrementerRetard(Eleve eleve) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        eleve.setRetards(Math.max(0, eleve.getRetards() - 1));
        mettreAJourNoteDiscipline(eleve);
    }
    
    
    /**
     * Réinitialise les compteurs d'absences et de retards à zéro, et restaure la note de discipline à 10/10.
     *
     * @param eleve L'élève concerné. Ne doit pas être null.
     * @throws IllegalArgumentException si l'élève est null.
     */
    public void reinitialiserCompteurs(Eleve eleve) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        eleve.setAbsences(0);
        eleve.setRetards(0);
        eleve.setNoteDiscipline(10); // Réinitialise la note de discipline
    }
    
    
    /**
     * Calcule et applique la nouvelle note de discipline de l'élève en fonction de ses absences et retards.
     * Pénalités : -2 points par absence, -1 point par retard. La note minimale est 0.
     *
     * @param eleve L'élève dont la note doit être mise à jour.
     */
    private void mettreAJourNoteDiscipline(Eleve eleve) {
        int penaliteAbsences = eleve.getAbsences() * 2;
        int penaliteRetards = eleve.getRetards();
        
        int nouvelleNote = Math.max(0, 10 - penaliteAbsences - penaliteRetards);
        eleve.setNoteDiscipline(nouvelleNote);
    }
    
    
    /**
     * Enregistre une absence pour l'élève spécifié à la date du jour.
     *
     * @param nomComplet Le nom complet de l'élève.
     * @param motif Le motif de l'absence.
     */
    private void enregistrerAbsence(String nomComplet, String motif) {
        LocalDate aujourdhui = LocalDate.now();
        Map<LocalDate, String> absencesEleve = historiqueAbsences.getOrDefault(nomComplet, new HashMap<>());
        absencesEleve.put(aujourdhui, motif);
        historiqueAbsences.put(nomComplet, absencesEleve);
    }
    
    
    /**
     * Retourne l'historique des absences de l'élève (date -> motif) sous forme de copie.
     *
     * @param eleve L'élève concerné.
     * @return Une Map contenant l'historique des absences, ou une Map vide si l'élève est null ou n'a pas d'historique.
     */
    public Map<LocalDate, String> getHistoriqueAbsences(Eleve eleve) {
        if (eleve == null) {
            return new HashMap<>();
        }
        return new HashMap<>(historiqueAbsences.getOrDefault(eleve.getNomComplet(), new HashMap<>()));
    }
    
    
    /**
     * Calcule le taux d'absence de l'élève en pourcentage par rapport au nombre total de jours.
     *
     * @param eleve L'élève concerné.
     * @param joursTotal Le nombre total de jours de cours sur la période de référence.
     * @return Le taux d'absence en pourcentage (entre 0.0 et 100.0), ou 0.0 si les arguments sont invalides.
     */
    public double calculerTauxAbsence(Eleve eleve, int joursTotal) {
        if (eleve == null || joursTotal <= 0) {
            return 0.0;
        }
        return (eleve.getAbsences() * 100.0) / joursTotal;
    }
    
    
    /**
     * Génère un rapport textuel complet des statistiques de discipline de l'élève.
     * Le rapport inclut le total des absences, retards, la note de discipline et l'historique détaillé.
     *
     * @param eleve L'élève pour lequel générer le rapport.
     * @return Le rapport d'absences formaté.
     */
    public String genererRapportAbsences(Eleve eleve) {
        if (eleve == null) {
            return "Aucun élève spécifié";
        }
        
        StringBuilder rapport = new StringBuilder();
        rapport.append(String.format("Rapport d'absences - %s\n", eleve.getNomComplet()));
        rapport.append("=".repeat(40)).append("\n");
        rapport.append(String.format("Absences: %d\n", eleve.getAbsences()));
        rapport.append(String.format("Retards: %d\n", eleve.getRetards()));
        rapport.append(String.format("Note de discipline: %d/10\n", eleve.getNoteDiscipline()));
        
        Map<LocalDate, String> historique = getHistoriqueAbsences(eleve);
        if (!historique.isEmpty()) {
            rapport.append("\nHistorique des absences:\n");
            historique.forEach((date, motif) -> {
                rapport.append(String.format("  - %s: %s\n", date, motif));
            });
        }
        
        return rapport.toString();
    }
}