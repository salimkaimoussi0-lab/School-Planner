package org.eidd.poa.school.planner.donnee;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eidd.poa.school.planner.modele.Eleve;
import org.eidd.poa.school.planner.modele.PlanDeClasse;

/**
 * Stocke les données en mémoire.
 */
public class ReferentielMemoire {

    // Logger Log4j2 pour cette classe
    private static final Logger LOGGER = LogManager.getLogger(ReferentielMemoire.class);

    private PlanDeClasse plan;
    private final List<Eleve> eleves;

    public ReferentielMemoire() {
        this.eleves = new ArrayList<>();
        this.plan = new PlanDeClasse(4, 5); 

        LOGGER.info("Initialisation du référentiel mémoire...");
        initialiserDemo();
        LOGGER.info("Référentiel mémoire initialisé avec {} élève(s).", eleves.size());
    }

    /**
     * Initialise des données de démonstration en mémoire.
     */
    private void initialiserDemo() {
        LOGGER.debug("Début de l'initialisation des données de démonstration.");
        
        Eleve A = new Eleve("Gdaiem", "Yassin", 20);
        eleves.add(A);
        LOGGER.debug("Élève ajouté au référentiel : {}", A);

        A.ajouterAbsence();
        LOGGER.debug("Absence ajoutée pour l'élève {} (absences = {}).",
                A.getNomComplet(), A.getAbsences());

        Eleve[] tab = { A };
        int index = 0;
        for (int r = 0; r < plan.getRangees(); r++) {
            for (int c = 0; c < plan.getColonnes(); c++) {
                if (index < tab.length) {
                    plan.obtenirPlace(r, c).affecter(tab[index]);
                    LOGGER.debug("Élève {} affecté à la place ({}, {}).",
                            tab[index].getNomComplet(), r, c);
                    index++;
                }
            }
        }
        LOGGER.info("PlanDeClasse initialisé avec {} élève(s) placé(s).", index);
        LOGGER.debug("Fin de l'initialisation des données de démonstration.");
    }

    public PlanDeClasse getPlanActif() {
        LOGGER.debug("getPlanActif() renvoie un plan non null.");
        return plan;
    }

    public List<Eleve> getTousLesEleves() {
        LOGGER.debug("getTousLesEleves() appelé. Nombre d'élèves : {}", eleves.size());
        return eleves;
    }

    /**
     * Ajoute un élève au référentiel s'il n'y est pas déjà.
     */
    public void ajouterEleve(Eleve e) {
        if (!eleves.contains(e)) {
            eleves.add(e);
            LOGGER.info("Nouvel élève ajouté au référentiel : {}", e.getNomComplet());
        } else {
            LOGGER.info("ajouterEleve() : l'élève {} est déjà présent dans le référentiel.",
                    e.getNomComplet());
        }
    }

    /**
     * Permettra plus tard d'injecter un PlanDeClasse.
     */
    public void setPlan(PlanDeClasse plan) {
        this.plan = plan;
        LOGGER.info("PlanDeClasse mis à jour via setPlan().");
    }
    
    public void reinitialiser(PlanDeClasse nouveauPlan) {
        LOGGER.info("Réinitialisation du référentiel mémoire...");

                this.plan = nouveauPlan;

                this.eleves.clear();

        LOGGER.info("Référentiel mémoire réinitialisé. Nouveau plan : {} rangées, {} colonnes.",
                nouveauPlan.getRangees(), nouveauPlan.getColonnes());
    }
    
}
