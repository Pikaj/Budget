import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Baza implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df= new DecimalFormat("#.##");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public Baza(Dane baza) throws IOException, ParseException
	{
		
		Dimension rozmiar = new Dimension(800,600);
		Okno frame = new Okno(800, 400, "Mój bud¿et", rozmiar);
		frame.location(100, 50);
		frame.exit_on_close();
		
		Card card = new Card(rozmiar, frame, "Podsumowanie", false);
		Podsumowanie ogolne = new Podsumowanie(card.getCard(),baza);
		
		PasekMenu menu = new PasekMenu();
		
		final ArrayList<String> tytuly = new ArrayList<String>();
		@SuppressWarnings("unused")
		Nasluch listener = new Nasluch(menu, baza, frame, tytuly, ogolne); 
		
		
		frame.addCard(null, card.getCard());
		frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
		//Do zamykania zakladek
		
		frame.addMenu(menu.get());
		frame.visible();
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException, ParseException
	{
		FileInputStream plik;
		Dane baza_danych;
		try {
			plik = new FileInputStream("baza.dat");
			ObjectInputStream odczyt = new ObjectInputStream(plik);

	
			baza_danych = (Dane) odczyt.readObject();
			
			odczyt.close();
			@SuppressWarnings("unused")
			Baza b = new Baza(baza_danych);
			
			/*
			File file = new File("1.txt");
		    Scanner in = new Scanner(file);
		 
		    String zdanie="";
		    String tab[];
		    Wpis nowy;
		    Date data= new Date();
			double kwota=0;
					
		    
		    while (in.hasNextLine())
		    {
		    	zdanie=in.nextLine();
		    	tab = zdanie.split("\t");
		    	data=sdf.parse(tab[0]);
		    	kwota=Double.parseDouble(tab[2].split(" ")[0].replace(",", "."));
				nowy = new Wpis(
						data,
						tab[1],
						kwota,
						baza_danych.get_kat_przychody().indexOf(tab[3]));
				baza_danych.get_Przychody().add(nowy);
		    }
			
		    in.close();
		    
			*/
		} catch (FileNotFoundException e) {
			baza_danych = new Dane();
			@SuppressWarnings("unused")
			Baza b = new Baza(baza_danych);
		}
	}
}

