package org.eidd.poa.school.planner.vue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eidd.poa.school.planner.controleur.ControleurPrincipal;
import org.eidd.poa.school.planner.modele.Eleve;
import org.eidd.poa.school.planner.modele.Place;
import org.eidd.poa.school.planner.modele.PlanDeClasse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FenetrePrincipal extends JFrame {

    private static final Logger LOGGER = LogManager.getLogger(FenetrePrincipal.class);
    private static final long serialVersionUID = 1L;

    private ControleurPrincipal controleur;

    private PlanDeClasse plan;
    private JPanel panelPlan;
    private JButton[][] boutonsPlaces;
    
    private String titrePlan = "Plan de classe";


    private JTextArea infoEleve;

    private JButton btnAjouter;
    private JButton btnDeplacer;
    private JButton btnRetirer;
    private JButton btnAbsence;
    private JButton btnRetard;
    private JButton btnRemarque;
    private JButton btnSave;
    private JButton btnLoad;
    private JButton btnMoinsAbs;
    private JButton btnMoinsRet;
    private JButton btnMoinsRemarque;


    public FenetrePrincipal(PlanDeClasse plan) {
        super("Plan de classe");
        this.plan = plan;

        LOGGER.info("Initialisation de la fenêtre principale...");

        initialiserFenetre();
        initialiserComposants();
        construireGrillePlaces();

        LOGGER.info("Fenêtre principale initialisée avec succès.");
        setVisible(true);
    }

    public void setControleur(ControleurPrincipal controleur) {
        this.controleur = controleur;
        LOGGER.info("Contrôleur principal associé à la vue.");
        connecterBoutonsControleur();
    }

    private void initialiserFenetre() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        LOGGER.debug("Dimensions de fenêtre : {} x {}", getWidth(), getHeight());
    }

    private void initialiserComposants() {

        LOGGER.info("Initialisation des composants graphiques...");

        // --- Panel du plan de classe ---
        panelPlan = new JPanel(new GridLayout(plan.getRangees(), plan.getColonnes(), 5, 5));
        panelPlan.setBorder(new TitledBorder(titrePlan));
        add(panelPlan, BorderLayout.CENTER);

        // --- Panel d'infos ---
        infoEleve = new JTextArea(12, 26);
        infoEleve.setEditable(false);
        infoEleve.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollInfos = new JScrollPane(infoEleve);
        scrollInfos.setBorder(new TitledBorder("Détails élève"));
        add(scrollInfos, BorderLayout.EAST);

        // --- Panel bouton ---
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        btnAjouter = new JButton("Ajouter un élève");
        btnDeplacer = new JButton("Déplacer");
        btnRetirer = new JButton("Retirer");
        btnAbsence = new JButton("Absence");
        btnRetard = new JButton("Retard");
        btnRemarque = new JButton("Remarque +");
        btnMoinsRemarque = new JButton("Remarque -");
        btnSave = new JButton("Enregistrer");
        btnLoad = new JButton("Charger");
        btnMoinsAbs = new JButton("- Abs");
        btnMoinsRet = new JButton("- Ret");
        
        // Eleve
        JPanel panelEleve = new JPanel(new GridLayout(2, 2, 5, 5));
        panelEleve.setBorder(BorderFactory.createTitledBorder("Élève"));
        panelEleve.add(btnAjouter);
        panelEleve.add(btnDeplacer);
        panelEleve.add(btnRetirer);

        // Discipline (Absence / Retard)
        JPanel panelDiscipline = new JPanel(new GridLayout(2, 2, 5, 5));
        panelDiscipline.setBorder(BorderFactory.createTitledBorder("Discipline"));
        panelDiscipline.add(btnAbsence);
        panelDiscipline.add(btnRetard);
        panelDiscipline.add(btnMoinsAbs);
        panelDiscipline.add(btnMoinsRet);

        // Remarques
        JPanel panelRemarque = new JPanel(new GridLayout(2, 1, 5, 5));
        panelRemarque.setBorder(BorderFactory.createTitledBorder("Remarques"));
        panelRemarque.add(btnRemarque);
        panelRemarque.add(btnMoinsRemarque);

        panelBoutons.add(panelEleve);
        panelBoutons.add(panelDiscipline);
        panelBoutons.add(panelRemarque);
        panelBoutons.add(btnSave);
        panelBoutons.add(btnLoad);

        add(panelBoutons, BorderLayout.SOUTH);

        LOGGER.info("Composants graphiques initialisés.");
    }


    public void mettreAJourPlan(PlanDeClasse nouveauPlan) {
        LOGGER.info("Mise à jour du plan affiché.");
        this.plan = nouveauPlan;
        construireGrillePlaces();
    }
    
    public void mettreAJourTitrePlan(String nouveauTitre) {
        this.titrePlan = nouveauTitre;
        panelPlan.setBorder(new TitledBorder(titrePlan));
        panelPlan.repaint();
    }
    
    private String tooltipPourPlace(Place p) {
        Eleve e = p.getEleve();
        int ligne = p.getRangee() + 1;   // pour afficher à partir de 1
        int colonne = p.getColonne() + 1;

        if (e == null) {
            return "Place libre (ligne " + ligne + ", colonne " + colonne + ")";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(e.getNomComplet())
          .append(" – Absences: ").append(e.getAbsences())
          .append(", Retards: ").append(e.getRetards());

        String rem = e.getRemarques();
        if (rem != null && !rem.isBlank()) {
            String firstLine = rem.split("\n")[0].trim();
            if (firstLine.length() > 40) {
                firstLine = firstLine.substring(0, 37) + "...";
            }
            sb.append(" | Remarque: ").append(firstLine);
        }

        return sb.toString();
    }



    private void construireGrillePlaces() {

        LOGGER.info("Construction de la grille des places...");

        int rMax = plan.getRangees();
        int cMax = plan.getColonnes();
        boutonsPlaces = new JButton[rMax][cMax];

        panelPlan.removeAll();

        for (int r = 0; r < rMax; r++) {
            for (int c = 0; c < cMax; c++) {

                Place p = plan.obtenirPlace(r, c);
                JButton btn = new JButton();

                int rr = r, cc = c;
                btn.addActionListener(ev -> {
                    LOGGER.debug("Clic sur place ({}, {}).", rr, cc);
                    if (controleur != null) controleur.gererClicPlace(p);
                });

                boutonsPlaces[r][c] = btn;
                panelPlan.add(btn);
            }
        }

        panelPlan.revalidate();
        panelPlan.repaint();

        LOGGER.info("Grille des places construite.");
    }


    public void rafraichirPlan(PlanDeClasse plan, Place placeSelectionnee) {

        LOGGER.debug("Rafraîchissement du plan...");

        this.plan = plan;
        Place[][] grille = plan.getPlaces();

        for (int r = 0; r < grille.length; r++) {
            for (int c = 0; c < grille[r].length; c++) {

                Place p = grille[r][c];
                JButton btn = boutonsPlaces[r][c];

                Eleve e = p.getEleve();
                if (e != null) {
                    btn.setText(e.getNomComplet());
                    btn.setBackground(couleurSelonDiscipline(e));
                } else {
                    btn.setText("Place libre");
                    btn.setBackground(Color.LIGHT_GRAY);
                }
                
                btn.setToolTipText(tooltipPourPlace(p));

                if (p == placeSelectionnee) {
                    LOGGER.debug("Place sélectionnée ({}, {}).", r, c);
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                } else {
                    btn.setBorder(UIManager.getBorder("Button.border"));
                }
            }
        }

        panelPlan.repaint();
    }


    private Color couleurSelonDiscipline(Eleve e) {
        int score = e.getAbsences() + e.getRetards();
        // couleurs pastel + lisibles
        if (score >= 3) return new Color(255, 205, 210);     // rouge très clair
        if (score == 2) return new Color(255, 236, 179);     // jaune pastel
        if (score == 1) return new Color(187, 222, 251);     // bleu pastel
        return new Color(200, 230, 201); 
    }


    public void afficherInfoEleve(Eleve e) {
        if (e == null) {
            infoEleve.setText("Cette place est libre.");
            LOGGER.debug("Affichage : place libre.");
            return;
        }

        LOGGER.debug("Affichage infos élève : {}", e.getNomComplet());

        StringBuilder sb = new StringBuilder();
        sb.append("Nom : ").append(e.getNomComplet()).append("\n");
        sb.append("Absences : ").append(e.getAbsences()).append("\n");
        sb.append("Retards : ").append(e.getRetards()).append("\n\n");

        String rem = e.getRemarques().trim();
        if (rem.isEmpty()) {
            sb.append("Remarques : aucune\n");
        } else {
            sb.append("Remarques :\n");
            String[] lines = rem.split("\n");
            for (String line : lines) {
                sb.append("  • ").append(line).append("\n");
            }
        }

        infoEleve.setText(sb.toString());
    }


    private void connecterBoutonsControleur() {

        LOGGER.info("Connexion des boutons au contrôleur...");

        btnAjouter.addActionListener(e -> { LOGGER.debug("Click Ajouter élève"); controleur.ajouterEleve(); });
        btnDeplacer.addActionListener(e -> { LOGGER.debug("Click Déplacer"); controleur.deplacerEleve(); });
        btnRetirer.addActionListener(e -> { LOGGER.debug("Click Retirer"); controleur.retirerEleve(); });
        btnAbsence.addActionListener(e -> { LOGGER.debug("Click +Absence"); controleur.ajouterAbsence(); });
        btnRetard.addActionListener(e -> { LOGGER.debug("Click +Retard"); controleur.ajouterRetard(); });
        btnMoinsAbs.addActionListener(e -> { LOGGER.debug("Click -Absence"); controleur.retirerAbsence(); });
        btnMoinsRet.addActionListener(e -> { LOGGER.debug("Click -Retard"); controleur.retirerRetard(); });
        btnRemarque.addActionListener(e -> { LOGGER.debug("Click Remarque+"); controleur.ajouterRemarque(); });
        btnMoinsRemarque.addActionListener(e -> { LOGGER.debug("Click Remarque-"); controleur.retirerRemarque(); });
        btnSave.addActionListener(e -> { LOGGER.info("Click Sauvegarder plan"); controleur.sauvegarderPlan(); });
        btnLoad.addActionListener(e -> { LOGGER.info("Click Charger plan"); controleur.chargerPlan(); });

        LOGGER.info("Tous les boutons sont maintenant connectés.");
    }


    public JFrame getFrame() {
        return this;
    }
}
