package recherche;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;

import javax.swing.JTextField;

/**
 * @author nath
 *
 */
public class RechercheModele extends Observable {

	String[][] resultatsRecherche;
	String[][] tableauBDD;

	/**Constructeur par defaut.
	 * @param rechercheEnBase true si on veut rechercher dans la base fictive.
	 */
	public RechercheModele(boolean rechercheEnBase) {
		tableauBDD = trouverBDD(rechercheEnBase);
		resultatsRecherche = tableauBDD;
	}

	/**Methode qui va mettre a jour les tables avec les sorters.
	 * @param texteAChercher le texte à chercher
	 *///FIXME a quoi sert cette variable alors qu'on en fait rien ???
	public void chercher(JTextField texteAChercher)
	{
		setChanged();
		notifyObservers();
	}

	/**
	 * @param rechercheEnBase vrai si on recherche en base, faux si on fait une recherche locale.
	 * @return la liste des resultats de la recherche
	 */
	public String[][] trouverBDD(boolean rechercheEnBase)
	{
		if(rechercheEnBase){
			return rechercherEnBase();
		}else{
			return rechercheEnLocal();
		}
	}

	/**
	 * @return la liste des resultats de la recherche locale.
	 */
	private String[][] rechercheEnLocal() {
		String[][] tableau = null;
		System.out.println("on fait une recherche locale");
		return tableau;
	}

	private String[][] rechercherEnBase() {
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
			rs2 = statement.executeQuery("select * from songs");

			while(rs2.next())
			{
				tableau[i-1][0] = rs2.getString("album");
				tableau[i-1][1] = rs2.getString("artist");
				tableau[i-1][2] = rs2.getString("title");
				tableau[i-1][3] = rs2.getString("genre");
				tableau[i-1][4] = rs2.getString("year");
				tableau[i-1][5] = rs2.getString("duration");
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
