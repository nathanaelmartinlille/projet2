import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javazoom.jl.decoder.JavaLayerException;


public class LecteurMP3Vue extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	LecteurMP3Controlleur controlleur;


	JButton play, avant, arriere;
	JSlider sliderLecture;
	JPanel panelEcouteCourante;
	JLabel ecouteActuelle;

	public LecteurMP3Vue(LecteurMP3Controlleur controlleur) {
		this.controlleur = controlleur;
		this.setLayout(new BorderLayout());
		initComposantPrincipaux();
		initPanelLectureCourante();
		initHandler();
		
		this.add(play, BorderLayout.CENTER);
		this.add(avant, BorderLayout.WEST);
		this.add(arriere, BorderLayout.EAST);
		this.add(panelEcouteCourante, BorderLayout.SOUTH);

		
	}
	
	private void initPanelLectureCourante() {
		panelEcouteCourante = new JPanel();
		panelEcouteCourante.setLayout(new GridLayout(2, 1));
		// init du slider de 0 ˆ 100
		sliderLecture = new JSlider();
		sliderLecture.setValue(0);
		
		ecouteActuelle = new JLabel();
		ecouteActuelle.setText("aucune chanson en cours de lecture");
		panelEcouteCourante.add(sliderLecture);
		panelEcouteCourante.add(ecouteActuelle);
		
	}

	public void initComposantPrincipaux(){
		play = new JButton(">");
		avant = new JButton("AV");
		arriere = new JButton("AR");
		
	}
	

	private void initHandler() {
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controlleur.playPauseControlleur();
				} catch (JavaLayerException e1) {
					JOptionPane.showMessageDialog(null,
							"Erreur lors de l'ouverture du fichier",
							"Erreur durant l'ouverture du fichier, fichier introuvable",
							JOptionPane.ERROR_MESSAGE);
					controlleur.setStatus(-1);
				}
				if(Constantes.PAUSE.equals(play.getText())){
					play.setText(Constantes.LECTURE);

				}else{
					play.setText(Constantes.PAUSE);
				}
			}
		});

		sliderLecture.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				controlleur.setPosition(controlleur.getPlayer().getDuration() * sliderLecture.getValue() /100);
			}
		});
	}
	// l'objet en parametre 
	@Override
	public void update(Observable o, Object avancement) {
		// on recupere l'observable qui est le lecteur MP3
		LecteurMP3Modele lecteurMP3Modele = (LecteurMP3Modele) o;
		// on va update les textfield avec le nom de la musique, ainsi que la position actuelle de la lecture
		//sliderLecture.setValue(avancementBis);
		ecouteActuelle.setText("en ecoute : " + lecteurMP3Modele.musiqueActuelle.toString());
		System.out.println("PositionEvent avec position pourcent =" + avancement); 

	}

}
