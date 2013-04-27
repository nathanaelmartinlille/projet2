package lecteurMP3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;

import javax.swing.JOptionPane;

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

	// Cette fonction est appel�e depuis la table des musiques
	public void majModele(Musique nouvelleMusique) throws Exception 
	{
		System.out.println("maj musique : "+nouvelleMusique);
		controlleur.afficherStop();
		musiqueActuelle = nouvelleMusique;
		// gestion du cas d'une musique provenant de la bdd. 
		if(!(musiqueActuelle == null)){
			currentPath = musiqueActuelle.path;
			try {
				stop();
				state = 0;
				playPause();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}

	}

	public void playPause() throws Exception{

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
		System.out.println("volume avant : "+volume);
		volume = level/100;
		System.out.println("volume : "+level/100);
		player.setVolume(level/100);
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

	public void load(String path) throws Exception{
		if(state != 0)
			stop();

		currentPath = path;
		try {
			player = new LillePlayer(currentPath);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
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
					passerChansonSuivante();
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

	public void passerChansonPrecedente() throws Exception
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

			// On v�rifie row -> si c'est le premier de la liste alors on met le dernier
			int row = getRowId(musiqueActuelle);
			if(row == 0)
				row = dernierRowListe();		
			else
				row--;

			// On v�rifie que le morceau n'est pas d�j� dans le player
			ResultSet rs = statement.executeQuery("select * from musiques WHERE rowid = '"+(getRowId(musiqueActuelle)-1)+"' ");

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
		controlleur.lireMusique(m);
	}

	public void passerChansonSuivante() throws Exception
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

			// On v�rifie row -> si c'est le dernier de la liste alors on met 0
			int row = getRowId(musiqueActuelle);
			if(row == dernierRowListe())
				row = 0;
			else
				row++;

			// On v�rifie que le morceau n'est pas d�j� dans le player
			ResultSet rs = statement.executeQuery("select * from musiques WHERE rowid = '"+row+"' ");

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
		controlleur.lireMusique(m);
	}

	public int dernierRowListe()
	{
		int row = 0;
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

			// On v�rifie que le morceau n'est pas d�j� dans le player
			ResultSet rs = statement.executeQuery("select rowid from musiques");

			if(rs.next())
			{
				row = rs.getInt("rowid");
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
		return row;
	}

	public int getRowId(Musique musique)
	{
		int row = 0;
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

			// On v�rifie que le morceau n'est pas d�j� dans le player
			ResultSet rs = statement.executeQuery("select rowid from musiques WHERE path = '"+musiqueActuelle.path+"' ");

			if(rs.next())
			{
				row = rs.getInt("rowid");
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
		System.out.println("row : "+row);

		return row;
	}

}
