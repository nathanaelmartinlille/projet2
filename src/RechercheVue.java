import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
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
		texteRecherche = new JTextField(50);

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
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;      //make this component tall
		c.weightx = 1.0;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 1;
		this.add(panelTable, c);
		
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
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	
	
	
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
			nomsColonnes[0] = "Num�ro";
			nomsColonnes[1] = "Titre";
			nomsColonnes[2] = "Artiste";
			nomsColonnes[3] = "Album";
			nomsColonnes[4] = "Genre";
			nomsColonnes[5] = "Ann�e";
			nomsColonnes[6] = "Dur�e";

			donnees = controlleur.avoirResultatsRecherche();

		}
	}
	
}
