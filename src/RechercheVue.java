import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
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


public class RechercheVue extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	RechercheControlleur controlleur;

	private JButton boutonRecherche;
	private JTextField texteRecherche;
	private Timer timer;
	private TimerTask taskChercher;

	// Pour les r�sultats de la recherche

	JTable tableRecherche;
	TableRowSorter<TableModel> sorter;



	public RechercheVue(RechercheControlleur controlleur) {
		this.controlleur = controlleur;

		boutonRecherche = new JButton("Chercher");
		texteRecherche = new JTextField(30);

		initHandler();
		JPanel panelRecherche = new JPanel();
		panelRecherche.setLayout(new GridLayout(1, 2));
		panelRecherche.add(texteRecherche);
		panelRecherche.add(boutonRecherche);
		this.setLayout(new BorderLayout());
		this.add(panelRecherche, BorderLayout.NORTH);



		tableRecherche =  new JTable(new TablePerso());
		tableRecherche.setDragEnabled(false);
		//tableRecherche.setTransferHandler(new TransferHandlerPerso(tableMorceaux));
		//		tableRecherche.setBackground(new Color(253, 45, 155));

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


		//panelTable.add(tableRecherche.getTableHeader(), BorderLayout.NORTH);


		this.add(panelTable, BorderLayout.CENTER);
		JLabel labelAide = new JLabel("double cliquez sur une musique pour l'ajouter à la liste ou bien glissez la");
		labelAide.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(labelAide, BorderLayout.SOUTH);

	}


	// l'objet en parametre 
	@Override
	public void update(Observable o, Object avancement) {
		// On update la table
		String regex = texteRecherche.getText();
		sorter.setRowFilter(RowFilter.regexFilter("(?i)" + regex, 1, 2, 3));
	}

	private void initHandler() {
		boutonRecherche.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controlleur.demandeRecherche(texteRecherche);
			}
		});

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
			System.out.println("j'appuie sur l'index : " +rowIndex);
			return donnees[rowIndex][columnIndex];
		}

		public void ajouterMusique() {

			//fireTableRowsInserted(amis.size() -1, amis.size() -1);
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
			nomsColonnes = new String[7];
			nomsColonnes[0] = "Numéro";
			nomsColonnes[1] = "Titre";
			nomsColonnes[2] = "Artiste";
			nomsColonnes[3] = "Album";
			nomsColonnes[4] = "Genre";
			nomsColonnes[5] = "Année";
			nomsColonnes[6] = "Durée";

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
			// TODO Auto-generated method stub

		}


		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

}
