import java.util.Observable;

import javazoom.jl.decoder.Equalizer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.LillePlayer;

// requete en base
public class LecteurMP3Modele extends Observable {

	Musique musiqueActuelle;
	private LillePlayer player;
	private String currentPath;
	private int state;  //0:stop, 1:load, 2:play
	private float volume = 1;
	private int position = 0;


	public LecteurMP3Modele() {
		player = null;
		currentPath = "ressources/Musiques/backtoblack.mp3";
		ID3Reader id3Reader = new ID3Reader(currentPath);
		musiqueActuelle = new Musique(id3Reader.title, id3Reader.album, id3Reader.artist, id3Reader.year);
		state = 0;
	}

	public void majModele(Musique nouvelleMusique) {

	}

	public void playPause() throws JavaLayerException{

		if(state == 0){
			load(currentPath);
			playPause();
		}else if(state == 1){
			LaunchListenThread llt = new LaunchListenThread(player);
			llt.start();
			state = 2;
		}else if(state == 2){
			player.pause();
		}else{
			stop();
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

	public void load(String path) throws JavaLayerException{
		if(state != 0)
			stop();

		currentPath = path;
		player = new LillePlayer(currentPath);
		player.setVolume(volume);
		Equalizer eq = new Equalizer();
		eq.getBand(0);
		state = 1;
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
						setChanged();
						float pourcentageAvancement = ((float) position / (float) playerInterne.getDuration()) *100;
						notifyObservers(pourcentageAvancement);
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

	public int getStatus() {
		return state;
	}

	public void setStatus(int status) {
		this.state = status;
	}

}
