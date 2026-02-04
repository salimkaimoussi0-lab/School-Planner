package org.eidd.poa.school.planner.modele;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestPlanDeClasse {

    @Test
    @DisplayName("Constructeur : les dimensions doivent être respectées et toutes les places créées")
    void testConstructeurDimensionsValides() {
        int rangees = 3;
        int colonnes = 4;

        PlanDeClasse plan = new PlanDeClasse(rangees, colonnes);

        assertEquals(rangees, plan.getRangees());
        assertEquals(colonnes, plan.getColonnes());

        Place[][] places = plan.getPlaces();
        assertNotNull(places);
        assertEquals(rangees, places.length, "Le tableau de places doit avoir le bon nombre de rangées.");
        assertEquals(colonnes, places[0].length, "Chaque rangée doit avoir le bon nombre de colonnes.");

        for (int r = 0; r < rangees; r++) {
            for (int c = 0; c < colonnes; c++) {
                assertNotNull(places[r][c], "Chaque place de la grille doit être initialisée.");
                assertEquals(r, places[r][c].getRangee());
                assertEquals(c, places[r][c].getColonne());
            }
        }
    }

    @Test
    @DisplayName("Constructeur : dimensions invalides doivent lever une exception")
    void testConstructeurDimensionsInvalides() {
        assertThrows(IllegalArgumentException.class,
                () -> new PlanDeClasse(0, 3),
                "Une plan de classe avec 0 rangée doit lever une exception.");

        assertThrows(IllegalArgumentException.class,
                () -> new PlanDeClasse(3, 0),
                "Une plan de classe avec 0 colonne doit lever une exception.");

        assertThrows(IllegalArgumentException.class,
                () -> new PlanDeClasse(-1, -2),
                "Des dimensions négatives doivent lever une exception.");
    }

    @Test
    @DisplayName("obtenirPlace() doit renvoyer la bonne place pour des indices valides")
    void testObtenirPlaceValide() {
        PlanDeClasse plan = new PlanDeClasse(2, 3);
        Place p = plan.obtenirPlace(1, 2);

        assertNotNull(p);
        assertEquals(1, p.getRangee());
        assertEquals(2, p.getColonne());
    }

    @Test
    @DisplayName("obtenirPlace() doit lever une exception pour des indices invalides")
    void testObtenirPlaceInvalide() {
        PlanDeClasse plan = new PlanDeClasse(2, 3);

        assertThrows(IndexOutOfBoundsException.class,
                () -> plan.obtenirPlace(-1, 0));

        assertThrows(IndexOutOfBoundsException.class,
                () -> plan.obtenirPlace(0, -1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> plan.obtenirPlace(2, 0));

        assertThrows(IndexOutOfBoundsException.class,
                () -> plan.obtenirPlace(0, 3));
    }

    @Test
    @DisplayName("compterPlacesLibres() doit compter correctement les places libres")
    void testCompterPlacesLibres() {
        PlanDeClasse plan = new PlanDeClasse(2, 2);
        assertEquals(4, plan.compterPlacesLibres(),
                "Au départ, toutes les places doivent être libres.");

        Eleve e1 = new Eleve("Nom1", "Prenom1", 20);
        Eleve e2 = new Eleve("Nom2", "Prenom2", 21);

        plan.obtenirPlace(0, 0).affecter(e1);
        plan.obtenirPlace(1, 1).affecter(e2);

        assertEquals(2, plan.compterPlacesLibres(),
                "Après affectation de 2 élèves, il doit rester 2 places libres.");
    }

    @Test
    @DisplayName("getPlaces() et obtenirPlace() doivent être cohérents")
    void testGetPlacesEtObtenirPlaceCoherents() {
        PlanDeClasse plan = new PlanDeClasse(3, 3);
        Place[][] places = plan.getPlaces();

        for (int r = 0; r < plan.getRangees(); r++) {
            for (int c = 0; c < plan.getColonnes(); c++) {
                assertSame(places[r][c], plan.obtenirPlace(r, c),
                        "getPlaces()[r][c] et obtenirPlace(r,c) doivent renvoyer la même instance.");
            }
        }
    }
}
