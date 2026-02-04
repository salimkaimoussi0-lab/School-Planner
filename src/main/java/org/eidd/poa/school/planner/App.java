package org.eidd.poa.school.planner;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf; // look moderne type IntelliJ

import org.eidd.poa.school.planner.controleur.ControleurPrincipal;
import org.eidd.poa.school.planner.donnee.ReferentielMemoire;
import org.eidd.poa.school.planner.modele.PlanDeClasse;
import org.eidd.poa.school.planner.vue.FenetrePrincipal;

public class App {

    public static void main(String[] args) {

        try {
            // 1) Look & Feel moderne (base IntelliJ clair)
            UIManager.setLookAndFeel(new FlatIntelliJLaf());

            // 2) Personnalisation globale (arrondis, couleurs, focus…)

            // Couleur d’accent (boutons, focus, etc.)
            UIManager.put("Component.accentColor", new Color(52, 152, 219)); // bleu moderne

            // Arrondis des composants
            UIManager.put("Component.arc",       12);  // coins arrondis généraux
            UIManager.put("Button.arc",          16);  
            UIManager.put("TextComponent.arc",   10);

            // Bordure de focus très fine
            UIManager.put("Component.focusWidth",      1);
            UIManager.put("Button.innerFocusWidth",    0);

            // Scrollbars plus clean
            UIManager.put("ScrollBar.showButtons", false);

        } catch (Exception e) {
            System.err.println("Impossible d'appliquer FlatLaf, look par défaut utilisé.");
            e.printStackTrace();
        }

        // 3) Lancement de l’appli
        SwingUtilities.invokeLater(() -> {
            ReferentielMemoire ref = new ReferentielMemoire();
            PlanDeClasse plan = new PlanDeClasse(4, 5);
            ref.setPlan(plan);

            FenetrePrincipal vue = new FenetrePrincipal(plan);
            new ControleurPrincipal(ref, plan, vue);
        });
    }
}
