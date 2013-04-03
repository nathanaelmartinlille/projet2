import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JTextField;




public class PanelRecherche extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton boutonRecherche;
	private JTextField texteRecherche;
	private Timer timer;
	private TimerTask taskChercher;
	
	
	public PanelRecherche()
	{
		boutonRecherche = new JButton("Chercher");
		texteRecherche = new JTextField(50);
		
		
		
		initHandler();
		
		this.add(boutonRecherche, BorderLayout.WEST);
		this.add(texteRecherche, BorderLayout.EAST);
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
	}

	
}


/* Pour faire la recherche sur la base de données : 
listeMusique = new ArrayList<String[]>();


// load the sqlite-JDBC driver using the current class loader
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

	int i = 1;
	ResultSet rs = statement.executeQuery("select * from songs");
	while(rs.next())
	{
		String[] tableau = new String[7];
		tableau[0] = ""+i;
		tableau[1] = rs.getString("album");
		tableau[2] = rs.getString("artist");
		tableau[3] = rs.getString("title");
		tableau[4] = rs.getString("genre");
		tableau[5] = rs.getString("year");
		tableau[6] = rs.getString("duration");
		// read the result set
		System.out.println(i + " " + rs.getString("album") + " " + rs.getString("artist") + " " +
				rs.getString("title") + " " + rs.getString("genre") + " " + rs.getString("year") + " " + rs.getString("duration"));
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
}*/