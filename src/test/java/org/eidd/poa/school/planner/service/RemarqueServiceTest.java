package org.eidd.poa.school.planner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.eidd.poa.school.planner.modele.Eleve;

class RemarqueServiceTest {
    private RemarqueService remarqueService;
    private Eleve eleve;
    
    @BeforeEach
    void setUp() {
        remarqueService = new RemarqueService();
        eleve = new Eleve("Martin", "Jean", 12);
    }
    
    @Test
    void testAjouterRemarque() {
        remarqueService.ajouterRemarque(eleve, "Bon comportement");
        assertEquals(1, remarqueService.compterRemarques(eleve));
        assertFalse(eleve.getRemarques().isEmpty());
    }
    
    @Test
    void testAjouterRemarqueAvecType() {
        remarqueService.ajouterRemarque(eleve, "COMPORTEMENT", "Très participatif");
        String[] remarques = remarqueService.getRemarquesTableau(eleve);
        
        assertEquals(1, remarques.length);
        assertTrue(remarques[0].contains("[COMPORTEMENT]"));
    }
    
    @Test
    void testAjouterMultipleRemarques() {
        remarqueService.ajouterRemarque(eleve, "Première remarque");
        remarqueService.ajouterRemarque(eleve, "COMPORTEMENT", "Deuxième remarque");
        
        assertEquals(2, remarqueService.compterRemarques(eleve));
        String[] remarques = remarqueService.getRemarquesTableau(eleve);
        assertEquals(2, remarques.length);
    }
    
    @Test
    void testSupprimerDerniereRemarque() {
        remarqueService.ajouterRemarque(eleve, "Première remarque");
        remarqueService.ajouterRemarque(eleve, "Deuxième remarque");
        
        assertEquals(2, remarqueService.compterRemarques(eleve));
        assertTrue(remarqueService.supprimerDerniereRemarque(eleve));
        assertEquals(1, remarqueService.compterRemarques(eleve));
    }
    
    @Test
    void testClearRemarques() {
        remarqueService.ajouterRemarque(eleve, "Test remarque");
        remarqueService.clearRemarques(eleve);
        
        assertTrue(eleve.getRemarques().isEmpty());
        assertEquals(0, remarqueService.compterRemarques(eleve));
    }
    
    @Test
    void testGenererRapportRemarques() {
        remarqueService.ajouterRemarque(eleve, "COMPORTEMENT", "Excellent élève");
        String rapport = remarqueService.genererRapportRemarques(eleve);
        
        assertTrue(rapport.contains("Rapport des remarques"));
        assertTrue(rapport.contains("Jean Martin"));
        assertTrue(rapport.contains("Excellent élève"));
    }
    
    @Test
    void testEleveNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            remarqueService.ajouterRemarque(null, "Test");
        });
    }
    
    @Test
    void testRemarqueVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            remarqueService.ajouterRemarque(eleve, "");
        });
    }
}
