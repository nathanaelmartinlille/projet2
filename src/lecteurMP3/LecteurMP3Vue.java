package lecteurMP3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
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


	JLabel play, avant, arriere;
	JSlider sliderLecture;
	JPanel panelEcouteCourante;
	JLabel ecouteActuelle;
	boolean lectureEnCours = false;
	ImageIcon imgPlay = new ImageIcon(getClass().getResource("/play.png"));
	ImageIcon imgPause = new ImageIcon(getClass().getResource("/pause.png"));
	ImageIcon imgAV = new ImageIcon(getClass().getResource("/suivant.png"));
	ImageIcon imgAR= new ImageIcon(getClass().getResource("/precedent.png"));



	public LecteurMP3Vue(LecteurMP3Controlleur controlleur) {
		this.controlleur = controlleur;
		this.setLayout(new BorderLayout());
		initComposantPrincipaux();
		initPanelLectureCourante();
		initHandler();

		this.add(play, BorderLayout.CENTER);
		this.add(avant, BorderLayout.EAST);
		this.add(arriere, BorderLayout.WEST);
		this.add(panelEcouteCourante, BorderLayout.SOUTH);


	}

	private void initPanelLectureCourante() {
		panelEcouteCourante = new JPanel();
		panelEcouteCourante.setLayout(new GridLayout(2, 1));
		// init du slider de 0 � 100
		sliderLecture = new JSlider();
		sliderLecture.setValue(0);

		ecouteActuelle = new JLabel();
		ecouteActuelle.setText("aucune chanson en cours de lecture");
		panelEcouteCourante.add(sliderLecture);
		panelEcouteCourante.add(ecouteActuelle);

	}

	public void initComposantPrincipaux(){
		play = new JLabel(imgPlay);
		avant = new JLabel(imgAV);
		arriere = new JLabel(imgAR);
	}


	private void initHandler() {
		play.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					controlleur.playPauseControlleur();
				} catch (JavaLayerException e1) {
					JOptionPane.showMessageDialog(null,
							"Erreur lors de l'ouverture du fichier",
							"Erreur durant l'ouverture du fichier, fichier introuvable",
							JOptionPane.ERROR_MESSAGE);
					controlleur.setStatus(-1);
				}
				if(lectureEnCours){
					lectureEnCours = ! lectureEnCours;
					System.out.println("on change");
					play.setIcon(imgPlay);
				}else{
					lectureEnCours = !lectureEnCours;
					play.setIcon(imgPause);
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
	public void update(Observable o, Object obj) {
		// on recupere l'observable qui est le lecteur MP3
		LecteurMP3Modele lecteurMP3Modele = (LecteurMP3Modele) o;
		// on va update les textfield avec le nom de la musique, ainsi que la position actuelle de la lecture
		float pourcentageAvancement = ((float) lecteurMP3Modele.getPosition()/ (float) lecteurMP3Modele.getDuration()) *100;
		sliderLecture.setValue((int) pourcentageAvancement);
		ecouteActuelle.setText("en ecoute : " + lecteurMP3Modele.musiqueActuelle.toString());
		System.out.println("PositionEvent avec position en pourcent =" + pourcentageAvancement); 

	}

}
