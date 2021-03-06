package core;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import playlist.PlayListMusiqueVue;
import playlist.PlayListMusiquesControlleur;
import recherche.RechercheControlleur;
import recherche.RechercheVue;

import lecteurMP3.LecteurMP3Controlleur;
import lecteurMP3.LecteurMP3Vue;

/**La fenetre principale gere toutes les vues et controlleur.
 */
public class FenetrePrincipale {

	private JFrame frameFenetrePrincipale; 
	private LecteurMP3Vue vueLecteurMP3;
	private RechercheVue vueRecherche;
	private PlayListMusiqueVue vueListeMusiques;

	/**
	 * Constructeur par defaut qui va initialiser les composants et la fenetre.
	 */
	public FenetrePrincipale() {
		initComposant();
		initFenetre();
	}


	/**
	 * Cette methode initialise les controlleurs des differentes vue.
	 */
	private void initComposant() {
		LecteurMP3Controlleur controlleurLecteurMP3 = new LecteurMP3Controlleur();
		this.vueLecteurMP3 = controlleurLecteurMP3.getVue();
		RechercheControlleur controlleurRecherche = new RechercheControlleur();
		this.vueRecherche = controlleurRecherche.getVue();
		PlayListMusiquesControlleur controlleurListeMusiques = new PlayListMusiquesControlleur(controlleurLecteurMP3);
		this.vueListeMusiques = controlleurListeMusiques.getVue();
	}


	/**
	 * Methode qui va initialiser la fenetre.
	 */
	private void initFenetre() {
		frameFenetrePrincipale = new JFrame();
		frameFenetrePrincipale.add(vueRecherche);
		frameFenetrePrincipale.add(vueLecteurMP3);
		frameFenetrePrincipale.add(vueListeMusiques);
		frameFenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameFenetrePrincipale.setSize(900, 500);
		frameFenetrePrincipale.setMinimumSize(new Dimension(100,100));
		frameFenetrePrincipale.setLayout(new GridLayout(3, 1));
		frameFenetrePrincipale.setVisible(true);
	}

	public static void main(String[] args) {
		new FenetrePrincipale();
	}

}
