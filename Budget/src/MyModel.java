import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

public class MyModel extends AbstractTableModel implements Serializable 
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		DecimalFormat df= new DecimalFormat("#.##");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		private ArrayList<Wpis> dane=new ArrayList<Wpis>();
		private ArrayList<String> kategorie=new ArrayList<String>();
		private ArrayList<Double> rodzaj=new ArrayList<Double>();
		private ArrayList<Boolean> pokrycie=new ArrayList<Boolean>();
		JLabel label;
		double suma[];
		String[] nazwy_kolumn;
		boolean czy_edytowac;
		Podsumowanie glowna;
		Dane baza;
		
		public MyModel(String[] nazwy, ArrayList<Wpis> baza, ArrayList<String> kat) 
		{ 
			nazwy_kolumn=nazwy; 
			dane=baza;
			kategorie=kat;
			czy_edytowac=true;
		}
		public MyModel(MyModel nowy)
		{
			dane=nowy.getDane();
			kategorie=nowy.getKategoria();
			rodzaj=nowy.getRodzaj();
			pokrycie=nowy.getPokrycie();
			label=nowy.getLabel();
			suma=nowy.getSuma();
			nazwy_kolumn=nowy.getNazwy();
		}
		
		public ArrayList<Wpis> getDane() { return dane; }
		public ArrayList<String> getKategoria() { return kategorie; }
		public ArrayList<Double> getRodzaj() { return rodzaj; }
		public ArrayList<Boolean> getPokrycie() { return pokrycie; }
		public JLabel getLabel() { return label; }
		public double[] getSuma() { return suma; }
		public String[] getNazwy() { return nazwy_kolumn; }
		
		public void dodaj_kwote(double kwota, int i) { suma[i]+=kwota; }
		
		public void ustaw_sume(double[] nowa) { suma=nowa; }
		public void ustaw_label(JLabel nowy) { label=nowy; }
		public void ustaw_rodzaj(ArrayList<Double> nowy) { rodzaj=nowy; }
		public void ustaw_pokrycie(ArrayList<Boolean> nowe) { pokrycie=nowe; }
		public void ustaw_edycje(boolean edycja) { czy_edytowac=edycja; }
		public void ustaw_glowna(Podsumowanie p, Dane d)
		{
			glowna=p;
			baza=d;
		}
		
		public int getColumnCount() { return nazwy_kolumn.length; }

		public int getRowCount() { return dane.size(); }

		public Object getValueAt(int arg0, int arg1) {
			if (arg1==4) return df.format(rodzaj.get(arg0))+" z³";
			else if (arg1==5) return pokrycie.get(arg0);
			else
			{
				Wpis temp=dane.get(arg0);
				if (arg1==0) return sdf.format(temp.getData());
				else if (arg1==1) return temp.getNazwa();
				else if (arg1==2) return df.format(temp.getKwota())+" z³";
				else if (arg1==3) return kategorie.get(temp.getRodzaj());
				else return null;
			}
		}
		public String getColumnName(int col) { return nazwy_kolumn[col]; }
		
		public void zmien(ArrayList<Wpis> nowe) { dane=nowe; }
		public void zmien_model(MyModel nowy)
		{
			dane=nowy.getDane();
			kategorie=nowy.getKategoria();
			rodzaj=nowy.getRodzaj();
			pokrycie=nowy.getPokrycie();
			label=nowy.getLabel();
			suma=nowy.getSuma();
			nazwy_kolumn=nowy.getNazwy();
		}
		public void zamien(int i, int j)
		{
			Wpis temp=dane.get(i);
			dane.set(i, dane.get(j));
			dane.set(j, temp);
		}
		public void setValueAt(Object aValue,int row,int col)
		{ 
			if (col==0)
				try {
					dane.get(row).zmien_data(sdf.parse(aValue.toString()));
					
					if (nazwy_kolumn.length<5)
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
				}
			else if (col==1) dane.get(row).zmien_nazwa(aValue.toString());
			else if (col==2) 
			{
				if (nazwy_kolumn.length>4)
				{
					double stara=dane.get(row).getKwota(), 
						nowa=Double.parseDouble(aValue.toString().replace(",", ".").split(" ")[0]);
				
					dane.get(row).zmien_kwota(nowa);
				
					if (rodzaj.get(row)==stara) 
					{
						suma[1]-=stara;
						suma[1]+=nowa;
						rodzaj.set(row, nowa);
					}
					else if (rodzaj.get(row)==0)
					{
						suma[2]-=stara;
						suma[2]+=nowa;
					}
					else
					{
						suma[0]-=rodzaj.get(row);
						suma[0]+=nowa/2;
						rodzaj.set(row, nowa/2);
					}
				
					label.setText("<html><font color=red><font size=4>" +
						"Podsumowanie na osobê</font></font><br>" +
						"<ul><li>Wydatki dzielone: " +
						"<font size=4><font color=red>"+
						df.format(suma[0])+" z³</font></font></li>" +
						"<li>Moje wydatki: " +
						"<font size=4><font color=red>"+
						df.format(suma[1])+" z³ </font></font>" +
						" w sumie: <font color=red><font size=4>"+
						df.format(suma[1]+suma[0])+" z³ </font></font></li>"+ 
						"<li>Wydatki drugiej osoby: " +
						"<font size=4><font color=red>"+
						df.format(suma[2])+" z³ </font></font>" +
						" w sumie: <font size=4><font color=red>"+
						df.format(suma[2]+suma[0])+
						" z³ </font</font></li></html>");
				}else 
				{
					dane.get(row).zmien_kwota(Double.parseDouble(aValue.toString().replace(",", ".").split(" ")[0]));
					glowna.odswiez(baza);
				}
			}
			else if (col==3)
			{
				if (kategorie.contains(aValue)) dane.get(row).zmien_rodzaj(kategorie.indexOf(aValue));
				else {//Tutaj okienko b³edu
					}
				if (nazwy_kolumn.length<5) 
						glowna.odswiez(baza);
			}
			else if (col==4)
			{
				double 	stara=rodzaj.get(row),
						nowa=Double.parseDouble(aValue.toString().replace(",", ".").split(" ")[0]),
						kwota=dane.get(row).getKwota();
				
				if(stara==kwota) 	suma[1]-=stara;
				else if (stara==0) 	suma[2]-=kwota;
				else 				suma[0]-=stara;
				
				if(nowa==kwota)		suma[1]+=nowa;
				else if (nowa==0)	suma[2]+=kwota;
				else 				suma[0]+=nowa;
				
				rodzaj.set(row, nowa);
				
				label.setText("<html><font color=red><font size=4>" +
						"Podsumowanie na osobê</font></font><br>" +
						"<ul><li>Wydatki dzielone: " +
						"<font size=4><font color=red>"+
						df.format(suma[0])+" z³</font></font></li>" +
						"<li>Moje wydatki: " +
						"<font size=4><font color=red>"+
						df.format(suma[1])+" z³ </font></font>" +
						" w sumie: <font color=red><font size=4>"+
						df.format(suma[1]+suma[0])+" z³ </font></font></li>"+ 
						"<li>Wydatki drugiej osoby: " +
						"<font size=4><font color=red>"+
						df.format(suma[2])+" z³ </font></font>" +
						" w sumie: <font size=4><font color=red>"+
						df.format(suma[2]+suma[0])+
						" z³ </font</font></li></html>");
			}
			else if (col==5) 
				if (pokrycie.get(row)) pokrycie.set(row, false);
				else pokrycie.set(row, true);
			
			
		}
		public boolean isCellEditable(int row, int col) 
		{ 
			if (czy_edytowac) return true; 
			else return false; 
		}
		
	   	public Class<?> getColumnClass(int col) {
	         return getValueAt(0, col).getClass();
	   }

	}