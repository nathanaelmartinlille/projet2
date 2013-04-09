package playlist;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;



public class PlayListMusique extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PlayListMusiquesControlleur controlleur;
	private JTable tableMorceaux;

	public PlayListMusique(PlayListMusiquesControlleur controlleur)
	{
		this.controlleur = controlleur;
		tableMorceaux =  new JTable(new TablePerso());
		tableMorceaux.setDragEnabled(true);
		tableMorceaux.setTransferHandler(new TransferHandlerPerso(tableMorceaux));
		
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		tableMorceaux.setDragEnabled(true);
//		tableMorceaux.setBackground(new Color(253, 45, 155));	
		JPanel panelTable = new JPanel();
		tableMorceaux.setFillsViewportHeight(true);
		tableMorceaux.setAutoCreateRowSorter(true);
		JScrollPane scrollRecherche= new JScrollPane(tableMorceaux);
		scrollRecherche.setMinimumSize(new Dimension(800,200));
		scrollRecherche.setMaximumSize(new Dimension(800, 200));
		scrollRecherche.setPreferredSize(new Dimension(800, 200));
		panelTable.add(scrollRecherche);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;      //make this component tall
		c.weightx = 1.0;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 1;
		this.add(panelTable, c);
		
//		this.add(new JScrollPane(tableMorceaux));

		//this.add(tableMorceaux, BorderLayout.CENTER);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}
	
	class TransferHandlerPerso extends TransferHandler
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JTable table;
		public TransferHandlerPerso(JTable table)
		{
			this.table = table;
		}


		public boolean canImport(TransferHandler.TransferSupport info) {
			if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				return false;
			}
			return true;
		}

		public boolean importData(TransferHandler.TransferSupport support){
			if(!canImport(support))
				return false;

			Transferable data = support.getTransferable();

			try {
				data.getTransferData(DataFlavor.javaFileListFlavor);
			} catch (UnsupportedFlavorException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		protected void exportDone(JComponent c, Transferable t, int action){
		}

		protected Transferable createTransferable(JComponent c) {
			return null;
		}

		public int getSourceActions(JComponent c) {
			return COPY;
		}   

	}

	// Table Personnalis�e

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
			nomsColonnes[0] = "Num�ro";
			nomsColonnes[1] = "Artiste";
			nomsColonnes[2] = "Titre";
			nomsColonnes[3] = "Album";
			nomsColonnes[4] = "Genre";
			nomsColonnes[5] = "Ann�e";
			nomsColonnes[6] = "Dur�e";

			donnees = new String[2][7];
			donnees[0][0] = "0";
			donnees[0][1] = "Pif";
			donnees[0][2] = "Titre0";
			donnees[0][3] = "Album0";
			donnees[0][4] = "Genre0";
			donnees[0][5] = "2010";
			donnees[0][6] = " 01:03";

			donnees[1][0] = "1";
			donnees[1][1] = "Paf";
			donnees[1][2] = "Titre1";
			donnees[1][3] = "Album1";
			donnees[1][4] = "Genre1";
			donnees[1][5] = "2011";
			donnees[1][6] = " 01:13";

		}
	}

}
