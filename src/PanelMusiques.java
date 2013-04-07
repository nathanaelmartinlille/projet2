import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;


public class PanelMusiques extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable tableMorceaux;


	public PanelMusiques()
	{
		
		
		tableMorceaux =  new JTable(new TablePerso());
		tableMorceaux.setDragEnabled(true);
		tableMorceaux.setTransferHandler(new TransferHandlerPerso(tableMorceaux));
		tableMorceaux.setMinimumSize(new Dimension(700,200));
		tableMorceaux.setMaximumSize(new Dimension(700, 200));
		tableMorceaux.setPreferredSize(new Dimension(700, 200));
//		tableMorceaux.setBackground(new Color(12, 45, 155));
		this.add(new JScrollPane(tableMorceaux), BorderLayout.CENTER);
		this.add(tableMorceaux.getTableHeader(), BorderLayout.NORTH);
		//this.add(tableMorceaux, BorderLayout.CENTER);
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


