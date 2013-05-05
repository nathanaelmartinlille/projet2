package recherche;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import core.Constantes;
import core.ID3Reader;
import core.Musique;
import core.MusiqueUtils;

/**
 * @author nath
 *
 */
public class RechercheModele extends Observable {

	String[][] resultatsRecherche;

	/**Constructeur par defaut.
	 * @param rechercheEnBase true si on veut rechercher dans la base fictive.
	 */
	public RechercheModele(boolean rechercheEnBase) {
		trouverBDD(rechercheEnBase);
	}

	/**Methode qui va mettre a jour les tables avec les sorters.
	 * @param texteAChercher le texte à chercher
	 *///FIXME a quoi sert cette variable alors qu'on en fait rien ???
	public void rafraichirRecherche()
	{
		setChanged();
		notifyObservers();
	}

	/**
	 * @param rechercheEnBase vrai si on recherche en base, faux si on fait une recherche locale.
	 * @return la liste des resultats de la recherche
	 */
	public void trouverBDD(boolean rechercheEnBase)
	{
		System.out.println("on fait une mise à jour du tableau");
		if(rechercheEnBase){
			resultatsRecherche = rechercherEnBase();
		}else{
			resultatsRecherche = rechercheEnLocal();
		}
	}

	/**
	 * @return la liste des resultats de la recherche locale.
	 */
	private String[][] rechercheEnLocal() {
		String[][] tableau = null;
		System.out.println("on fait une recherche locale");
		ID3Reader reader;
		int i = 1;
		List<Musique> listeMusiqueConvertie = new ArrayList<Musique>();
		
		List<File> listeFichierEnLocal = MusiqueUtils.recupererMusique(Constantes.CHEMIN_MUSIQUE);
		for (File file : listeFichierEnLocal) {
			reader = new ID3Reader(file.getPath());
			System.out.println("musique trouvee = " + reader.getTitle());
			listeMusiqueConvertie.add(reader.recupererMusiqueAPartirInformationTag());
		}
		
		tableau = new String[listeMusiqueConvertie.size()][7];
		for (Musique musique : listeMusiqueConvertie) {
			tableau[i-1][0] = musique.album;
			tableau[i-1][1] = musique.artiste;
			tableau[i-1][2] = musique.titre;
			tableau[i-1][3] = musique.genre;
			tableau[i-1][4] = musique.annee;
			tableau[i-1][5] = musique.duree;
			i++;
		}
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
