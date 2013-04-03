import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

/* Test github */

public class FenetrePrincipale {

	private JFrame frameFenetrePrincipale; 
	private PanelLecteurMP3 lecteurMP3;
	private PanelRecherche rechercheDynamique;
	private PanelMusiques tableMusiques;

	public FenetrePrincipale() {
		initComposant();
		initFenetre();
	}


	private void initComposant() {
		lecteurMP3 = new PanelLecteurMP3();
		rechercheDynamique = new PanelRecherche();
		tableMusiques = new PanelMusiques();
	}


	private void initFenetre() {
		frameFenetrePrincipale = new JFrame();
		frameFenetrePrincipale.add(rechercheDynamique);
		frameFenetrePrincipale.add(lecteurMP3);
		frameFenetrePrincipale.add(tableMusiques);
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
