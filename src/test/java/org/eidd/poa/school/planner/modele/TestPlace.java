package org.eidd.poa.school.planner.modele;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestPlace {

    @Test
    @DisplayName("Constructeur : une nouvelle place doit être libre")
    void testConstructeurPlace() {
        Place p = new Place(1, 2);

        assertEquals(1, p.getRangee());
        assertEquals(2, p.getColonne());
        assertNull(p.getEleve(), "Une nouvelle place ne doit pas avoir d'élève.");
        assertTrue(p.estLibre(), "Une nouvelle place doit être libre.");
    }

    @Test
    @DisplayName("affecter(Eleve) doit associer un élève à la place")
    void testAffecterEleve() {
        Place p = new Place(0, 0);
        Eleve e = new Eleve("Nom", "Prenom", 18);

        p.affecter(e);

        assertFalse(p.estLibre(), "Après affectation, la place ne doit plus être libre.");
        assertNotNull(p.getEleve(), "L'élève doit être enregistré sur la place.");
        assertEquals(e, p.getEleve(), "L'élève récupéré doit être celui affecté.");
    }

    @Test
    @DisplayName("liberer() doit rendre la place libre")
    void testLibererPlace() {
        Place p = new Place(0, 0);
        Eleve e = new Eleve("Nom", "Prenom", 18);
        p.affecter(e);

        p.liberer();

        assertTrue(p.estLibre(), "Après libération, la place doit redevenir libre.");
        assertNull(p.getEleve(), "Après libération, il ne doit plus y avoir d'élève sur la place.");
    }

    @Test
    @DisplayName("affecter(null) ne doit pas modifier la place")
    void testAffecterNull() {
        Place p = new Place(0, 0);

        p.affecter(null);  // selon ton implémentation, ça logge un warning et ne change rien

        assertTrue(p.estLibre(), "Après affecter(null), la place doit rester libre.");
        assertNull(p.getEleve(), "Aucun élève ne doit être associé.");
    }
    
    @Test
    @DisplayName("liberer() sur une place déjà libre doit fonctionner sans erreur")
    void testLibererPlaceDejaLibre() {
        Place p = new Place(1, 1);

        // Première libération sur une place vide (branche else)
        assertDoesNotThrow(() -> p.liberer(),
                "liberer() sur une place déjà libre ne doit pas lever d'exception.");

        assertTrue(p.estLibre(),
                "La place doit rester libre après libérer().");
    }


    @Test
    @DisplayName("toString() doit contenir coordonnées et éventuellement nom de l'élève")
    void testToString() {
        Place p = new Place(1, 1);
        String s1 = p.toString();
        assertTrue(s1.contains("1"), "toString() doit contenir la rangée/colonne.");
        assertTrue(s1.toLowerCase().contains("libre"), "toString() doit indiquer que la place est libre.");

        Eleve e = new Eleve("Doe", "John", 17);
        p.affecter(e);
        String s2 = p.toString();
        assertTrue(s2.contains("John Doe"), "toString() doit contenir le nom complet de l'élève lorsque la place est occupée.");
    }
}
