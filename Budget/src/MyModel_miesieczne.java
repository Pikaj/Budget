import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.table.AbstractTableModel;

public class MyModel_miesieczne extends AbstractTableModel implements Serializable 
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		DecimalFormat df= new DecimalFormat("#.##");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		private String[][] dane;
		String[] naglowki;
		boolean czy_wydatki;
		
		public MyModel_miesieczne(Date min, Dane baza, boolean wydatki) 
		{ 
			czy_wydatki=wydatki;
			if (czy_wydatki)
			{
				dane=wydatki_tabela(baza, min);
				naglowki= naglowki_wydatki(baza);
			}
			else
			{
				dane=miesieczne_tabela(baza, min);
				naglowki=naglowki_miesieczne(baza);
			}
		}
		public int getColumnCount() { return naglowki.length; }

		public int getRowCount() { return dane.length; }

		public Object getValueAt(int row, int col) {
			return dane[row][col];
		}
		public String getColumnName(int col)
		{
			return naglowki[col];
		}
		public void zmien(Date nowa, Dane baza) 
		{ 
			if (czy_wydatki) dane=wydatki_tabela(baza,nowa);
			else dane=miesieczne_tabela(baza,nowa);
		}
		
		
		public String[][] miesieczne_tabela(Dane baza, Date min)
		{
			@SuppressWarnings("deprecation")
			int month= min.getMonth(), year=min.getYear();
			String[][] tab = new String[12-month][5];
			String[] miesiace = {"Styczeñ","Luty","Marzec","Kwiecieñ",
								"Maj","Czerwiec","Lipiec","Sierpieñ","Wrzesieñ",
								"PaŸdziernik","Listopad","Grudzieñ"};
			Calendar cal = new GregorianCalendar();
			double suma;
			int ilosc;
			
			for (int i=month; i<12; i++)
			{
				tab[i-month][0]=miesiace[i];
				suma=baza.suma_wydatki_data(i,year);
				tab[i-month][1]=df.format(suma)+" z³";
				cal.set(year, i,1); //Miesiace od zera
				ilosc=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				tab[i-month][2]=Integer.toString(ilosc);
				tab[i-month][3]=df.format(suma/ilosc)+" z³";
				tab[i-month][4]=df.format(
						(suma-baza.suma_wydatki_data_kat(i,year, 0)-baza.suma_wydatki_data_kat(i,  year, 1))/
													ilosc)+" z³";
			}
			return tab;
		}
		public String[] naglowki_miesieczne(Dane baza)
		{
			String[] naglowki = new String[5];
			naglowki[0]="Miesi¹c";
			naglowki[1]="Suma";
			naglowki[2]="Liczba dni";
			naglowki[3]="Œr ca³oœæ";
			naglowki[4]="Œr wybrane";
			
			return naglowki;
		}
		
		public String[][] wydatki_tabela(Dane baza, Date min)
		{
			@SuppressWarnings("deprecation")
			int month= min.getMonth(), year=min.getYear();
			int ilosc_kat = baza.get_kat_wydatki().size();
			String[][] tab = new String[12-month][1+ilosc_kat];
			String[] miesiace = {"Styczeñ","Luty","Marzec","Kwiecieñ",
								"Maj","Czerwiec","Lipiec","Sierpieñ","Wrzesieñ",
								"PaŸdziernik","Listopad","Grudzieñ"};
			
			for (int i=month; i<12; i++)
			{
				tab[i-month][0]=miesiace[i];
				for (int j=0; j<ilosc_kat; j++) tab[i-month][j+1]=df.format(baza.suma_wydatki_data_kat(i,year,j))+" z³";
			}
			return tab;
		}
		
		public String[] naglowki_wydatki(Dane baza)
		{
			int ilosc_kat=baza.get_kat_wydatki().size();
			String[] naglowki = new String[1+ilosc_kat];
			naglowki[0]="Miesi¹c";
			for(int i=0; i<ilosc_kat; i++) naglowki[i+1]=baza.get_kat_wydatki().get(i);
			
			return naglowki;
		}

	}