package org.eidd.poa.school.planner.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests unitaires pour la classe Professeur
 * @author POA-2025-Groupe1
 */
class ProfesseurTest {

    private Professeur professeur;
    private static final int ID_VALIDE = 1;
    private static final String NOM_VALIDE = "Dupont";
    private static final String PRENOM_VALIDE = "Pierre";
    private static final String MATIERE_VALIDE = "Mathématiques";

    @BeforeEach
    void setUp() {
        professeur = new Professeur(ID_VALIDE, NOM_VALIDE, PRENOM_VALIDE, MATIERE_VALIDE);
    }

    // ========== TESTS DU CONSTRUCTEUR ==========

    @Test
    @DisplayName("Construction avec paramètres valides")
    void testConstructeurValide() {
        assertNotNull(professeur);
        assertEquals(ID_VALIDE, professeur.getId());
        assertEquals(NOM_VALIDE, professeur.getNom());
        assertEquals(PRENOM_VALIDE, professeur.getPrenom());
        assertEquals(MATIERE_VALIDE, professeur.getMatiere());
        assertTrue(professeur.getClassesEnseignees().isEmpty());
        assertEquals(0, professeur.getNombreClasses());
    }

    @Test
    @DisplayName("Construction avec espaces dans les chaînes")
    void testConstructeurAvecEspaces() {
        Professeur prof = new Professeur(2, "  Martin  ", "  Marie  ", "  Français  ");
        assertEquals("Martin", prof.getNom());
        assertEquals("Marie", prof.getPrenom());
        assertEquals("Français", prof.getMatiere());
    }

    @Test
    @DisplayName("Construction avec ID invalide (0)")
    void testConstructeurIdZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Professeur(0, NOM_VALIDE, PRENOM_VALIDE, MATIERE_VALIDE)
        );
        assertEquals("L'ID doit être positif", exception.getMessage());
    }

    @Test
    @DisplayName("Construction avec ID négatif")
    void testConstructeurIdNegatif() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Professeur(-1, NOM_VALIDE, PRENOM_VALIDE, MATIERE_VALIDE)
        );
        assertEquals("L'ID doit être positif", exception.getMessage());
    }

    @Test
    @DisplayName("Construction avec nom null")
    void testConstructeurNomNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Professeur(ID_VALIDE, null, PRENOM_VALIDE, MATIERE_VALIDE)
        );
        assertEquals("Le nom ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Construction avec nom vide")
    void testConstructeurNomVide() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Professeur(ID_VALIDE, "", PRENOM_VALIDE, MATIERE_VALIDE)
        );
        assertEquals("Le nom ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Construction avec nom contenant uniquement des espaces")
    void testConstructeurNomEspaces() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Professeur(ID_VALIDE, "   ", PRENOM_VALIDE, MATIERE_VALIDE)
        );
        assertEquals("Le nom ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Construction avec prénom null")
    void testConstructeurPrenomNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Professeur(ID_VALIDE, NOM_VALIDE, null, MATIERE_VALIDE)
        );
        assertEquals("Le prénom ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Construction avec matière null")
    void testConstructeurMatiereNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Professeur(ID_VALIDE, NOM_VALIDE, PRENOM_VALIDE, null)
        );
        assertEquals("La matière ne peut pas être vide", exception.getMessage());
    }

    // ========== TESTS DES GETTERS ==========

    @Test
    @DisplayName("Test des getters")
    void testGetters() {
        assertEquals(ID_VALIDE, professeur.getId());
        assertEquals(NOM_VALIDE, professeur.getNom());
        assertEquals(PRENOM_VALIDE, professeur.getPrenom());
        assertEquals(MATIERE_VALIDE, professeur.getMatiere());
    }

    @Test
    @DisplayName("Test getClassesEnseignees retourne une copie")
    void testGetClassesEnseigneesCopie() {
        professeur.ajouterClasse("6ème A");
        List<String> classes = professeur.getClassesEnseignees();
        classes.add("5ème B"); // Ne doit pas affecter l'original
        
        assertEquals(1, professeur.getNombreClasses());
        assertTrue(professeur.enseigneClasse("6ème A"));
        assertFalse(professeur.enseigneClasse("5ème B"));
    }

    // ========== TESTS DES SETTERS ==========

    @Test
    @DisplayName("Test setNom valide")
    void testSetNomValide() {
        professeur.setNom("Durand");
        assertEquals("Durand", professeur.getNom());
    }

    @Test
    @DisplayName("Test setNom null")
    void testSetNomNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> professeur.setNom(null)
        );
        assertEquals("Le nom ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Test setPrenom valide")
    void testSetPrenomValide() {
        professeur.setPrenom("Jean");
        assertEquals("Jean", professeur.getPrenom());
    }

    @Test
    @DisplayName("Test setPrenom vide")
    void testSetPrenomVide() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> professeur.setPrenom("")
        );
        assertEquals("Le prénom ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Test setMatiere valide")
    void testSetMatiereValide() {
        professeur.setMatiere("Physique");
        assertEquals("Physique", professeur.getMatiere());
    }

    // ========== TESTS DES MÉTHODES MÉTIER ==========

    @Test
    @DisplayName("Ajout d'une classe valide")
    void testAjouterClasseValide() {
        boolean resultat = professeur.ajouterClasse("6ème A");
        
        assertTrue(resultat);
        assertTrue(professeur.enseigneClasse("6ème A"));
        assertEquals(1, professeur.getNombreClasses());
        assertEquals(1, professeur.getClassesEnseignees().size());
    }

    @Test
    @DisplayName("Ajout d'une classe avec espaces")
    void testAjouterClasseAvecEspaces() {
        boolean resultat = professeur.ajouterClasse("  6ème A  ");
        
        assertTrue(resultat);
        assertTrue(professeur.enseigneClasse("6ème A"));
    }

    @Test
    @DisplayName("Ajout d'une classe dupliquée")
    void testAjouterClasseDupliquee() {
        professeur.ajouterClasse("5ème B");
        boolean resultat = professeur.ajouterClasse("5ème B");
        
        assertFalse(resultat);
        assertEquals(1, professeur.getNombreClasses());
    }

    @Test
    @DisplayName("Ajout d'une classe null")
    void testAjouterClasseNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> professeur.ajouterClasse(null)
        );
        assertEquals("Le nom de la classe ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Ajout d'une classe vide")
    void testAjouterClasseVide() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> professeur.ajouterClasse("")
        );
        assertEquals("Le nom de la classe ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Suppression d'une classe existante")
    void testSupprimerClasseExistante() {
        professeur.ajouterClasse("4ème C");
        boolean resultat = professeur.supprimerClasse("4ème C");
        
        assertTrue(resultat);
        assertFalse(professeur.enseigneClasse("4ème C"));
        assertEquals(0, professeur.getNombreClasses());
    }

    @Test
    @DisplayName("Suppression d'une classe inexistante")
    void testSupprimerClasseInexistante() {
        boolean resultat = professeur.supprimerClasse("Classe Inexistante");
        assertFalse(resultat);
    }

    @Test
    @DisplayName("Suppression d'une classe null")
    void testSupprimerClasseNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> professeur.supprimerClasse(null)
        );
        assertEquals("Le nom de la classe ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Test enseigneClasse")
    void testEnseigneClasse() {
        professeur.ajouterClasse("3ème D");
        
        assertTrue(professeur.enseigneClasse("3ème D"));
        assertTrue(professeur.enseigneClasse("  3ème D  "));
        assertFalse(professeur.enseigneClasse("Classe Inexistante"));
        assertFalse(professeur.enseigneClasse(null));
    }

    @Test
    @DisplayName("Test getNombreClasses")
    void testGetNombreClasses() {
        assertEquals(0, professeur.getNombreClasses());
        professeur.ajouterClasse("6ème A");
        assertEquals(1, professeur.getNombreClasses());
        professeur.ajouterClasse("5ème B");
        assertEquals(2, professeur.getNombreClasses());
        professeur.supprimerClasse("6ème A");
        assertEquals(1, professeur.getNombreClasses());
    }

    @Test
    @DisplayName("Test getNomComplet")
    void testGetNomComplet() {
        assertEquals("Pierre Dupont", professeur.getNomComplet());
        
        professeur.setPrenom("Jean");
        professeur.setNom("Martin");
        assertEquals("Jean Martin", professeur.getNomComplet());
    }

    @Test
    @DisplayName("Test getDescription")
    void testGetDescription() {
        String description = professeur.getDescription();
        assertTrue(description.contains("Professeur Pierre Dupont"));
        assertTrue(description.contains("Mathématiques"));
        assertTrue(description.contains("0 classe(s)"));
        
        professeur.ajouterClasse("6ème A");
        professeur.ajouterClasse("5ème B");
        description = professeur.getDescription();
        assertTrue(description.contains("2 classe(s)"));
    }

    @Test
    @DisplayName("Test clearClasses")
    void testClearClasses() {
        professeur.ajouterClasse("6ème A");
        professeur.ajouterClasse("5ème B");
        assertEquals(2, professeur.getNombreClasses());
        
        professeur.clearClasses();
        assertEquals(0, professeur.getNombreClasses());
        assertTrue(professeur.getClassesEnseignees().isEmpty());
    }

    // ========== TESTS DES MÉTHODES OBJECT ==========

    @Test
    @DisplayName("Test toString")
    void testToString() {
        String toString = professeur.toString();
        assertTrue(toString.contains("Professeur"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nom='Dupont'"));
        assertTrue(toString.contains("prenom='Pierre'"));
        assertTrue(toString.contains("matiere='Mathématiques'"));
    }

    @Test
    @DisplayName("Test equals - même instance")
    void testEqualsMemeInstance() {
        assertEquals(professeur, professeur);
    }

    @Test
    @DisplayName("Test equals - instances égales")
    void testEqualsInstancesEgales() {
        Professeur memeProfesseur = new Professeur(ID_VALIDE, NOM_VALIDE, PRENOM_VALIDE, MATIERE_VALIDE);
        assertEquals(professeur, memeProfesseur);
    }

    
    @DisplayName("Test equals - instances différentes")
    void testEqualsInstancesDifferentes() {
        Professeur autreProfesseur = new Professeur(2, "Martin", "Marie", "Français");
        assertNotEquals(professeur, autreProfesseur);
    }

    
    @DisplayName("Test equals - null")
    void testEqualsNull() {
        assertNotEquals(professeur, null);
    }

    @Test
    @DisplayName("Test equals - autre type d'objet")
    void testEqualsAutreType() {
        assertNotEquals(professeur, "une chaîne");
    }

    @Test
    @DisplayName("Test hashCode cohérent avec equals")
    void testHashCode() {
        Professeur memeProfesseur = new Professeur(ID_VALIDE, NOM_VALIDE, PRENOM_VALIDE, MATIERE_VALIDE);
        assertEquals(professeur.hashCode(), memeProfesseur.hashCode());
    }

    // ========== TEST DE SCÉNARIO COMPLET ==========

    @Test
    @DisplayName("Scénario complet d'utilisation")
    void testScenarioComplet() {
        // Création d'un professeur
        Professeur prof = new Professeur(10, "Leclerc", "Sophie", "Physique");
        
        // Ajout de classes
        assertTrue(prof.ajouterClasse("Terminale S"));
        assertTrue(prof.ajouterClasse("1ère S"));
        assertFalse(prof.ajouterClasse("Terminale S")); // Dupliqué
        
        // Vérifications
        assertEquals(2, prof.getNombreClasses());
        assertTrue(prof.enseigneClasse("Terminale S"));
        assertTrue(prof.enseigneClasse("1ère S"));
        
        // Modification des informations
        prof.setNom("Dubois");
        prof.setMatiere("Physique-Chimie");
        assertEquals("Dubois", prof.getNom());
        assertEquals("Physique-Chimie", prof.getMatiere());
        assertEquals("Sophie Dubois", prof.getNomComplet());
        
        // Suppression
        assertTrue(prof.supprimerClasse("1ère S"));
        assertEquals(1, prof.getNombreClasses());
        
        // Réinitialisation
        prof.clearClasses();
        assertEquals(0, prof.getNombreClasses());
    }

    @Test
    @DisplayName("Test avec plusieurs classes")
    void testAvecPlusieursClasses() {
        professeur.ajouterClasse("6ème A");
        professeur.ajouterClasse("5ème B");
        professeur.ajouterClasse("4ème C");
        
        assertEquals(3, professeur.getNombreClasses());
        assertTrue(professeur.enseigneClasse("6ème A"));
        assertTrue(professeur.enseigneClasse("5ème B"));
        assertTrue(professeur.enseigneClasse("4ème C"));
        
        // Suppression au milieu
        assertTrue(professeur.supprimerClasse("5ème B"));
        assertEquals(2, professeur.getNombreClasses());
        assertFalse(professeur.enseigneClasse("5ème B"));
    }
}