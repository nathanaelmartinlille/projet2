package lecteurMP3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;

import core.ID3Reader;
import core.Musique;

import javazoom.jl.decoder.Equalizer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.LillePlayer;

// requete en base
public class LecteurMP3Modele extends Observable {

	Musique musiqueActuelle;
	public LillePlayer player;
	private String currentPath;
	private int state;  //0:stop, 1:load, 2:play
	private float volume = 1;
	private int position = 0;
	LecteurMP3Controlleur controlleur;


	public LecteurMP3Modele(LecteurMP3Controlleur controlleur) {
		this.controlleur = controlleur;
		player = null;
		currentPath = "ressources/Musiques/backtoblack.mp3";
		ID3Reader id3Reader = new ID3Reader(currentPath);
		musiqueActuelle = new Musique(id3Reader.getTitle(), id3Reader.getAlbum(), id3Reader.getArtist(), "genre",id3Reader.getYear(), "00:00", currentPath);
		state = 0;
	}

	// Cette fonction est appelée depuis la table des musiques
	public void majModele(Musique nouvelleMusique) 
	{
		System.out.println("maj musique : "+nouvelleMusique);
		musiqueActuelle = nouvelleMusique;
		currentPath = musiqueActuelle.path;
		try {
			stop();
			state = 0;
			playPause();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		System.out.println("volume : "+level);
		player.setVolume(level/10);
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
						notifyObservers();
					}

					try{
						Thread.sleep(200);
					}catch(Exception e){ e.printStackTrace(); }
				}

				if(player == playerInterne){
					System.out.println("EndEvent");
					controlleur.afficherStop();
					passerChansonSuivante(musiqueActuelle);
				}
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
	
	public void passerChansonSuivante(Musique musiqueActuelle)
	{
		Musique m = null;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		Connection connection = null;
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:" + "ressources/mp3database.sqlite");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			// On vérifie que le morceau n'est pas déjà dans le player
			ResultSet rs = statement.executeQuery("select * from musiques WHERE rowid = '"+musiqueActuelle.path+"' ");

			if(rs.next())
			{
				m = new Musique(rs.getString("title"), rs.getString("album"), rs.getString("artist"), rs.getString("genre"), rs.getString("year"), rs.getString("duration"), rs.getString("path"));
			}


		}
		catch(SQLException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				System.err.println(e);
			}
		}
		majModele(m);
	}

}
