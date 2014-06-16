import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class Card implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ImageIcon closeIcon = new ImageIcon("close_icon.gif");
	
	Dimension rozmiarButton = new Dimension(
            closeIcon.getIconWidth(),
            closeIcon.getIconHeight());
	
	JButton przycisk;
	
	JPanel katalog = new JPanel();
	JPanel zakladka = new JPanel();
	
	JLabel label;
	
	public Card(Dimension rozmiar, Okno frame, String name, boolean ikonka_zamknij)
	{	
		label = new JLabel(name);
		
		zakladka.setOpaque(false); //Przezroczystoœæ
		zakladka.add(label, BorderLayout.WEST);
		
		if(ikonka_zamknij)
		{
			przycisk = new JButton(closeIcon);
			przycisk.setPreferredSize(rozmiarButton);
			przycisk.addActionListener(zamknij_zakladke(frame.getTPane(), katalog));
			
			zakladka.add(przycisk, BorderLayout.EAST);
		}
		
		katalog.setPreferredSize(rozmiar);
		katalog.setLayout( null );
	}
	public JPanel getCard() { return katalog; }
	public JPanel getZakladka() { return zakladka; }
	
	public ActionListener zamknij_zakladke(final JTabbedPane pane,final JPanel panel)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int closeTabNumber = pane.indexOfComponent(panel);
				pane.removeTabAt(closeTabNumber);
			}
		};
		return a;
	}
}
