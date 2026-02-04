package org.eidd.poa.school.planner.donnee;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.eidd.poa.school.planner.modele.Eleve;
import org.eidd.poa.school.planner.modele.PlanDeClasse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la classe ReferentielMemoire.
 */
public class TestReferentielMemoire {

    @Test
    @DisplayName("Initialisation du référentiel : au moins un élève présent")
    void testInitialisationEleves() {
        ReferentielMemoire ref = new ReferentielMemoire();

        List<Eleve> eleves = ref.getTousLesEleves();
        assertNotNull(eleves, "La liste d'élèves ne doit pas être null.");
        assertFalse(eleves.isEmpty(), "La liste d'élèves devrait contenir au moins un élève de démo.");

        Eleve premier = eleves.get(0);
        assertNotNull(premier.getNom(), "Le nom du premier élève ne doit pas être null.");
        assertNotNull(premier.getPrenom(), "Le prénom du premier élève ne doit pas être null.");
    }

    @Test
    @DisplayName("ajouterEleve() ajoute un nouvel élève non présent")
    void testAjouterEleve() {
        ReferentielMemoire ref = new ReferentielMemoire();
        int tailleAvant = ref.getTousLesEleves().size();

        Eleve nouveau = new Eleve("Test", "JUnit", 20);
        ref.ajouterEleve(nouveau);

        int tailleApres = ref.getTousLesEleves().size();
        assertEquals(tailleAvant + 1, tailleApres,
                "ajouterEleve() doit augmenter la taille de la liste quand l'élève n'était pas présent.");
        assertTrue(ref.getTousLesEleves().contains(nouveau),
                "La liste doit contenir le nouvel élève ajouté.");
    }

    @Test
    @DisplayName("ajouterEleve() ignore un élève déjà présent")
    void testAjouterEleveDoublon() {
        ReferentielMemoire ref = new ReferentielMemoire();
        Eleve e = ref.getTousLesEleves().get(0);

        int tailleAvant = ref.getTousLesEleves().size();
        ref.ajouterEleve(e); // même élève
        int tailleApres = ref.getTousLesEleves().size();

        assertEquals(tailleAvant, tailleApres,
                "ajouterEleve() ne doit pas ajouter de doublon.");
    }

    @Test
    @DisplayName("setPlan() permet d'injecter un PlanDeClasse non null")
    void testSetPlan() {
        ReferentielMemoire ref = new ReferentielMemoire();
        PlanDeClasse planFictif = new PlanDeClasse(2, 3);

        ref.setPlan(planFictif);
        assertNotNull(ref.getPlanActif(), "Après setPlan(), le plan ne doit plus être null.");
        assertEquals(2, ref.getPlanActif().getRangees());
        assertEquals(3, ref.getPlanActif().getColonnes());
    }
}
