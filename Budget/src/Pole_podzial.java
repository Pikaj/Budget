import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


public class Pole_podzial implements Serializable  {
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		JButton button;
		JButton button2;
		JButton button3;
		JButton button4;
		Tabela tabela, tabela2;
	
	
		public Pole_podzial(JPanel card,String[] przycisk, boolean czy_dlugi)
		{
			JLabel label, label2;
			if (czy_dlugi)
			{
				label= new JLabel("<html><font size=5><center><font color=green>MOJE D£UGI</font></center></html>");
				label2= new JLabel("<html><font size=5><center><font color=green>LISTA D£U¯NIKÓW</font></center></font></html>");
			}else
			{
				label= new JLabel("<html><font size=5><center><font color=green>WYDATKI</font></center></html>");
				label2= new JLabel("<html><font size=5><center><font color=green>PRZYCHODY</font></center></font></html>");
			}
			
			label.setBounds(50, 20, 150, 20);
			card.add(label);
			
			label2.setBounds(450, 20, 200, 20);
			card.add(label2);
			
			JSeparator pion =new JSeparator(JSeparator.VERTICAL);
			pion.setBounds(400, 20, 1, 500);
			card.add(pion);
			
			button = new JButton(przycisk[0]);
			button2 = new JButton(przycisk[1]);
			button3 = new JButton(przycisk[2]);
			button4 = new JButton(przycisk[3]);
		}
		public void dwie_tabele(int x, JPanel card, AbstractTableModel model, 
								AbstractTableModel model2, int[] rozmiar)
		{
			button.setBounds(130, 205-4*x, 150, 25);
			card.add(button);
			
			button2.setBounds(530, 205-4*x, 150, 25);
			card.add(button2);
			
			button3.setBounds(130, 500-x, 150, 25);
			card.add(button3);
			
			button4.setBounds(530, 500-x, 150, 25);
			card.add(button4);
			
			tabela = new Tabela(model,rozmiar);
			tabela.get_Table().setBounds(20+x,240-2*x,350-x,250+x);
			tabela.get_Scroll().setBounds(20+x,240-2*x,350-x,250+x);
			
			tabela.get_Table().scrollRectToVisible(tabela.get_Table().getCellRect(tabela.get_Table().getRowCount()-1, 0, true));
			tabela.dodaj(card);
			
			tabela2 = new Tabela(model2,rozmiar);
			tabela2.get_Table().setBounds(420+x,240-2*x,350-x,250+x);
			tabela2.get_Scroll().setBounds(420+x,240-2*x,350-x,250+x);
			
			tabela2.get_Table().scrollRectToVisible(tabela.get_Table().getCellRect(tabela.get_Table().getRowCount()-1, 0, true));
			tabela2.dodaj(card);
		}
		public JButton getButton1() { return button; }
		public JButton getButton2() { return button2; }
		public JButton getButton3() { return button3; }
		public JButton getButton4() { return button4; }
		public JTable getTabela() { return tabela.get_Table(); }
		public JTable getTabela2() { return tabela2.get_Table(); }
		

}
