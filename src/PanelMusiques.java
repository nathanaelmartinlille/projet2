import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.TransferHandler;


public class PanelMusiques extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable tableMorceaux;
	private ArrayList<String> nomsColonnes;
	
	public PanelMusiques()
	{
		initTable();
		tableMorceaux = new JTable();
		tableMorceaux.setDragEnabled(true);
		tableMorceaux.setTransferHandler(new TransferHandlerPerso(tableMorceaux));
		tableMorceaux.setSize(200, 200);
		tableMorceaux.setBackground(new Color(12, 45, 155));
		this.add(tableMorceaux, BorderLayout.NORTH);
	}
	
	private void initTable()
	{
		nomsColonnes = new ArrayList<String>();
		nomsColonnes.add("Numéro");
		nomsColonnes.add("Artiste");
		nomsColonnes.add("Titre");
		nomsColonnes.add("Album");
		nomsColonnes.add("Genre");
		nomsColonnes.add("Année");
		nomsColonnes.add("Durée");
		
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

}
