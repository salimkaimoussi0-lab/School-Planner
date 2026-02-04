package org.eidd.poa.school.planner.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.eidd.poa.school.planner.modele.*;

public class RemarqueService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    
    public void ajouterRemarque(Eleve eleve, String remarque) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        if (remarque == null || remarque.trim().isEmpty()) {
            throw new IllegalArgumentException("La remarque ne peut pas être vide");
        }
        
        String remarqueFormatee = formaterRemarque(remarque.trim());
        String remarquesExistantes = eleve.getRemarques();
        
        if (remarquesExistantes.isEmpty()) {
            eleve.setRemarques(remarqueFormatee);
        } else {
            eleve.setRemarques(remarquesExistantes + "\n" + remarqueFormatee);
        }
    }
    
    
    public void ajouterRemarque(Eleve eleve, String type, String remarque) {
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève ne peut pas être null");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type ne peut pas être vide");
        }
        if (remarque == null || remarque.trim().isEmpty()) {
            throw new IllegalArgumentException("La remarque ne peut pas être vide");
        }
        
        String remarqueFormatee = String.format("[%s] %s - %s", 
            type.trim().toUpperCase(), 
            LocalDateTime.now().format(FORMATTER), 
            remarque.trim());
        
        String remarquesExistantes = eleve.getRemarques();
        if (remarquesExistantes.isEmpty()) {
            eleve.setRemarques(remarqueFormatee);
        } else {
            eleve.setRemarques(remarquesExistantes + "\n" + remarqueFormatee);
        }
    }
    
    
    public boolean supprimerDerniereRemarque(Eleve eleve) {
        if (eleve == null || eleve.getRemarques().isEmpty()) {
            return false;
        }
        
        String[] remarques = eleve.getRemarques().split("\n");
        if (remarques.length == 1) {
            eleve.setRemarques("");
        } else {
            StringBuilder nouvellesRemarques = new StringBuilder();
            for (int i = 0; i < remarques.length - 1; i++) {
                if (i > 0) nouvellesRemarques.append("\n");
                nouvellesRemarques.append(remarques[i]);
            }
            eleve.setRemarques(nouvellesRemarques.toString());
        }
        return true;
    }
    
    
    public String[] getRemarquesTableau(Eleve eleve) {
        if (eleve == null || eleve.getRemarques().isEmpty()) {
            return new String[0];
        }
        return eleve.getRemarques().split("\n");
    }
    
    
    public int compterRemarques(Eleve eleve) {
        if (eleve == null || eleve.getRemarques().isEmpty()) {
            return 0;
        }
        return eleve.getRemarques().split("\n").length;
    }
    
    
    public String[] filtrerRemarquesParType(Eleve eleve, String type) {
        if (eleve == null || type == null || eleve.getRemarques().isEmpty()) {
            return new String[0];
        }
        
        String typeRecherche = "[" + type.toUpperCase() + "]";
        String[] toutesRemarques = eleve.getRemarques().split("\n");
        java.util.List<String> resultat = new java.util.ArrayList<>();
        
        for (String remarque : toutesRemarques) {
            if (remarque.startsWith(typeRecherche)) {
                resultat.add(remarque);
            }
        }
        return resultat.toArray(new String[0]);
    }
    
    
    private String formaterRemarque(String remarque) {
        return String.format("%s - %s", 
            LocalDateTime.now().format(FORMATTER), 
            remarque);
    }
    
    
    public void clearRemarques(Eleve eleve) {
        if (eleve != null) {
            eleve.setRemarques("");
        }
    }
    
    
    public String genererRapportRemarques(Eleve eleve) {
        if (eleve == null) {
            return "Aucun élève spécifié";
        }
        
        StringBuilder rapport = new StringBuilder();
        rapport.append(String.format("Rapport des remarques - %s\n", eleve.getNomComplet()));
        rapport.append("=".repeat(50)).append("\n");
        
        String[] remarques = getRemarquesTableau(eleve);
        if (remarques.length == 0) {
            rapport.append("Aucune remarque enregistrée.\n");
        } else {
            for (int i = 0; i < remarques.length; i++) {
                rapport.append(String.format("%d. %s\n", i + 1, remarques[i]));
            }
        }
        
        return rapport.toString();
    }
}
