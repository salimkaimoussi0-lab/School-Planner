package org.eidd.poa.school.planner.modele;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Représente une place dans le plan de classe (une chaise / un bureau).
 */
public class Place {

    private static final Logger LOGGER = LogManager.getLogger(Place.class);

    private final int rangee;
    private final int colonne;
    private Eleve eleve; // peut être null si la place est libre

    public Place(int rangee, int colonne) {
        this.rangee = rangee;
        this.colonne = colonne;
        this.eleve = null;

        LOGGER.debug("Création d'une nouvelle place en ({}, {}).", rangee, colonne);
    }

    public int getRangee() {
        return rangee;
    }

    public int getColonne() {
        return colonne;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public boolean estLibre() {
        return eleve == null;
    }

    /**
     * Affecte un élève à cette place.
     */
    public void affecter(Eleve eleve) {
        if (eleve == null) {
            LOGGER.warn("Tentative d'affecter un élève null à la place ({}, {}).", rangee, colonne);
            return;
        }
        LOGGER.info("Affectation de l'élève {} à la place ({}, {}).",
                eleve.getNomComplet(), rangee+1, colonne+1);  // Pour respecter le choix arbitraire (1,1) étant la première place
        this.eleve = eleve;
    }

    /**
     * Libère la place (plus aucun élève dessus).
     */
    public void liberer() {
        if (eleve != null) {
            LOGGER.info("Libération de la place ({}, {}) occupée par {}.",
                    rangee, colonne, eleve.getNomComplet());
        } else {
            LOGGER.debug("liberer() appelée sur une place déjà vide ({}, {}).", rangee, colonne);
        }
        this.eleve = null;
    }

    @Override
    public String toString() {
        if (eleve == null) {
            return "Place (" + rangee + "," + colonne + ") - libre";
        }
        return "Place (" + rangee + "," + colonne + ") - " + eleve.getNomComplet();
    }
}
