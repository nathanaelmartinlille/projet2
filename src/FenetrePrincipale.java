import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;


public class FenetrePrincipale {

	private JFrame frameFenetrePrincipale; 
	private PanelLecteurMP3 lecteurMP3;

	public FenetrePrincipale() {
		initComposant();
		initFenetre();
	}


	private void initComposant() {
		lecteurMP3 = new PanelLecteurMP3();
	}


	private void initFenetre() {
		frameFenetrePrincipale = new JFrame();
		frameFenetrePrincipale.add(lecteurMP3);
		frameFenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameFenetrePrincipale.setSize(350, 700);
		frameFenetrePrincipale.setMinimumSize(new Dimension(100,100));
		frameFenetrePrincipale.setLayout(new GridLayout(2, 2));
		frameFenetrePrincipale.setVisible(true);
	}

	public static void main(String[] args) {
		new FenetrePrincipale();
	}

}
