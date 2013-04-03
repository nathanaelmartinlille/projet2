import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
