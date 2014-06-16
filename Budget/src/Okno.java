import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class Okno implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame;
	JTabbedPane pane;
	CardLayout cardLayout = new CardLayout(); 
	public Okno(int width, int height, String title, Dimension rozmiar)
	{
		frame=new JFrame();
		frame.setVisible(false);
		frame.setResizable(true);
		frame.setSize(width,height);
		frame.setTitle(title);
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		frame.setIconImage(kit.getImage("icon.png"));
		
		pane = new JTabbedPane();
		pane.setPreferredSize(rozmiar);
		
		frame.getContentPane().add(pane, null);
		frame.pack();
	}
	public void location(int x, int y) { frame.setLocation(x,y); }
	public void exit_on_close() { frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); }
	public void visible() { frame.setVisible(true); }
	public void addCard(String title, JPanel panel) { pane.addTab(title, panel); } 
	public void addMenu(MenuBar m) { frame.setMenuBar(m); }
	public JTabbedPane getTPane() { return pane; }
	public JFrame getFrame() { return frame; }
}
