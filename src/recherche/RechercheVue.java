package recherche;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


/**Cette classe permet de modeliser la vue du panel de recherche.
 */
public class RechercheVue extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	RechercheControlleur controlleur;

	private JTextField texteRecherche;
	private JCheckBox choixRechercheLocale;
	private Timer timer;
	private TimerTask taskChercher;

	// Pour les résultats de la recherche

	JTable tableRecherche;
	TablePerso tablePerso;
	TableRowSorter<TableModel> sorter;



	public RechercheVue(final RechercheControlleur controlleur) {
		this.controlleur = controlleur;
		choixRechercheLocale = new JCheckBox("Utiliser une recherche fictive");
		choixRechercheLocale.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// si la case est cochée on va faire une recherche fictive
				controlleur.modele.trouverBDD(e.getStateChange() == ItemEvent.SELECTED);
				tablePerso.initTable();
				tablePerso.actualiserTable();
			}
		});

		JPanel panelRechercheTexte = new JPanel();
		texteRecherche = new JTextField(30);

		texteRecherche.setToolTipText("exemple: titre: friday album: fantasia");

		initHandler();
		JPanel panelRecherche = new JPanel();
		panelRecherche.setLayout(new GridLayout(1, 2));
		JLabel lab = new JLabel("Tapez votre recherche ici");
		lab.setHorizontalAlignment(SwingConstants.RIGHT);
		panelRecherche.add(lab);
		panelRechercheTexte.setLayout(new GridLayout(1, 2));
		panelRechercheTexte.add(texteRecherche);
		panelRechercheTexte.add(choixRechercheLocale);
		panelRecherche.add(panelRechercheTexte);
		this.setLayout(new BorderLayout());
		this.add(panelRecherche, BorderLayout.NORTH);
		tablePerso = new TablePerso();
		tableRecherche =  new JTable(tablePerso);
		tableRecherche.setDragEnabled(false);

		JPanel panelTable = new JPanel();
		tableRecherche.setFillsViewportHeight(true);
		tableRecherche.setAutoCreateRowSorter(true);
		sorter = new TableRowSorter<TableModel>(tableRecherche.getModel());   
		tableRecherche.setRowSorter(sorter);


		JScrollPane scrollRecherche= new JScrollPane(tableRecherche);
		scrollRecherche.setMinimumSize(new Dimension(800,100));
		scrollRecherche.setMaximumSize(new Dimension(800, 100));
		scrollRecherche.setPreferredSize(new Dimension(800, 100));
		panelTable.add(scrollRecherche);

		this.add(panelTable, BorderLayout.CENTER);
		JLabel labelAide = new JLabel("double cliquez sur une musique pour l'ajouter à la liste ou bien glissez la");
		labelAide.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(labelAide, BorderLayout.SOUTH);

	}


	// l'objet en parametre 
	@Override
	public void update(Observable o, Object avancement) {
		// le boolean pour savoir quelle type de recherche l'utilisateur souhaite
		boolean rechercheArtiste = false;
		boolean rechercheTitre = false;
		boolean rechercheAlbum = false;
		boolean rechercheGenre = false;
		// true si on fait une recherche personnalisée (par album etc ... )
		boolean recherchePersonnalisee = false;
		String valeurRechercheGenre = null;
		String valeurRechercheArtiste = null;
		String valeurRechercheTitre = null;
		String valeurRechercheAlbum = null;

		// les differents filtres possible, dont chacun filtre sur une colonne
		RowFilter<TableModel, Object> filtreArtiste = null;
		RowFilter<TableModel, Object> filtreTitre = null;
		RowFilter<TableModel, Object> filtreAlbum = null;
		RowFilter<TableModel, Object> filtreGenre = null;
		List<RowFilter<TableModel,Object>> ensembleFiltre = new ArrayList<RowFilter<TableModel,Object>>();
		RowFilter<TableModel, Object> ensembleRowFilter = null;

		// On update la table
		String valeurRecherche = texteRecherche.getText();
		// on recherche les mots clés possible que l'utilisateur a pu taper
		rechercheTitre = valeurRecherche.contains("titre");
		rechercheAlbum = valeurRecherche.contains("album");
		rechercheArtiste = valeurRecherche.contains("artiste");
		rechercheGenre = valeurRecherche.contains("genre");
		// on va chercher quel mot clé a été utilisé par l'utilisateur
		if(rechercheTitre){
			recherchePersonnalisee = true;
			valeurRechercheTitre = valeurRecherche.substring(valeurRecherche.indexOf("titre") + 6);
			if( valeurRechercheTitre.indexOf("album") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheTitre = valeurRechercheTitre.substring(0, valeurRechercheTitre.indexOf("album"));
			}
			if( valeurRechercheTitre.indexOf("artiste") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheTitre = valeurRechercheTitre.substring(0, valeurRechercheTitre.indexOf("artiste"));
			}
			if( valeurRechercheTitre.indexOf("genre") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheTitre = valeurRechercheTitre.substring(0, valeurRechercheTitre.indexOf("genre"));
			}
		}

		if(rechercheAlbum){
			recherchePersonnalisee = true;
			valeurRechercheAlbum = valeurRecherche.substring(valeurRecherche.indexOf("album") + 6);
			if( valeurRechercheAlbum.indexOf("titre") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheAlbum = valeurRechercheAlbum.substring(0, valeurRechercheAlbum.indexOf("titre"));
			}
			if( valeurRechercheAlbum.indexOf("artiste") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheAlbum = valeurRechercheAlbum.substring(0, valeurRechercheAlbum.indexOf("artiste"));
			}
			if( valeurRechercheAlbum.indexOf("genre") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheAlbum = valeurRechercheAlbum.substring(0, valeurRechercheAlbum.indexOf("genre"));
			}
		}

		if(rechercheArtiste){
			recherchePersonnalisee = true;
			valeurRechercheArtiste = valeurRecherche.substring(valeurRecherche.indexOf("artiste") + 6);
			if( valeurRechercheArtiste.indexOf("titre") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheArtiste = valeurRechercheArtiste.substring(0, valeurRechercheArtiste.indexOf("titre"));
			}
			if( valeurRechercheArtiste.indexOf("album") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheArtiste = valeurRechercheArtiste.substring(0, valeurRechercheArtiste.indexOf("album"));
			}
			if( valeurRechercheArtiste.indexOf("genre") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheArtiste = valeurRechercheArtiste.substring(0, valeurRechercheArtiste.indexOf("genre"));
			}
		}

		if(rechercheGenre){
			recherchePersonnalisee = true;
			valeurRechercheGenre = valeurRecherche.substring(valeurRecherche.indexOf("genre") + 6);
			if( valeurRechercheGenre.indexOf("titre") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheGenre = valeurRechercheGenre.substring(0, valeurRechercheGenre.indexOf("titre"));
			}
			if( valeurRechercheGenre.indexOf("album") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheGenre = valeurRechercheGenre.substring(0, valeurRechercheGenre.indexOf("album"));
			}
			if( valeurRechercheGenre.indexOf("artiste") != -1 ) {
				// on a trouvé un mot clé correspondant à l'album
				valeurRechercheGenre = valeurRechercheGenre.substring(0, valeurRechercheGenre.indexOf("artiste"));
			}
		}
		if(rechercheTitre){

			System.out.println("on a detecté une recherche par titre avec titre = " +valeurRechercheTitre);
			filtreTitre = RowFilter.regexFilter("(?i)" + valeurRechercheTitre.trim(), 0);
			ensembleFiltre.add(filtreTitre);
		} 
		if(rechercheArtiste){
			System.out.println("on a detecté une recherche par artiste avec artiste = " +valeurRechercheArtiste);
			filtreArtiste = (RowFilter.regexFilter("(?i)" + valeurRechercheArtiste.trim(), 1));
			ensembleFiltre.add(filtreArtiste);
		} 
		if(rechercheAlbum){
			System.out.println("on a detecté une recherche par album avec album = " +valeurRechercheAlbum);
			filtreAlbum = RowFilter.regexFilter("(?i)" + valeurRechercheAlbum.trim(), 2);
			ensembleFiltre.add(filtreAlbum);
		} 
		if(rechercheGenre){
			System.out.println("on a detecté une recherche par genre avec genre = " +valeurRechercheGenre);
			filtreGenre = RowFilter.regexFilter("(?i)" + valeurRechercheGenre.trim(), 3);
			ensembleFiltre.add(filtreGenre);
		}
		if (!recherchePersonnalisee) {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + valeurRecherche, 0, 1, 2));
		}else{
			// on fait un && avec les differents criteres
			ensembleRowFilter = RowFilter.andFilter(ensembleFiltre);
			sorter.setRowFilter(ensembleRowFilter);
		}
	}

	private void initHandler() {
		texteRecherche.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("key typed : "+e);
				if(e.getKeyCode()==(KeyEvent.VK_ENTER))
				{
					controlleur.demandeRecherche(texteRecherche);
				}

				if(timer!=null)
					timer.cancel();
				timer = new Timer();
				taskChercher = new TimerTask() {
					public void run()
					{
						controlleur.demandeRecherche(texteRecherche);
					}
				};
				timer.schedule(taskChercher, 200);			
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}




	public class TablePerso extends AbstractTableModel implements MouseListener{
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
			return donnees[rowIndex][columnIndex];
		}

		public void ajouterMusique() {
			System.out.println("demande d'ajout de musique");
		}

		public void enleverMusique(int rowIndex) {
			//donnees.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}

		public void actualiserTable()
		{
			fireTableDataChanged();
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		private void initTable()
		{
			nomsColonnes = new String[6];
			nomsColonnes[0] = "Titre";
			nomsColonnes[1] = "Artiste";
			nomsColonnes[2] = "Album";
			nomsColonnes[3] = "Genre";
			nomsColonnes[4] = "Année";
			nomsColonnes[5] = "Durée";

			donnees = controlleur.avoirResultatsRecherche();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			ajouterMusique();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

}
