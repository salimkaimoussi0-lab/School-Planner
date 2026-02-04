package org.eidd.poa.school.planner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.eidd.poa.school.planner.modele.Eleve;

class AbsenceServiceTest {
    private AbsenceService absenceService;
    private Eleve eleve;
    
    @BeforeEach
    void setUp() {
        absenceService = new AbsenceService();
        eleve = new Eleve("Martin", "Jean", 12);
    }
    
    @Test
    void testIncrementerAbsence() {
        absenceService.incrementerAbsence(eleve);
        assertEquals(1, eleve.getAbsences());
        assertEquals(8, eleve.getNoteDiscipline()); // 10 - 2 points
    }
    
    @Test
    void testIncrementerRetard() {
        absenceService.incrementerRetard(eleve);
        assertEquals(1, eleve.getRetards());
        assertEquals(9, eleve.getNoteDiscipline()); // 10 - 1 point
    }
    
    @Test
    void testCombinaisonAbsencesRetards() {
        absenceService.incrementerAbsence(eleve); // -2 points
        absenceService.incrementerRetard(eleve);  // -1 point
        absenceService.incrementerRetard(eleve);  // -1 point
        
        assertEquals(1, eleve.getAbsences());
        assertEquals(2, eleve.getRetards());
        assertEquals(6, eleve.getNoteDiscipline()); // 10 - 2 - 1 - 1 = 6
    }
    
    @Test
    void testDecrementerAbsence() {
        absenceService.incrementerAbsence(eleve);
        absenceService.incrementerAbsence(eleve);
        absenceService.decrementerAbsence(eleve);
        
        assertEquals(1, eleve.getAbsences());
        assertEquals(8, eleve.getNoteDiscipline());
    }
    
    @Test
    void testReinitialiserCompteurs() {
        absenceService.incrementerAbsence(eleve);
        absenceService.incrementerRetard(eleve);
        absenceService.reinitialiserCompteurs(eleve);
        
        assertEquals(0, eleve.getAbsences());
        assertEquals(0, eleve.getRetards());
        assertEquals(10, eleve.getNoteDiscipline());
    }
    
    @Test
    void testEleveNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            absenceService.incrementerAbsence(null);
        });
    }
}
