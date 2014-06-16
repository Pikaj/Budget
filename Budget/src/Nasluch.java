import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;


public class Nasluch implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DecimalFormat df= new DecimalFormat("#.##");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	Dimension rozmiar = new Dimension(800,600);
	
	Okno frame;
	
	//Dane do stworzenia menu:
	String[] iPlik = {"Podsumowanie ogólne","Podsumowanie miesiêczne","Rozliczenie","Spis rozliczeñ","Zamknij"};
	String[] iDodaj = {"Nowy wydatek/przychód","Zarz¹dzaj d³ugami","Zarz¹dzaj kategoriami"};
	String[] iEdytuj = {"Baza wydatków","Baza przychodów"};
	String[] iPomoc = {"Instrukcja", "O programie"};
	//Dane do stworzenia tabeli:
	String[] naglowki = {"Data","Nazwa","Kwota","Kategoria"};
	String[] naglowki_dlugi = {"Data","Osoba","Kwota","Opis"};
	
	int[] rozmiary = {105,200,80,120};
	int[] rozmiary_dlugi = {105,120,80,200};
	
	
	public Nasluch(	PasekMenu menu, 
					final Dane baza, 
					final Okno okno,
					ArrayList<String> tytuly,
					Podsumowanie glowna) 
	{
		frame=okno;
		frame.getFrame().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Object[] options = { "Wychodzê", "Zostajê" };
				int n = JOptionPane.showOptionDialog(frame.getFrame(),
							"Czy chcesz zakoñczyæ dzia³anie programu?",
							"Potwierdzenie", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
				if (n == JOptionPane.OK_OPTION){
					try {
						zapisz_baze(baza);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(frame.getFrame(),
								"Nieudana próba zapisania bazy do pliku: baza.dat.",
							    "B³¹d",
							    JOptionPane.ERROR_MESSAGE); 
					}
					System.exit(0);
				}
			}
		});
		//Dodanie zapisywania przy zamknieciu okna 
		ActionListener[] plik = { 
								ogolne(baza),
								miesieczne(baza, "Miesiêczne"),
								rozliczenie(baza, "Rozliczenie", glowna),
								spis(baza.get_lista_zakupow(), baza.get_lista_tytulow(), "Spis rozliczeñ"),
								zamknij(baza) };
		menu.add("Plik",iPlik, plik);
		
		ActionListener[] dodaj = { 
								dodaj_wpis("Nowy wpis",baza, false, glowna),
								dodaj_wpis("D³ugi", baza, true, glowna),
								kategorie(baza, false, glowna) };
		menu.add("Narzêdzia",iDodaj, dodaj);
		
		ActionListener[] edytuj = {
								edytuj_baze(baza, true, glowna),
								edytuj_baze(baza, false, glowna) };
		menu.add("Edytuj",iEdytuj, edytuj);
		
		ActionListener[] pomoc = { instrukcja(),o_pogramie("O programie") };
		menu.add("Pomoc",iPomoc, pomoc);
	}
	
	public ActionListener zamknij(final Dane baza)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				try {
					zapisz_baze(baza);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame.getFrame(),
							"Nieudana próba zapisania bazy do pliku: baza.dat.",
						    "B³¹d",
						    JOptionPane.ERROR_MESSAGE); 
				}
				System.exit(0);
			}
		};
		return a;
	}
	public ActionListener instrukcja()
	{
		ActionListener instr=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					@SuppressWarnings("unused")
					Process p;
					p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler  instrukcja.pdf");
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(frame.getFrame(),"Nie pliku instrukcja.pdf",
								"Ostrze¿enie",
					    JOptionPane.WARNING_MESSAGE);
					}
			}
		};
		return instr;
	}
	public void zapisz_baze(Dane baza) throws IOException

	{
		FileOutputStream file = new FileOutputStream("baza.dat");
		ObjectOutputStream zapis = new ObjectOutputStream(file);
		zapis.writeObject(baza);
		
		zapis.flush();
		zapis.close();
	}
	
	public ActionListener o_pogramie(final String tytul)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card card;
				card= new Card(rozmiar, frame, tytul, true);
				
				ImageIcon jpg = new ImageIcon("logo.png");
		    	JLabel jpgLabel = new JLabel(); 
		    	jpgLabel.setIcon(jpg); 
		    	jpgLabel.setBounds(230, 10, 100, 100);
		    	card.getCard().add(jpgLabel);
		    	
		    	JLabel nazwa = new JLabel("<html><strong><font face='Times New Roman'>" +
		    								"<font color=green><font size=8><b>" +
		    								"Mój bud¿et 1.0</b></font></font></font></strong></html>");
		    	nazwa.setBounds(340, 30, 300, 50);
		    	card.getCard().add(nazwa);
		    	
		    	JLabel opis = new JLabel("<html><font size=4><center>"+
		    							"Program s³u¿y do prowadzenia w³asnego domowego bud¿etu.<br>" +
		    							"Posiada szereg funkcji, które pozwol¹ u¿ytkownikowi zapanowaæ<br>"+
		    							"nad wydatkami i zyskaæ pe³n¹ kontrolê finansow¹.<br>"+
		    							"Korzystaj¹c z programu masz mo¿liwoœæ archiwizaji wszystkich<br>"+
		    							"swoich kosztów, dostosowania kategorii do w³asnych potrzeb,<br>"+
		    							"zapisywania d³ugów i po¿yczek oraz tworzenia rozliczeñ miesiêcznych.<br>"+
		    							"Dla osób, które dziel¹ swoje wydatki z drug¹ osob¹, przygotowane<br>"+
		    							"zosta³y rozliczenia typu 'lista zakupów', które oblicz¹<br>"+
		    							"za Ciebie saldo zakupów w zale¿noœci od osoby, która p³aci³a<br>"+
		    							"oraz sposobu zapisania w bazie. "+
		    							"<font color=green>'Mój bud¿et'</font> zosta³ stworzony<br>"+
		    							"dla osób, które lubi¹ mieæ porz¹dek na swoim koncie<br>"+
		    							"oraz szukaja odpowiedzi na pytanie:<br>"+
		    							"<font color=green><u>Gdzie siê podzia³y moje pieni¹dze?</u>"+
		    							"</font></center></font></html>");
		    	opis.setBounds(170, 60, 500, 400);
		    	card.getCard().add(opis);
		    	
		    	JLabel prawa = new JLabel("<html><center>Program zosta³ stworzony jako projekt w³asny.<br>"+
		    								"Je¿eli jesteœ zainteresowany wspó³prac¹<br> lub masz "+
		    								"jakieœ uwagi/pytania napisz na:<strong>" +
		    								"<font color=red> anna.adam@onet.pl </font></strong>" +
		    								"<br>Program jest bezp³atny i przeznaczony jedynie do celów prywatnych." +
		    								"<br><font color=red><strong>Wszystkie prawa zastrze¿one:" +
		    								" © Anna Adam</strong></font></center></html>");
		    	prawa.setBounds(230, 420, 500, 100);
		    	card.getCard().add(prawa);
		    	
				frame.addCard(null, card.getCard());
				frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
				frame.getTPane().setSelectedComponent(card.getCard());
			}
		};
		return a;
	}
	
	
	public ActionListener edytuj_baze(	final Dane baza, 
										final Boolean czy_wydatki,
										final Podsumowanie glowna)
	{
		ActionListener a= new ActionListener() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void actionPerformed(ActionEvent e) {
				Card card;
				if (czy_wydatki) card= new Card(rozmiar, frame,"Baza wydatków", true);
				else card= new Card(rozmiar, frame,"Baza przychodów", true);
				
				JLabel label= new JLabel("Wybierz kategoriê:");
				label.setBounds(40, 10, 150, 20);
				card.getCard().add(label);
				
				JComboBox combo;
				if (czy_wydatki) combo= new JComboBox(baza.get_kat_wydatki().toArray()); 
				else combo= new JComboBox(baza.get_kat_przychody().toArray()); 
				combo.setBounds(160, 10, 200, 20);
				card.getCard().add(combo);
				
				JButton button = new JButton("Poka¿ wybran¹");
				button.setBounds(380, 5, 150, 25);
				card.getCard().add(button);
				
				JButton button2 = new JButton("Poka¿ wszystkie");
				button2.setBounds(610, 5, 150, 25);
				card.getCard().add(button2);
				
				MyModel model;
				Tabela tabela;
				if (czy_wydatki) 
				{
					model = new MyModel(naglowki, baza.get_Wydatki(), baza.get_kat_wydatki());
					tabela = new Tabela(model,rozmiary);
				}
				else 
				{
					model = new MyModel(naglowki, baza.get_Przychody(), baza.get_kat_przychody());
					tabela = new Tabela(model, rozmiary);
				}
				model.ustaw_glowna(glowna, baza);
				tabela.get_Table().setBounds(30,40,730,470);
				tabela.get_Scroll().setBounds(30,40,730,470);
				tabela.get_Table().scrollRectToVisible(tabela.get_Table().getCellRect(tabela.get_Table().getRowCount()-1, 0, true));
				
				tabela.dodaj(card.getCard());
				
				button.addActionListener(wybierz_kategorie(czy_wydatki,model, combo, baza));
				button2.addActionListener(pokaz_wszystkie(czy_wydatki,model, baza));
				
				frame.addCard(null, card.getCard());
				frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
				frame.getTPane().setSelectedComponent(card.getCard());//Do zmieniania zakladek 
			}
		};
		return a;
	}
	@SuppressWarnings("rawtypes")
	public ActionListener wybierz_kategorie(final Boolean czy_wydatki, 
											final MyModel model, 
											final JComboBox combo, 
											final Dane baza)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				if (czy_wydatki) model.zmien(baza.get_Wydatki_wg_kat(combo.getSelectedIndex()));
				else model.zmien(baza.get_Przychody_wg_kat(combo.getSelectedIndex()));
				model.fireTableDataChanged();
			}
		};
		return a;
	}
	public ActionListener pokaz_wszystkie(final Boolean czy_wydatki, final MyModel model, final Dane baza)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				if (czy_wydatki) model.zmien(baza.get_Wydatki());
				else model.zmien(baza.get_Przychody());
				model.fireTableDataChanged();
			}
		};
		return a;
	}
	public ActionListener kategorie(	final Dane baza, 
										final boolean czy_dlugi,
										final Podsumowanie glowna)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card card;
				card= new Card(rozmiar, frame,"Kategorie", true);
				
				JLabel label= new JLabel("Wpisz nazwê kategorii:");
				label.setBounds(20, 50, 300, 80);
				card.getCard().add(label);
				
				JLabel label2= new JLabel("Wpisz nazwê kategorii:");
				label2.setBounds(420, 50, 300, 80);
				card.getCard().add(label2);
				
				JTextField text= new JTextField();
				text.setBounds(155, 80, 200, 20);
				card.getCard().add(text);
				
				JTextField text2= new JTextField();
				text2.setBounds(555, 80, 200, 20);
				card.getCard().add(text2);
				
				String[] str={"Dodaj kategoriê","Dodaj kategoriê","Usuñ kategoriê", "Usuñ kategoriê"};
				Pole_podzial pole = new Pole_podzial(card.getCard(), str, czy_dlugi);
				
				MyModel_kat model = new MyModel_kat(baza.get_kat_wydatki(), null, true);
				MyModel_kat model2 = new MyModel_kat(baza.get_kat_przychody(), null, true);
				
				int[] rozmiar={200};
				pole.dwie_tabele(20, card.getCard(), model, model2, rozmiar);
				
				ImageIcon jpg = new ImageIcon("dymek.png");
		    	JLabel jpgLabel = new JLabel(); 
		    	jpgLabel.setIcon(jpg); 
		    	jpgLabel.setVisible(false);
		    	card.getCard().add(jpgLabel);
				
				pole.getButton1().addActionListener(
						zmien_dane(text, baza.get_kat_wydatki(), baza, model, glowna, jpgLabel, true));
				pole.getButton2().addActionListener(
						zmien_dane(text2, baza.get_kat_przychody(), baza, model2, glowna, jpgLabel, false));
				
				pole.getButton3().addActionListener(
						usuwanie_wiersza(	pole.getTabela(), 
											baza, 
											glowna,
											true,
											czy_dlugi,
											true,
											model));
				pole.getButton4().addActionListener(
						usuwanie_wiersza(	pole.getTabela2(), 
											baza, 
											glowna,
											true,
											czy_dlugi,
											false,
											model2));
				
				frame.addCard(null, card.getCard());
				frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
				frame.getTPane().setSelectedComponent(card.getCard());//Do zmieniania zakladek 
			}
		};
		return a;
	}
	public ActionListener zmien_dane(	final JTextField text, 
										final ArrayList<String> baza, 
										final Dane dane,
										final AbstractTableModel model,
										final Podsumowanie glowna,
										final JLabel jpgLabel,
										final boolean czy_wydatki)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				baza.add(text.getText());
				text.setText("");
				
				if (czy_wydatki) jpgLabel.setBounds(240, 10, 150, 50);
				else jpgLabel.setBounds(640, 10, 150, 50);
				Watek w = new Watek(jpgLabel);
				w.start();
				
				model.fireTableDataChanged();
				glowna.odswiez(dane);
			}
		};
		return a;
	}
	
	
	public ActionListener dodaj_wpis(	final String tytul,
										final Dane baza, 
										final boolean czy_dlugi,
										final Podsumowanie glowna)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card card;
				card= new Card(rozmiar, frame,tytul, true);
				
				Pole_wpis pole1=new Pole_wpis(card.getCard(),0,baza, true, czy_dlugi);
				Pole_wpis pole2=new Pole_wpis(card.getCard(),400,baza, false, czy_dlugi);
				
				ImageIcon jpg = new ImageIcon("dymek.png");
		    	JLabel jpgLabel = new JLabel(); 
		    	jpgLabel.setIcon(jpg); 
		    	jpgLabel.setVisible(false);
		    	card.getCard().add(jpgLabel);
				
				Pole_podzial pole_podzial; 
				if (czy_dlugi) 
				{
					String[] str = {"Dodaj d³ug","Dodaj d³u¿nika","Usuñ d³ug", "Usuñ d³u¿nika"};
					pole_podzial= new Pole_podzial(card.getCard(),str, czy_dlugi);
				}
				else
				{
					String[] str2 = {"Dodaj wydatek","Dodaj przychód","Usuñ wydatek", "Usuñ przychód"};
					pole_podzial= new Pole_podzial(card.getCard(),str2, czy_dlugi);
				}
				if (czy_dlugi)
				{
					MyModel_dlugi model, model2;
					model = new MyModel_dlugi(naglowki_dlugi, baza.get_Dlugi(), true);
					model2 = new MyModel_dlugi(naglowki_dlugi, baza.get_Pozyczki(), true);
					
					pole_podzial.dwie_tabele(0, card.getCard(), model, model2, rozmiary_dlugi);
					
					pole_podzial.getButton1().addActionListener(
							odswiez_dlugi(pole1,
									baza.get_Dlugi(),
									baza, 
									model, 
									pole_podzial.getTabela(),
									glowna,
									jpgLabel,
									true));
					pole_podzial.getButton2().addActionListener(
							odswiez_dlugi(pole2,
									baza.get_Pozyczki(),
									baza, 
									model2, 
									pole_podzial.getTabela2(),
									glowna,
									jpgLabel,
									false));
					
					pole_podzial.getButton3().addActionListener(
							usuwanie_wiersza(	pole_podzial.getTabela(), 
												baza, 
												glowna,
												false,
												czy_dlugi,
												true,
												model));
					pole_podzial.getButton4().addActionListener(
							usuwanie_wiersza(	pole_podzial.getTabela2(), 
												baza, 
												glowna,
												false,
												czy_dlugi,
												false,
												model2));
				}else{
					MyModel model, model2;
					model = new MyModel(naglowki, baza.get_Wydatki(), baza.get_kat_wydatki());
					model.ustaw_glowna(glowna, baza);
					model2 = new MyModel(naglowki, baza.get_Przychody(), baza.get_kat_przychody());
					model2.ustaw_glowna(glowna, baza);
					
					pole_podzial.dwie_tabele(0, card.getCard(), model, model2, rozmiary);
			    	
			    	
					pole_podzial.getButton1().addActionListener(
							odswiez(pole1,baza.get_Wydatki(),
									baza,
									model,
									pole_podzial.getTabela(), 
									glowna,
									jpgLabel,
									true));
					pole_podzial.getButton2().addActionListener(
							odswiez(pole2,baza.get_Przychody(),
									baza,
									model2,
									pole_podzial.getTabela2(), 
									glowna,
									jpgLabel,
									false));
					
					pole_podzial.getButton3().addActionListener(
							usuwanie_wiersza(	pole_podzial.getTabela(), 
												baza, 
												glowna,
												false,
												czy_dlugi,
												true,
												model));
					pole_podzial.getButton4().addActionListener(
							usuwanie_wiersza(	pole_podzial.getTabela2(), 
												baza, 
												glowna,
												false,
												czy_dlugi,
												false,
												model2));
				}
		    	
				frame.addCard(null, card.getCard());
				frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
				frame.getTPane().setSelectedComponent(card.getCard());//Do zmieniania zakladek 
			}
		};
		return a;
	}
	public ActionListener odswiez(	final Pole_wpis pole, 
									final ArrayList<Wpis> baza, 
									final Dane dane,
									final AbstractTableModel model,
									final JTable tabela,
									final Podsumowanie glowna,
									final JLabel jpgLabel,
									final boolean czy_wydatki)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				Wpis nowy;
				Date data= new Date();
				double kwota=0;
				try{
					data=sdf.parse(pole.getData().getText());
					try{
		
						kwota=Double.parseDouble(pole.getKwota().getText().replace(",", "."));
						nowy = new Wpis(
								data,
								pole.getNazwa().getText(),
								kwota,
								pole.getCombo().getSelectedIndex());
					if (baza.size()==0 || nowy.getData().equals(baza.get(baza.size()-1).getData()) || 
								nowy.getData().after(baza.get(baza.size()-1).getData()))
							baza.add(nowy);
					else
						for(int i=baza.size()-2; i>=0; i--)
							if (nowy.getData().equals(baza.get(i).getData()) || 
									nowy.getData().after(baza.get(i).getData()))
							{
								baza.add(baza.get(baza.size()-1));
								for(int j=baza.size()-2; j>i+1; j--)
								{
									baza.set(j,baza.get(j-1));
								}
								baza.set(i+1, nowy);
								break;
							}
						
						pole.getNazwa().setText("");
						pole.getKwota().setText("");
						
						if (czy_wydatki) jpgLabel.setBounds(240, 10, 150, 50);
						else jpgLabel.setBounds(640, 10, 150, 50);
						Watek w = new Watek(jpgLabel);
						w.start();
						
						model.fireTableDataChanged();
						tabela.scrollRectToVisible(tabela.getCellRect(tabela.getRowCount()-1, 0, true));
						glowna.odswiez(dane);
					} catch (Exception e)
					{
						JOptionPane.showMessageDialog(frame.getFrame(),
								"Z³y format kwoty.",
							    "B³¹d",
							    JOptionPane.ERROR_MESSAGE); 
					}
				} catch (ParseException e)
				{
					JOptionPane.showMessageDialog(frame.getFrame(),
							"Z³y format daty.",
						    "B³¹d",
						    JOptionPane.ERROR_MESSAGE); 
				}
					
			} 
		};
    	
		return a;
	}
	public ActionListener usuwanie_wiersza(	final JTable table, 
											final Dane baza,
											final Podsumowanie glowna,
											final boolean czy_kategorie,
											final boolean czy_dlugi,
											final boolean czy_pole1,
											final AbstractTableModel  model)
	{
		
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				if(table.getSelectedRow()>=0)
				{
					if (czy_kategorie)
					{
						Object[] options = { "Usuñ", "Anuluj" };
						int n = JOptionPane.showOptionDialog(frame.getFrame(),
									"<html><center>Usuniêcie kategorii lub zmiana nazwy mo¿e" +
									"<br>spowodowaæ nieprawidlowe dzia³anie programu.<br>" +
									"Czy mimo to chcesz usun¹æ kategoriê?</center></html>",
									"Ostrze¿enie", JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, options,
									options[1]);
						if (n == JOptionPane.OK_OPTION){
							if(czy_pole1)
								baza.get_kat_wydatki().remove(table.getSelectedRow());
							else 
								baza.get_kat_przychody().remove(table.getSelectedRow());
						}
					}
					else if(czy_dlugi)
					{
						if(czy_pole1)
							baza.get_Dlugi().remove(table.getSelectedRow());
						else 
							baza.get_Pozyczki().remove(table.getSelectedRow());
					}else
					{
						if(czy_pole1)
							baza.get_Wydatki().remove(table.getSelectedRow());
						else 
							baza.get_Przychody().remove(table.getSelectedRow());
					}
					model.fireTableDataChanged();
					glowna.odswiez(baza);
				}else JOptionPane.showMessageDialog(frame.getFrame(),
						"Zaznacz wiersz, który chcesz usun¹æ.",
					    "B³¹d",
					    JOptionPane.ERROR_MESSAGE); 
			}
		};
		return a;
	}
	public ActionListener odswiez_dlugi(final Pole_wpis pole, 
										final ArrayList<Dlugi> baza, 
										final Dane dane,
										final AbstractTableModel model,
										final JTable tabela,
										final Podsumowanie glowna,
										final JLabel jpgLabel,
										final boolean czy_moje)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				Dlugi nowy;
				try{
					@SuppressWarnings("unused")
					Date data= new Date();
					@SuppressWarnings("unused")
					double kwota=0;
					data=sdf.parse(pole.getData().getText());
					try{
						kwota=Double.parseDouble(pole.getKwota().getText().replace(",", "."));
						nowy = new Dlugi(
								sdf.parse(pole.getData().getText()),
								pole.getNazwa().getText(),
								Double.parseDouble(pole.getKwota().getText().replace(",", ".")),
								pole.getOpis().getText());
						
						if (baza.size()==0 || nowy.getData().equals(baza.get(baza.size()-1).getData()) || 
								nowy.getData().after(baza.get(baza.size()-1).getData()))
							baza.add(nowy);
						else
							for(int i=baza.size()-2; i>=0; i--)
								if (nowy.getData().equals(baza.get(i).getData()) || 
										nowy.getData().after(baza.get(i).getData()))
								{
									baza.add(baza.get(baza.size()-1));
									for(int j=baza.size()-2; j>i+1; j--)
									{
										baza.set(j,baza.get(j-1));
									}
									baza.set(i+1, nowy);
									break;
								}
						
						if (czy_moje) jpgLabel.setBounds(240, 10, 150, 50);
						else jpgLabel.setBounds(640, 10, 150, 50);
						Watek w = new Watek(jpgLabel);
						w.start();
						
						model.fireTableDataChanged();
						tabela.scrollRectToVisible(tabela.getCellRect(tabela.getRowCount()-1, 0, true));
						glowna.odswiez(dane);
					} catch (Exception e)
					{
						JOptionPane.showMessageDialog(frame.getFrame(),
								"Z³y format kwoty.",
							    "B³¹d",
							    JOptionPane.ERROR_MESSAGE); 
					}
				} catch (ParseException e)
				{
					JOptionPane.showMessageDialog(frame.getFrame(),
							"Z³y format daty.",
						    "B³¹d",
						    JOptionPane.ERROR_MESSAGE); 
				}
			}
		};
		return a;
	}
	

	public ActionListener ogolne(final Dane baza)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				Card card= new Card(rozmiar, frame,"Podsumowanie", true);
				
				@SuppressWarnings("unused")
				Podsumowanie nowy=new Podsumowanie(card.getCard(), baza);
				
				frame.addCard(null, card.getCard());
				frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
				frame.getTPane().setSelectedComponent(card.getCard());//Do zmieniania zakladek 
			}
		};
		return a;
	}
	public ActionListener miesieczne(final Dane baza, final String tytul)
	{
		ActionListener a= new ActionListener() {
			@SuppressWarnings({ "deprecation", "rawtypes" })
			public void actionPerformed(ActionEvent e) {
				Card card;
				card= new Card(rozmiar, frame,tytul, true);
				
				Date min=baza.znajdz_date();
				Date dzis=new Date();
				
				JLabel label= new JLabel("Wybierz rok:");
				label.setBounds(130, 10, 150, 20);
				card.getCard().add(label);
				
				String[] lata=new String[dzis.getYear()-min.getYear()+1];
				for(int i=0; i<lata.length; i++) lata[i]=Integer.toString(min.getYear()+i+1900);
				
				@SuppressWarnings("unchecked")
				JComboBox combo= new JComboBox(lata); 
				combo.setBounds(210, 10, 100, 20);
				card.getCard().add(combo);
				
				JButton button = new JButton("Poka¿ podsumowanie miesiêczne");
				button.setBounds(350, 5, 250, 25);
				card.getCard().add(button);
				
				MyModel_miesieczne model = new MyModel_miesieczne(min,baza, false);
				Tabela tabela = new Tabela(model,null);
				
				tabela.get_Table().setBounds(10,40,770,215);
				tabela.get_Scroll().setBounds(10,40,770,215);
				tabela.dodaj(card.getCard());
				
				MyModel_miesieczne model2 = new MyModel_miesieczne(min,baza, true);
				Tabela tabela2 = new Tabela(model2,null);
				
				tabela2.get_Table().setBounds(10,260,770,215);
				tabela2.get_Scroll().setBounds(10,260,770,215);
				tabela2.dodaj(card.getCard());
				
				button.addActionListener(zmien_rok(model,model2, baza, combo));
				
				frame.addCard(null, card.getCard());
				frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
				frame.getTPane().setSelectedComponent(card.getCard());//Do zmieniania zakladek 
			}
		};
		return a;
	}
	@SuppressWarnings("rawtypes")
	public ActionListener zmien_rok(	final MyModel_miesieczne model, 
										final MyModel_miesieczne model2,
										final Dane baza, 
										final JComboBox rok)
	{
		ActionListener a=new ActionListener(){
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent a){
				Date min=baza.znajdz_date();
				if (min.getYear()+1900==Integer.parseInt(rok.getSelectedItem().toString()))
				{
					model.zmien(min, baza);
					model2.zmien(min,  baza);
				}
				else 
				{
					Date nowa = new Date(Integer.parseInt(rok.getSelectedItem().toString())-1900,0,1);
					model.zmien(nowa, baza);
					model2.zmien(nowa,baza);
				}
				model.fireTableDataChanged();
				model2.fireTableDataChanged();
			}
		};
		return a;
	}
	
	
	public ActionListener rozliczenie(	final Dane baza, 
										final String tytul,
										final Podsumowanie glowna)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card card;
				card= new Card(rozmiar, frame,tytul, true);
				
				JLabel label= new JLabel("<html><font size=5><center>" +
										"<font color=green>LISTA ZAKUPÓW</font></center></html>");
				label.setBounds(80, 20, 200, 20);
				card.getCard().add(label);
				
				Pole_wpis wpis = new Pole_wpis(card.getCard(),0, baza, true, false);
				
				JLabel label2 = new JLabel("<html><font size=4>Sposób zapisania w bazie:</font></html>");
				label2.setBounds(30,205, 200, 30);
				card.getCard().add(label2);
				
				JRadioButton b1 = new JRadioButton("Wlicz po³owê");
				b1.setSelected(true);
			    JRadioButton b2 = new JRadioButton("Wlicz ca³oœæ");
			    JRadioButton b3 = new JRadioButton("Nie wliczaj");
			    
			    b1.setToolTipText("Po³owa kwoty zostanie wliczona do twoich wydatków.");
			    b2.setToolTipText("Ca³a kwota zostanie doliczona do twoich wydatków.");
			    b3.setToolTipText("Kwota nie zostanie wliczona do twoich wydatkow.");
			    
			    JRadioButton[] tablica_radio={b1,b2,b3};

			    ButtonGroup group = new ButtonGroup();
			    group.add(b1);
			    group.add(b2);
			    group.add(b3);
			    
			    b1.setBounds(60, 240, 150, 20);
			    b2.setBounds(60, 270, 150, 20);
			    b3.setBounds(60, 300, 150, 20);
			    
			    card.getCard().add(b1);
			    card.getCard().add(b2);
			    card.getCard().add(b3);
			    
				JCheckBox check = new JCheckBox("<html><font size=4> Pokrycie wydatku</font></html>");
				check.setSelected(true);
				check.setToolTipText("Zaznacz pole je¿eli to ty p³aci³eœ za przedmiot.");
				check.setBounds(90, 330, 150, 30);
				card.getCard().add(check);
				
				
				JButton button = new JButton("Dodaj wydatek");
				button.setBounds(90, 370, 150, 25);
				card.getCard().add(button);
				
				JButton button2 = new JButton("<html><p align='center'><font size=4>" +
											"Zakoñcz rozliczenie<br>i oblicz saldo" +
											"</font></p></html>");
				button2.setBounds(50, 440, 200, 70);
				card.getCard().add(button2);
				
				JLabel podsumowanie=new JLabel("<html><font color=red><font size=4>" +
						"						Podsumowanie na osobê</font></font><br>" +
												"<ul><li>Wydatki dzielone: " +
												"<font size=4><font color=red>0 z³</font></font></li>"+
												"<li>Moje wydatki: "+
												"<font size=4><font color=red>0 z³ </font></font>"+
												"w sumie: <font size=4><font color=red>0 z³</font></font></li>" +
												"<li>Wydatki drugiej osoby: "+
												"<font size=4><font color=red>0 z³ </font></font>" +
												"w sumie: <font size=4><font color=red>0 z³" +
												"</font></font></li></html>");
				podsumowanie.setBounds(370, 430, 500, 90);
				card.getCard().add(podsumowanie);
				
				JSeparator pion =new JSeparator(JSeparator.VERTICAL);
				pion.setBounds(330, 420, 1, 100);
				card.getCard().add(pion);
				
				JSeparator poziom =new JSeparator(JSeparator.HORIZONTAL);
				poziom.setBounds(20, 420, 760, 1);
				card.getCard().add(poziom);
				
				String[] nazwy={"Data","Nazwa","Koszt","Kategoria","Wliczono","P³atnoœæ"};
				ArrayList<Wpis> zakupy = new ArrayList<Wpis>();
				ArrayList<Double> kwoty = new ArrayList<Double>();
				ArrayList<Boolean> pokrycie = new ArrayList<Boolean>();
				double[] suma={0,0,0};
				
				MyModel model = new MyModel(nazwy, zakupy, baza.get_kat_wydatki());
				
				model.ustaw_rodzaj(kwoty);
				model.ustaw_pokrycie(pokrycie);
				model.ustaw_sume(suma);
				model.ustaw_label(podsumowanie);
				
				int[] szerokosc={20,40,8,30,8,8};
				
				Tabela tabela = new Tabela(model,szerokosc);
				
				tabela.get_Table().setBounds(330,30,440,370);
				tabela.get_Scroll().setBounds(330,30,440,370);
				tabela.dodaj(card.getCard());
			
				ImageIcon jpg = new ImageIcon("dymek.png");
		    	JLabel jpgLabel = new JLabel(); 
		    	jpgLabel.setIcon(jpg); 
		    	jpgLabel.setBounds(620, 420, 150, 50);
		    	jpgLabel.setVisible(false);
		    	card.getCard().add(jpgLabel);
		    	
				button.addActionListener(dodaj_zakup(wpis, 
													zakupy, 
													kwoty,
													pokrycie,
													model, 
													check, 
													tablica_radio,
													podsumowanie,
													suma,
													jpgLabel,
													tabela.get_Table()));
				
				button2.addActionListener(saldo(	zakupy,
													kwoty,
													pokrycie,
													baza,
													model,
													card,
													glowna));
				
				frame.addCard(null, card.getCard());
				frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
				frame.getTPane().setSelectedComponent(card.getCard());//Do zmieniania zakladek 
			}
		};
		return a;
	}
	public ActionListener dodaj_zakup(final Pole_wpis pole, 
									final ArrayList<Wpis> baza, 
									final ArrayList<Double> wplaty, 
									final ArrayList<Boolean> pokrycie,
									final AbstractTableModel model,
									final JCheckBox check,
									final JRadioButton[] tab,
									final JLabel label,
									final double[] suma,
									final JLabel jpgLabel,
									final JTable tabela)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				Wpis nowy;
				@SuppressWarnings("unused")
				Date data= new Date();
				double kwota=0;
				try{
					data=sdf.parse(pole.getData().getText());
					try{
						kwota=Double.parseDouble(pole.getKwota().getText().replace(",", "."));
						nowy = new Wpis(
								sdf.parse(pole.getData().getText()),
								pole.getNazwa().getText(),
								kwota,
								pole.getCombo().getSelectedIndex());
						baza.add(nowy);
						if(tab[0].isSelected())
						{
							wplaty.add(kwota/2);
							suma[0]+=kwota/2;
						}else if (tab[1].isSelected())
						{
							wplaty.add(kwota);
							suma[1]+=kwota;
						}else
						{
							wplaty.add(0.0);
							suma[2]+=kwota;
						}
						
						pokrycie.add(check.isSelected());
						
						pole.getNazwa().setText("");
						pole.getKwota().setText("");
						
						Watek w = new Watek(jpgLabel);
						w.start();
						
						model.fireTableDataChanged();
						
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
						
						tabela.scrollRectToVisible(tabela.getCellRect(tabela.getRowCount()-1, 0, true));
					} catch (Exception e)
					{
						JOptionPane.showMessageDialog(frame.getFrame(),
								"Z³y format kwoty.",
							    "B³¹d",
							    JOptionPane.ERROR_MESSAGE); 
					}
				} catch (ParseException e)
				{
					JOptionPane.showMessageDialog(frame.getFrame(),
							"Z³y format daty.",
						    "B³¹d",
						    JOptionPane.ERROR_MESSAGE); 
				}
			}
		};
		return a;
	}
	public ActionListener saldo(final ArrayList<Wpis> zakupy,
								final ArrayList<Double> wlicz,
								final ArrayList<Boolean> pokrycie,
								final Dane dane,
								final MyModel model,
								final Card card,
								final Podsumowanie glowna)
	{
		ActionListener a=new ActionListener(){
			public void actionPerformed(ActionEvent a){
				if (zakupy.size()>1)
				{
					Object[] options = { "Zakoñcz", "Wróæ" };
					int n = JOptionPane.showOptionDialog(frame.getFrame(),
							"<html>Czy napewno chcesz zakoñczyæ rozliczenie?" +
							"<br>Po zakoñczeniu nie bêdzie ju¿ mo¿liwoœci dokonania zmian.</html>",
							"Potwierdzenie", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
					if (n == JOptionPane.OK_OPTION){
						double moja_kwota=0;
						double suma=0;
						ArrayList<Wpis> baza=dane.get_Wydatki();
						ArrayList<MyModel> lista=dane.get_lista_zakupow();
						Date min=zakupy.get(0).getData(), max=zakupy.get(0).getData();
				
						for (int i=0; i<zakupy.size(); i++)
						{
							if (pokrycie.get(i)) moja_kwota+=zakupy.get(i).getKwota();
							if (wlicz.get(i)!=0)
							{
								suma+=wlicz.get(i);
								if (zakupy.get(i).getKwota()==wlicz.get(i)) baza.add(zakupy.get(i));
								else 
								{
									Wpis nowy = new Wpis(zakupy.get(i));
									nowy.zmien_kwota(wlicz.get(i));
									baza.add(nowy);
								}
							}
							if (baza.get(i).getData().before(min)) min=baza.get(i).getData();
							if (baza.get(i).getData().after(max)) max=baza.get(i).getData();
						}
						
						String info="<html><font size=4>" +
							"Rozliczenie zostalo zapisane.<br>"+
							"Saldo koñcowe wynosi: </font><font size=4>";
				
						if (moja_kwota-suma<0) info+="<font color=red>"+
							df.format(moja_kwota-suma)+" z³</font></font></html>";
						else info+="<font color=green>"+
								df.format(moja_kwota-suma)+" z³</font></font></html>";
				
						JOptionPane.showMessageDialog(frame.getFrame(),info,"Saldo rozliczenia",1);
				
						lista.add(model); //Dodanie tabeli do bazy
				
						info="Zakupy od "+sdf.format(min)+" do "+sdf.format(max)+" Saldo: "+df.format(moja_kwota-suma)+" z³";
						while (dane.get_lista_tytulow().contains(info)) info+="*";
						dane.get_lista_tytulow().add(info); //Dodadnie tytulu
				
						int closeTabNumber = frame.getTPane().indexOfComponent(card.getCard());
						frame.getTPane().removeTabAt(closeTabNumber); //Zmkniecie zakladki
				

						okno_spis(lista, dane.get_lista_tytulow(), "Spis zakupów"); //Otworzenie karty ze spisem
						glowna.odswiez(dane);
					}
				}else JOptionPane.showMessageDialog(frame.getFrame(),
						"Rozliczenie jest puste.",
					    "B³¹d",
					    JOptionPane.ERROR_MESSAGE); 
			}
		};
		return a;
	}
	
	
	public ActionListener spis(	final ArrayList<MyModel> zakupy,
								final ArrayList<String> tytuly,
								final String tytul)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okno_spis(zakupy, tytuly, tytul);
			}
		};
		return a;
	}
	@SuppressWarnings("rawtypes")
	public ActionListener nowa_lista(	final JComboBox combo,
										final ArrayList<MyModel> zakupy,
										final MyModel model)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.zmien_model(zakupy.get(combo.getSelectedIndex()));
				model.ustaw_edycje(false);
				model.fireTableDataChanged();
			}
		};
		return a;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void okno_spis(	final ArrayList<MyModel> zakupy,
							final ArrayList<String> tytuly,
							final String tytul)
	{
		Card card;
		card= new Card(rozmiar, frame,tytul, true);
		
		JLabel label= new JLabel("Wybierz listê:");
		label.setBounds(30, 10, 150, 20);
		card.getCard().add(label);
		
		JComboBox combo= new JComboBox(tytuly.toArray());
		combo.setBounds(120, 10, 400, 20);
		card.getCard().add(combo);
		
		JButton button = new JButton("Poka¿ listê");
		button.setBounds(550, 5, 100, 25);
		card.getCard().add(button);
		
		JButton button2 = new JButton("Usuñ listê");
		button2.setBounds(660, 5, 100, 25);
		card.getCard().add(button2);
		
		int[] szerokosc={40,180,20,80,20,20};
		MyModel model;
		Tabela tabela; 
		if (zakupy.size()>0)
		{
			model = new MyModel(zakupy.get(zakupy.size()-1));
			combo.setSelectedIndex(zakupy.size()-1);
			
			model.ustaw_edycje(false);
			tabela = new Tabela(model, szerokosc);
			button.addActionListener(nowa_lista(combo,zakupy,model));
			button2.addActionListener(usun_liste(combo, zakupy, model, tytuly, card));
		}else tabela = new Tabela(null, szerokosc);
		
		tabela.get_Table().setBounds(30,40,730,470);
		tabela.get_Scroll().setBounds(30,40,730,470);
		tabela.dodaj(card.getCard());
		
		
		frame.addCard(null, card.getCard());
		frame.getTPane().setTabComponentAt(frame.getTPane().getTabCount()-1,card.getZakladka());
		frame.getTPane().setSelectedComponent(card.getCard());//Do zmieniania zakladek 
		
	}
	@SuppressWarnings("rawtypes")
	public ActionListener usun_liste(	 final JComboBox combo,
										final ArrayList<MyModel> zakupy,
										final MyModel model,
										final ArrayList<String> tytuly,
										final Card card)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(zakupy.size()>1)
				{
					if(tytuly.get(combo.getSelectedIndex()).equals(
							combo.getItemAt(combo.getSelectedIndex()).toString()))
					{
						if (combo.getSelectedIndex()!=0)
						{
							model.zmien_model(zakupy.get(0));
							model.ustaw_edycje(false);
							model.fireTableDataChanged();
						}else if (zakupy.size()==1)
						{
							model.zmien_model(zakupy.get(1));
							model.ustaw_edycje(false);
							model.fireTableDataChanged();
						}
						combo.removeItemAt(combo.getSelectedIndex());
						tytuly.remove(combo.getSelectedIndex()+1);
						zakupy.remove(combo.getSelectedIndex()+1);
					}else{
						combo.removeItemAt(combo.getSelectedIndex());
						tytuly.remove(combo.getSelectedIndex()+1);
						zakupy.remove(combo.getSelectedIndex()+1);
					}
				}else{
					
					combo.removeItemAt(combo.getSelectedIndex());
					tytuly.clear();
					zakupy.clear();
					
					int closeTabNumber = frame.getTPane().indexOfComponent(card.getCard());
					frame.getTPane().removeTabAt(closeTabNumber); //Zmkniecie zakladki
					
					okno_spis(zakupy, tytuly, "Spis zakupów");
				}
			}
		};
		return a;
	}
}
