package org.eidd.poa.school.planner.modele;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestEleve {

    @Test
    @DisplayName("Constructeur : l'élève doit être créé avec les valeurs par défaut")
    void testConstructeur() {
        Eleve e = new Eleve("Yassin", "Gdaiem", 20);

        assertEquals("Yassin", e.getNom());
        assertEquals("Gdaiem", e.getPrenom());
        assertEquals(20, e.getAge());
        assertEquals(10, e.getNoteDiscipline(), "La note par défaut doit être 10");
        assertEquals(0, e.getAbsences());
        assertEquals(0, e.getRetards());
        assertEquals("", e.getRemarques());
    }

    @Test
    @DisplayName("getNomComplet() doit retourner prenom + nom")
    void testGetNomComplet() {
        Eleve e = new Eleve("Ghayate", "Sarah", 21);
        assertEquals("Sarah Ghayate", e.getNomComplet());
    }

    @Test
    @DisplayName("ajouterAbsence() incrémente correctement")
    void testAjouterAbsence() {
        Eleve e = new Eleve("Nom", "Prenom", 20);
        e.ajouterAbsence();
        e.ajouterAbsence();

        assertEquals(2, e.getAbsences());
    }

    @Test
    @DisplayName("ajouterRetard() incrémente correctement")
    void testAjouterRetard() {
        Eleve e = new Eleve("Nom", "Prenom", 20);
        e.ajouterRetard();
        e.ajouterRetard();
        e.ajouterRetard();

        assertEquals(3, e.getRetards());
    }

    @Test
    @DisplayName("setAbsences() ne doit jamais produire de valeur négative")
    void testSetAbsencesNeverNegative() {
        Eleve e = new Eleve("Nom", "Prenom", 20);

        e.setAbsences(-5);
        assertEquals(0, e.getAbsences(),
            "Les absences doivent rester >= 0");

        e.setAbsences(3);
        assertEquals(3, e.getAbsences());
    }

    @Test
    @DisplayName("setRetards() ne doit jamais produire de valeur négative")
    void testSetRetardsNeverNegative() {
        Eleve e = new Eleve("Nom", "Prenom", 20);

        e.setRetards(-10);
        assertEquals(0, e.getRetards());

        e.setRetards(2);
        assertEquals(2, e.getRetards());
    }

    @Test
    @DisplayName("setRemarques() gère correctement null et les chaînes normales")
    void testSetRemarques() {
        Eleve e = new Eleve("Nom", "Prenom", 20);

        e.setRemarques(null);
        assertEquals("", e.getRemarques());

        e.setRemarques("Bonne participation");
        assertEquals("Bonne participation", e.getRemarques());
    }

    @Test
    @DisplayName("setNoteDiscipline() change la note correctement")
    void testSetNoteDiscipline() {
        Eleve e = new Eleve("Nom", "Prenom", 20);

        e.setNoteDiscipline(7);
        assertEquals(7, e.getNoteDiscipline());
    }

    @Test
    @DisplayName("toString() doit contenir le nom complet et les stats")
    void testToString() {
        Eleve e = new Eleve("Nom", "Prenom", 20);
        e.ajouterAbsence();
        e.ajouterRetard();

        String txt = e.toString();
        assertTrue(txt.contains("Prenom Nom"));
        assertTrue(txt.contains("Absences: 1"));
        assertTrue(txt.contains("Retards: 1"));
    }
}
