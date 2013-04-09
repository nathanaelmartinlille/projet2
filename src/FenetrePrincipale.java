import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import lecteurMP3.LecteurMP3Controlleur;
import lecteurMP3.LecteurMP3Vue;

/* Test github */

public class FenetrePrincipale {

	private JFrame frameFenetrePrincipale; 
	private LecteurMP3Vue vueLecteurMP3;
	private RechercheVue vueRecherche;
	private PlayListMusique vueListeMusiques;

	public FenetrePrincipale() {
		initComposant();
		initFenetre();
	}


	private void initComposant() {
		LecteurMP3Controlleur controlleur = new LecteurMP3Controlleur();
		this.vueLecteurMP3 = controlleur.getVue();
		RechercheControlleur controlleurRecherche = new RechercheControlleur();
		this.vueRecherche = controlleurRecherche.vue;
		PlayListMusiquesControlleur controlleurListeMusiques = new PlayListMusiquesControlleur();
		this.vueListeMusiques = controlleurListeMusiques.vue;
	}


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
