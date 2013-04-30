package playlist;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.LillePlayer;
import core.ID3Reader;
import core.Musique;



public class PlayListMusiqueVue extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PlayListMusiquesControlleur controlleur;
	private JTable tableMorceaux;

	public PlayListMusiqueVue(final PlayListMusiquesControlleur controlleur)
	{
		this.controlleur = controlleur;
		tableMorceaux =  new JTable(new TablePerso());
		tableMorceaux.setDragEnabled(true);
		tableMorceaux.setTransferHandler(new TransferHandlerPerso(tableMorceaux));
		
		// On peut supprimer une chanson en cliquant sur la touche suppr quand on est sur une musique
		tableMorceaux.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_ALT)
				{
					System.out.println("supprimer");
					controlleur.supprimerMusique(tableMorceaux.getSelectedRow());
					MAJTable(null);
				}
				//Si on appuie sur entr�e, on joue la musique
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					System.out.println("entrer");
				}
			}
		});
		// On peut jouer la chanson si on clique deux fois dessus
		tableMorceaux.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2 )
				{
		                System.out.println("Double clicked");
		                try {
							controlleur.jouerMusique(tableMorceaux.getSelectedRow());
		                } catch (Exception e1) {
							JOptionPane.showMessageDialog(null,
									"Erreur lors de l'ouverture du fichier",
									"Erreur durant l'ouverture du fichier, fichier introuvable",
									JOptionPane.ERROR_MESSAGE);
						}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		tableMorceaux.setDragEnabled(true);
//		tableMorceaux.setBackground(new Color(253, 45, 155));	
		JPanel panelTable = new JPanel();
		tableMorceaux.setFillsViewportHeight(true);
		tableMorceaux.setAutoCreateRowSorter(true);
		JScrollPane scrollRecherche= new JScrollPane(tableMorceaux);
		scrollRecherche.setMinimumSize(new Dimension(800,120));
		scrollRecherche.setMaximumSize(new Dimension(800, 120));
		scrollRecherche.setPreferredSize(new Dimension(800, 120));
		panelTable.add(scrollRecherche);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;      //make this component tall
		c.weightx = 1.0;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 1;
		this.add(panelTable, c);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		//FIXME le mvc marche comment ici ? 
	}
	
	class TransferHandlerPerso extends TransferHandler
	{
		private static final long serialVersionUID = 1L;
		JTable table;
		public TransferHandlerPerso(JTable table)
		{
			this.table = table;
		}


		public boolean canImport(TransferHandler.TransferSupport info) {
			if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				System.out.println("trasfer handler NNOOO");
				return false;
			}
			return true;
		}

		public boolean importData(TransferHandler.TransferSupport support){
			System.out.println("je tranfer");
			if(!canImport(support))
				return false;

			Transferable data = support.getTransferable();

			try {
				data.getTransferData(DataFlavor.javaFileListFlavor);
				System.out.println("data : "+data);
				List<?> filelist = (List<?>) data.getTransferData(DataFlavor.javaFileListFlavor);

		        // Loop through the files to determine total size
		        int numfiles = filelist.size();
		        for (int i = 0; i < numfiles; i++) {
		        	File f = (File) filelist.get(i);
		        	ID3Reader id3Reader = new ID3Reader(f.getAbsolutePath());
		        	LillePlayer player = null;
		        	try {
						player = new LillePlayer(f.getAbsolutePath());
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
		        	int duration = player.getDuration();
		        	double secondes = duration/5;
		        	int minutes = (int)secondes;
		        	secondes = (secondes*100)-minutes;
		    		Musique musiqueActuelle = new Musique(id3Reader.getTitle(), id3Reader.getAlbum(), id3Reader.getArtist(), "genre",id3Reader.getYear(), ""+player.getDuration(),f.getAbsolutePath());
		    		controlleur.ajouterMusique(musiqueActuelle);
		    		MAJTable(musiqueActuelle);
		        }
		        
		        
		        
			} catch (UnsupportedFlavorException e){
				System.out.println("UnsupportedFlavorException");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
			}
			return false;
		}

		protected void exportDone(JComponent c, Transferable t, int action){
			System.out.println("exportDone");
		}

		protected Transferable createTransferable(JComponent c) {
			return null;
		}

		public int getSourceActions(JComponent c) {
			return COPY;
		}   

	}
	
	//FIXME je n'ai pas trouv� d'autre moyen que de tout r�initialiser poiur avoir les nouvelles musiques
	public void MAJTable(Musique musiqueActuelle)
	{
		System.out.println("MAJTABLE");
		tableMorceaux =  new JTable(new TablePerso());
		/*
		AbstractTableModel tableModel = (AbstractTableModel) tableMorceaux.getModel();
		tableModel.setValueAt(""+numDerniereLigne, numDerniereLigne, 0);
		tableModel.setValueAt(musiqueActuelle.artiste, numDerniereLigne, 1);
		tableModel.setValueAt(musiqueActuelle.titre, numDerniereLigne, 2);
		tableModel.setValueAt(musiqueActuelle.album, numDerniereLigne, 3);
		tableModel.setValueAt(musiqueActuelle.genre, numDerniereLigne, 4);
		tableModel.setValueAt(musiqueActuelle.annee, numDerniereLigne, 5);
		tableModel.setValueAt(musiqueActuelle.duree, numDerniereLigne, 6);
		tableModel.setValueAt(""+0, numDerniereLigne, 6);
		numDerniereLigne++;
		tableModel.fireTableDataChanged();*/
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
			System.out.println("init table");
			nomsColonnes = new String[8];
			nomsColonnes[0] = "Numéro";
			nomsColonnes[1] = "Artiste";
			nomsColonnes[2] = "Titre";
			nomsColonnes[3] = "Album";
			nomsColonnes[4] = "Genre";
			nomsColonnes[5] = "Ann�e";
			nomsColonnes[6] = "Dur�e";
			nomsColonnes[7] = "Nombre d'écoutes";

			donnees = controlleur.getMusiques();
			
		}
	}

}
