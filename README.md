# 🎓 School Planner - Gestion de Classe Interactive

Application de bureau en Java pour la gestion administrative et pédagogique d'une classe. Ce projet a été conçu pour simplifier le suivi des élèves (absences, retards, placement) via une interface visuelle intuitive.

## 🚀 Fonctionnalités Clés
* **Plan de Classe Interactif :** Visualisation graphique de la salle de classe avec déplacement des élèves par clic (`Place`, `PlanDeClasse`).
* **Suivi Disciplinaire :** Calcul automatique de la note de discipline en fonction des absences et retards (Logique métier dans `AbsenceService`).
* **Persistance des Données (CSV) :** Système de sauvegarde et chargement complet de l'état de la classe sans base de données externe (`PlanIO.java`).
* **Journalisation :** Suivi des actions via Log4j2 pour le débogage et l'audit.

## 🛠️ Architecture Technique
Le projet respecte strictement le pattern **MVC (Modèle-Vue-Contrôleur)** :
* **Modèle :** Classes métiers (`Eleve`, `Professeur`) totalement découplées de l'interface.
* **Vue :** Interface Swing (`FenetrePrincipal`) qui ne fait qu'afficher les données.
* **Contrôleur :** `ControleurPrincipal` qui orchestre les interactions utilisateur.

## 🧩 Extrait de Code (Gestion de Fichiers)
Le module `PlanIO` gère la sérialisation des données en format CSV natif :

```java
// Exemple de sauvegarde optimisée (PlanIO.java)
public static void sauvegarderCsv(PlanDeClasse plan, File fichier) throws IOException {
    try (PrintWriter out = new PrintWriter(
            new OutputStreamWriter(new FileOutputStream(fichier), StandardCharsets.UTF_8))) {
        // Écriture des métadonnées et des élèves...
    }
}
