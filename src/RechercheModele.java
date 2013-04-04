import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;



public class RechercheModele extends Observable {

	String[][] resultatsRecherche;
	String[][] tableauBDD;

	public RechercheModele() {
		// TODO Auto-generated constructor stub
		tableauBDD = trouverBDD();
		resultatsRecherche = tableauBDD;
	}

	public void majModele(Musique nouvelleMusique) {

	}

	public void chercher(JTextField texteAChercher)
	{
		System.out.println("texte recherche : "+texteAChercher.getText());
		int j = 0;
		ArrayList<String[]> listeContenue = new ArrayList<String[]>();
		for(String[] uneMusique : tableauBDD)
		{
			if(uneMusique[1].contains(texteAChercher.getText()) && !listeContenue.contains(uneMusique)){
				resultatsRecherche[j] = uneMusique;
				listeContenue.add(uneMusique);
				j++;
			}
			if(uneMusique[2].contains(texteAChercher.getText()) && !listeContenue.contains(uneMusique)){
				resultatsRecherche[j] = uneMusique;
				listeContenue.add(uneMusique);
				j++;
			}
			if(uneMusique[3].contains(texteAChercher.getText()) && !listeContenue.contains(uneMusique)){
				resultatsRecherche[j] = uneMusique;
				listeContenue.add(uneMusique);
				j++;
			}
		}
	}


	// Pour faire la recherche sur la base de données : 
	public String[][] trouverBDD()
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

			ResultSet rs2 = statement.executeQuery("select * from songs");
			int j = 0;
			while(rs2.next())
			{
				j++;
			}
			tableau = new String[j][7];
			int i = 1;
			ResultSet rs = statement.executeQuery("select * from songs");
			while(rs.next())
			{
				tableau[i-1][0] = ""+i;
				tableau[i-1][1] = rs.getString("album");
				tableau[i-1][2] = rs.getString("artist");
				tableau[i-1][3] = rs.getString("title");
				tableau[i-1][4] = rs.getString("genre");
				tableau[i-1][5] = rs.getString("year");
				tableau[i-1][6] = rs.getString("duration");
				// read the result set
				//System.out.println(i + " " + rs.getString("album") + " " + rs.getString("artist") + " " + rs.getString("title") + " " + rs.getString("genre") + " " + rs.getString("year") + " " + rs.getString("duration"));
				i++;
			}
		}
		catch(SQLException e)
		{
			System.err.println(e.getMessage());
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
