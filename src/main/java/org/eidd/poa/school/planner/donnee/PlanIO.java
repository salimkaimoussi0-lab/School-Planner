package org.eidd.poa.school.planner.donnee;

import org.eidd.poa.school.planner.modele.Eleve;
import org.eidd.poa.school.planner.modele.Place;
import org.eidd.poa.school.planner.modele.PlanDeClasse;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Gestion de sauvegarde/chargement du plan de classe en CSV.
 */
public class PlanIO {

    private static final String SEP = ";";

    /**
     * Sauvegarde un plan de classe dans un fichier CSV.
     */
    public static void sauvegarderCsv(PlanDeClasse plan, File fichier) throws IOException {
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(fichier), StandardCharsets.UTF_8))) {

            // 1) écrire les dimensions du plan
            out.println(plan.getRangees() + SEP + plan.getColonnes());

            // 2) écrire uniquement les places occupées
            for (int r = 0; r < plan.getRangees(); r++) {
                for (int c = 0; c < plan.getColonnes(); c++) {
                    Place p = plan.obtenirPlace(r, c);
                    Eleve e = p.getEleve();
                    if (e != null) {
                        String remarques = (e.getRemarques() == null) ? "" : e.getRemarques().replace("\n", "\\n");

                        out.println(
                                r + SEP + c + SEP +
                                e.getNom() + SEP +
                                e.getPrenom() + SEP +
                                e.getAge() + SEP +
                                e.getNoteDiscipline() + SEP +
                                e.getAbsences() + SEP +
                                e.getRetards() + SEP +
                                remarques
                        );
                    }
                }
            }
        }
    }

    /**
     * Charge un fichier CSV en créant un nouveau plan et en remplissant le référentiel.
     */
    public static PlanDeClasse chargerCsv(File fichier, ReferentielMemoire referentiel) throws IOException {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {

            String ligne = in.readLine();
            if (ligne == null) throw new IOException("Fichier CSV vide");

            // 1) Lecture dimensions
            String[] dims = ligne.split(SEP);
            int rangees = Integer.parseInt(dims[0]);
            int colonnes = Integer.parseInt(dims[1]);

            PlanDeClasse plan = new PlanDeClasse(rangees, colonnes);

            referentiel.reinitialiser(plan);

            // 2) Lecture contenu
            while ((ligne = in.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] cols = ligne.split(SEP, -1);

                int r = Integer.parseInt(cols[0]);
                int c = Integer.parseInt(cols[1]);
                String nom = cols[2];
                String prenom = cols[3];
                int age = Integer.parseInt(cols[4]);
                int note = Integer.parseInt(cols[5]);
                int abs = Integer.parseInt(cols[6]);
                int ret = Integer.parseInt(cols[7]);
                String remarques = cols[8].replace("\\n", "\n");

                Eleve e = new Eleve(nom, prenom, age);
                e.setNoteDiscipline(note);
                e.setAbsences(abs);
                e.setRetards(ret);
                e.setRemarques(remarques);

                referentiel.ajouterEleve(e);
                plan.obtenirPlace(r, c).affecter(e);
            }

            return plan;
        }
    }
}