import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class MyModel_kat extends AbstractTableModel implements Serializable 
	{
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		
		private ArrayList<String> dane;
		private ArrayList<Double> kwoty;
		private boolean czy_edytowac;
		DecimalFormat df= new DecimalFormat("#.##");
		
		public MyModel_kat(ArrayList<String> baza, ArrayList<Double> suma, boolean edycja) 
		{ 
			dane=baza;
			kwoty=suma;
			czy_edytowac=edycja;
		}
		
		public int getColumnCount() { if (kwoty==null) return 1; else return 2; }

		public int getRowCount() { return dane.size(); }

		public Object getValueAt(int arg0, int arg1) 
		{ 
			if(arg1==0) return dane.get(arg0); 
			else return df.format(kwoty.get(arg0))+" z³";
		}
		public String getColumnName(int col) 
		{ 
			if (col==0) return "Nazwa";
			else return "Kwota";
		}
		
		public void setValueAt(Object aValue,int row,int column){ dane.set(row, aValue.toString()); }
		public boolean isCellEditable(int rowIndex, int columnIndex) { return czy_edytowac; }
		
		public void zmien_sumy(ArrayList<Double> nowe) { kwoty=nowe; }

	}