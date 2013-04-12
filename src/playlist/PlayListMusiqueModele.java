package playlist;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;

import javax.swing.JTextField;

import core.Musique;




public class PlayListMusiqueModele extends Observable {

	String[][] resultatsRecherche;
	String[][] tableauBDD;

	public PlayListMusiqueModele() {
		tableauBDD = getMusiques();
		resultatsRecherche = tableauBDD;
	}

	public void majModele(Musique nouvelleMusique) {

	}

	public void chercher(JTextField texteAChercher)
	{
		setChanged();
		notifyObservers();
	}

	public void supprimerMusique(int row)
	{
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
			
			statement.executeUpdate("delete from musiques where rowid = '"+row+"';");
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
	}
	
	public Musique getMusique(int row)
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
			
			// On vŽrifie que le morceau n'est pas dŽjˆ dans le player
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
		return m;
	}
	
	public void ajouterMusique(Musique musique)
	{
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
			System.out.println("que ajouter : "+"insert into musiques values('"+musique.album+"','"+ musique.artiste +"','"+musique.titre+"','"+musique.genre+"','"+musique.annee+"','"+musique.duree+"','0','"+musique.path+"');");

			// On vŽrifie que le morceau n'est pas dŽjˆ dans le player
			ResultSet rs = statement.executeQuery("select * from musiques WHERE path = '"+musique.path+"' ");

			if(!rs.next())
			{
				statement.executeUpdate("insert into musiques values('"+musique.album+"','"+ musique.artiste +"','"+musique.titre+"','"+musique.genre+"','"+musique.annee+"','"+musique.duree+"','0','"+musique.path+"');");
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
	}

	public boolean addOneLecture(int row)
	{
		int cpt = this.getNbLecture(row);
		String requete = "";
		if(cpt>-1)
		{
			cpt++;
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			Connection connection = null;
			try
			{
				// create a database connection
				connection = DriverManager.getConnection("jdbc:sqlite:" + "ressources/mp3database.sqlite");
				Statement statement = connection.createStatement();
				requete = "UPDATE musiques SET nbEcoutes = '"+cpt+"' WHERE rowid = '"+row+"' ";
				statement.executeUpdate(requete); 
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()+ requete);
			}
			finally
			{
				try
				{
					if(connection != null)
						connection.close();
				}
				catch(SQLException e){System.err.println(e);}
			}
		}

		return true;
	}

	public int getNbLecture(int row)
	{
		int cpt = -1;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection connection = null;
		try{

			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + "ressources/mp3database.sqlite");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("select * from musiques WHERE rowid = '"+row+"' ");
			while(true)
			{
				if(rs.next())
				{
					try {
						cpt = Integer.parseInt(rs.getString("nbEcoutes"));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else
				{
					try
					{
						if(connection != null){
							connection.close();
						}
					}
					catch(SQLException e){
						System.err.println(e);
					}
					break;
				}
			}
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e){System.err.println(e);}
		}
		return cpt;
	}

	public void supprimerMusique(Musique musique)
	{

	}

	// Pour faire la recherche sur la base de donnï¿½es : 
	public String[][] getMusiques()
	{
		String[][] tableau = null;

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

			ResultSet rs2 = statement.executeQuery("select * from musiques");
			int j = 0;

			while(rs2.next())
			{
				j++;
			}
			tableau = new String[j][8];
			int i = 1;
			rs2 = statement.executeQuery("select * from musiques");

			while(rs2.next())
			{
				tableau[i-1][0] = ""+i;
				tableau[i-1][1] = rs2.getString("artist");
				tableau[i-1][2] = rs2.getString("title");
				tableau[i-1][3] = rs2.getString("album");
				tableau[i-1][4] = rs2.getString("genre");
				tableau[i-1][5] = rs2.getString("year");
				tableau[i-1][6] = rs2.getString("duration");
				tableau[i-1][7] = rs2.getString("nbEcoutes");
				i++;
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
		return tableau;
	}


}
