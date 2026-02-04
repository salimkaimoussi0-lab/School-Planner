package org.eidd.poa.school.planner.modele;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Représente un plan de classe sous forme de grille de places.
 */  
//test
public class PlanDeClasse {

    private static final Logger LOGGER = LogManager.getLogger(PlanDeClasse.class);

    private final int rangees;
    private final int colonnes;
    private final Place[][] places;

    /**
     * Crée un plan de classe avec le nombre de rangées et colonnes souhaité.
     */
    public PlanDeClasse(int rangees, int colonnes) {
        if (rangees <= 0 || colonnes <= 0) {
            LOGGER.error("Tentative de création de PlanDeClasse avec dimensions invalides : {}x{}",
                    rangees, colonnes);
            throw new IllegalArgumentException("Le nombre de rangées et de colonnes doit être > 0");
        }

        this.rangees = rangees;
        this.colonnes = colonnes;
        this.places = new Place[rangees][colonnes];

        LOGGER.info("Création d'un PlanDeClasse de {} rangée(s) × {} colonne(s).",
                rangees, colonnes);

        initialiserPlaces();
    }

    /**
     * Initialise toutes les places de la grille.
     */
    private void initialiserPlaces() {
        for (int r = 0; r < rangees; r++) {
            for (int c = 0; c < colonnes; c++) {
                places[r][c] = new Place(r, c);
            }
        }
        LOGGER.debug("Toutes les places du PlanDeClasse ont été initialisées.");
    }

    public int getRangees() {
        return rangees;
    }

    public int getColonnes() {
        return colonnes;
    }

    /**
     * Renvoie la place à la position (rangee, colonne).
     * @throws IndexOutOfBoundsException si les coordonnées sont invalides.
     */
    public Place obtenirPlace(int rangee, int colonne) {
        if (rangee < 0 || rangee >= rangees || colonne < 0 || colonne >= colonnes) {
            LOGGER.error("obtenirPlace() appelé avec indices invalides : ({}, {}) "
                    + "pour un plan de dimensions {}x{}.",
                    rangee, colonne, rangees, colonnes);
            throw new IndexOutOfBoundsException("Indices de place invalides : ("
                    + rangee + "," + colonne + ")");
        }
        return places[rangee][colonne];
    }

    /**
     * Retourne le tableau brut des places (si besoin pour la vue).
     */
    public Place[][] getPlaces() {
        return places;
    }

    /**
     * Compte les places libres dans le plan.
     */
    public int compterPlacesLibres() {
        int libres = 0;
        for (int r = 0; r < rangees; r++) {
            for (int c = 0; c < colonnes; c++) {
                if (places[r][c].estLibre()) {
                    libres++;
                }
            }
        }
        LOGGER.debug("compterPlacesLibres() → {} places libres dans le plan.", libres);
        return libres;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PlanDeClasse " + rangees + "x" + colonnes + "\n");
        for (int r = 0; r < rangees; r++) {
            for (int c = 0; c < colonnes; c++) {
                Eleve e = places[r][c].getEleve();
                if (e == null) {
                    sb.append("[ --- ]");
                } else {
                    sb.append("[ ")
                      .append(e.getPrenom().charAt(0))
                      .append(".")
                      .append(e.getNom())
                      .append(" ]");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
