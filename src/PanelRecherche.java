import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;




public class PanelRecherche extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton boutonRecherche;
	private JTextField texteRecherche;
	private Timer timer;
	private TimerTask taskChercher;

	// Pour les résultats de la recherche
	String[][] resultatsRecherche;
	JTable tableRecherche;

	// Pour la BDD
	String[][] tableauBDD;


	public PanelRecherche()
	{
		boutonRecherche = new JButton("Chercher");
		texteRecherche = new JTextField(50);

		tableauBDD = trouverBDD();
		resultatsRecherche = tableauBDD;

		initHandler();
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.75;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(texteRecherche, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.25;
		c.gridx = 1;
		c.gridwidth = 2;
		c.gridy = 0;
		this.add(boutonRecherche, c);

		
		

		tableRecherche =  new JTable(new TablePerso());
		tableRecherche.setDragEnabled(false);
		//tableRecherche.setTransferHandler(new TransferHandlerPerso(tableMorceaux));
		tableRecherche.setBackground(new Color(253, 45, 155));
		
		JPanel panelTable = new JPanel();
		tableRecherche.setFillsViewportHeight(true);

		JScrollPane scrollRecherche= new JScrollPane(tableRecherche);
		scrollRecherche.setMinimumSize(new Dimension(800,100));
		scrollRecherche.setMaximumSize(new Dimension(800, 100));
		scrollRecherche.setPreferredSize(new Dimension(800, 100));
		panelTable.add(scrollRecherche);


		//panelTable.add(tableRecherche.getTableHeader(), BorderLayout.NORTH);
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;      //make this component tall
		c.weightx = 1.0;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 1;
		this.add(panelTable, c);
	}

	private void initHandler() {
		boutonRecherche.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chercher(texteRecherche);
			}
		});

		texteRecherche.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("key typed : "+e);
				if(e.getKeyCode()==(KeyEvent.VK_ENTER))
				{
					chercher(texteRecherche);
				}

				if(timer!=null)
					timer.cancel();
				timer = new Timer();
				taskChercher = new TimerTask() {
					public void run()
					{
						chercher(texteRecherche);
					}
				};
				timer.schedule(taskChercher, 200);			
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void chercher(JTextField texteAChercher)
	{
		System.out.println("texte recherche : "+texteRecherche.getText());
		int j = 0;
		ArrayList<String[]> listeContenue = new ArrayList<String[]>();
		for(String[] uneMusique : tableauBDD)
		{
			if(uneMusique[1].contains(texteRecherche.getText()) && !listeContenue.contains(uneMusique)){
				resultatsRecherche[j] = uneMusique;
				listeContenue.add(uneMusique);
				j++;
			}
			if(uneMusique[2].contains(texteRecherche.getText()) && !listeContenue.contains(uneMusique)){
				resultatsRecherche[j] = uneMusique;
				listeContenue.add(uneMusique);
				j++;
			}
			if(uneMusique[3].contains(texteRecherche.getText()) && !listeContenue.contains(uneMusique)){
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




	// Table Personnalisée

	public class TablePerso extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String[] nomsColonnes;
		String[][] donnees;

		public TablePerso() {
			super();

			initTable();
		}

		public int getRowCount() {
			return donnees.length;
		}

		public int getColumnCount() {
			return nomsColonnes.length;
		}

		public String getColumnName(int columnIndex) {
			return nomsColonnes[columnIndex];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			//System.out.println("j'appuie sur l'index : " +rowIndex);
			return donnees[rowIndex][columnIndex];
		}

		public void ajouterMusique(File file) {

			//fireTableRowsInserted(amis.size() -1, amis.size() -1);
		}

		public void enleverMusique(int rowIndex) {
			//donnees.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		private void initTable()
		{
			nomsColonnes = new String[7];
			nomsColonnes[0] = "Numéro";
			nomsColonnes[1] = "Artiste";
			nomsColonnes[2] = "Titre";
			nomsColonnes[3] = "Album";
			nomsColonnes[4] = "Genre";
			nomsColonnes[5] = "Année";
			nomsColonnes[6] = "Durée";

			donnees = resultatsRecherche;

		}
	}
}