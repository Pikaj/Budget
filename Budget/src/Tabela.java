
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


public class Tabela implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
		JTable table;
		JScrollPane scroll;
		
		DecimalFormat df= new DecimalFormat("#.##");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		public Tabela(AbstractTableModel model, int[] rozmiary)
		{
			table = new JTable(model);	
			
			if (rozmiary!=null && model!=null)
				for (int i=0; i<rozmiary.length; i++) 
					table.getColumnModel().getColumn(i).setPreferredWidth(rozmiary[i]);
			
			scroll= new JScrollPane(table); 
		}
		public void dodaj(JPanel panel)
		{
			panel.add(table.getTableHeader());
			panel.add(scroll);
		}
		public JTable get_Table() { return table; }
		public JScrollPane get_Scroll() { return scroll; }
}
