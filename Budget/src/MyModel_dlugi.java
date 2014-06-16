import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class MyModel_dlugi extends AbstractTableModel implements Serializable 
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		DecimalFormat df= new DecimalFormat("#.##");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		private ArrayList<Dlugi> dane=new ArrayList<Dlugi>();
		String[] nazwy_kolumn;
		boolean czy_edytowac;
		
		public MyModel_dlugi(String[] nazwy, ArrayList<Dlugi> baza, boolean edycja) 
		{ 
			nazwy_kolumn=nazwy; 
			dane=baza;
			czy_edytowac=edycja;
		}
		public int getColumnCount() { return 4; }

		public int getRowCount() { return dane.size(); }

		public Object getValueAt(int arg0, int arg1) {
			Dlugi temp=dane.get(arg0);
			if (arg1==0) return sdf.format(temp.getData());
			else if (arg1==1) return temp.getOsoba();
			else if (arg1==2) return df.format(temp.getKwota())+" z³";
			else if (arg1==3) return temp.getOpis();
			else return null;
		}
		public void zamien(int i, int j)
		{
			Dlugi temp=dane.get(i);
			dane.set(i, dane.get(j));
			dane.set(j, temp);
		}
		public void setValueAt(Object aValue,int row,int col)
		{ 
			if (col==0)
				try {
					dane.get(row).zmien_data(sdf.parse(aValue.toString()));
					if (row!=0 && dane.get(row).getData().before(dane.get(row-1).getData()))
					{
						for (int i=row; i>0; i--)
						{
							if(dane.get(i).getData().equals(dane.get(i-1).getData()) || 
									dane.get(i).getData().after(dane.get(i-1).getData())) break;
							
							zamien(i, i-1);
						}
					}
					else if (row!=dane.size()-1 && dane.get(row).getData().after(dane.get(row+1).getData()))
					{
						for (int i=row; i<dane.size()-1; i++)
						{
							if(dane.get(i).getData().equals(dane.get(i+1).getData()) || 
									dane.get(i).getData().before(dane.get(i+1).getData())) break;
							
							zamien(i, i+1);
						}
					}
					this.fireTableDataChanged();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//Okienko b³edu Ÿle wpisana data
					e.printStackTrace();
				}
			else if (col==1) dane.get(row).zmien_osoba(aValue.toString());
			else if (col==2) dane.get(row).zmien_kwota(Double.parseDouble(aValue.toString().replace(",", ".").split(" ")[0]));
			else if (col==3) dane.get(row).zmien_opis(aValue.toString());
		}
		
		public String getColumnName(int col) { return nazwy_kolumn[col]; }
		public boolean isCellEditable(int row, int col) { return czy_edytowac; }

	}