import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javazoom.jl.decoder.Equalizer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.LillePlayer;

public class PanelLecteurMP3 extends JPanel {
	private LillePlayer player;
	private String currentPath;

	private int state;  //0:stop, 1:load, 2:play
	private float volume = 1;
	private int position = 0;
	JPanel panelEcouteCourante;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton play, avant, arriere;
	JSlider sliderLecture;

	public PanelLecteurMP3() {
		panelEcouteCourante = new JPanel();
		this.setLayout(new BorderLayout());
		play = new JButton(">");

		avant = new JButton("AV");
		arriere = new JButton("AR");

		sliderLecture = new JSlider();
		sliderLecture.setValue(0);

		//Init de l'objet player
		player = null;
		currentPath = "ressources/Musiques/backtoblack.mp3";
		state = 0;

		initHandler();

		panelEcouteCourante.add(sliderLecture);


		this.add(play, BorderLayout.CENTER);
		this.add(avant, BorderLayout.WEST);
		this.add(arriere, BorderLayout.EAST);
		this.add(sliderLecture, BorderLayout.SOUTH);
	}


	private void initHandler() {
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playPause();
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
				player.setPosition(player.getDuration() * sliderLecture.getValue() /100);
			}
		});
	}

	public void load(String path){
		if(state != 0)
			stop();

		try{
			currentPath = path;
			player = new LillePlayer(currentPath);
			player.setVolume(volume);
			Equalizer eq = new Equalizer();
			eq.getBand(0);
			state = 1;
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(this,
					"Erreur lors de l'ouverture du fichier",
					"Erreur durant l'ouverture du fichier, fichier introuvable",
					JOptionPane.ERROR_MESSAGE);
			state = -1;
			e.printStackTrace();
		}
	}

	public void playPause(){
		switch (state) {
		case 0:
			load(currentPath);
			playPause();
			break;
		case 1:
			LaunchListenThread llt = new LaunchListenThread(player);
			llt.start();
			state = 2;
			break;
		case 2:
			System.out.println("pause");
			player.pause();
			System.out.println("je suis en pause");
			break;

		default:
			stop();
			break;
		}
	}

	public void stop(){			
		if(state == 1 || state == 2){
			player.close();
			state = 0;
		}
	}

	public float getVolume(){
		return volume;
	}

	public void setVolume(float level){
		volume = level;
		player.setVolume(level);
	}

	public int getDuration(){
		if(player == null)
			return 0;
		return player.getDuration();
	}

	public LillePlayer getMediaPlayer(){
		return player;
	}

	public int getPosition(){
		return position;
	}

	public void setPosition(int pos){
		player.setPosition(pos);
		position = pos;
	}
	class LaunchListenThread extends Thread{
		private LillePlayer playerInterne;
		public LaunchListenThread(LillePlayer p){
			playerInterne = p;
		}
		public void run(){
			try{			
				System.out.println("LaunchEvent");
				PlayThread pt = new PlayThread();
				pt.start();

				while(!playerInterne.isComplete()){
					position = playerInterne.getPosition();
					if(player == playerInterne){
						float pourcentageAvancement = ((float) position / (float) playerInterne.getDuration()) *100;
						sliderLecture.setValue((int)pourcentageAvancement);
						System.out.println("PositionEvent avec position = " + position + "pourcentage = " +pourcentageAvancement); 

					}

					try{
						Thread.sleep(200);
					}catch(Exception e){ e.printStackTrace(); }
				}

				if(player == playerInterne)
					System.out.println("EndEvent");
			}catch(Exception e){ e.printStackTrace(); }
		}

		class PlayThread extends Thread{
			public void run(){
				try {
					playerInterne.play();
				}catch(JavaLayerException e){ e.printStackTrace(); }
			}
		}		

	}
}