import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

//Klasa s³uzy do stworzenia labeli na pierwsza zak³dakê "Rozliczenie"
//Klase mo¿na uzupelnic o metody
public class Podsumowanie implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel l1,l2,l3,l4, l5, l6;
	String napis, napis2, napis3, napis4;
	
	DecimalFormat df= new DecimalFormat("#.##");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	MyModel_kat model;
	MyModel_dlugi model2; 
	MyModel_dlugi model3; 
	
	
	public Podsumowanie (JPanel panel, Dane baza)
	{
		etykiety(baza);
		
		l1=new JLabel(napis);
		l1.setBounds(30, 5, 250, 120);
		panel.add(l1);
		
		JSeparator pion =new JSeparator(JSeparator.VERTICAL);
		pion.setBounds(265, 10, 1, 110);
		panel.add(pion);
		
		JSeparator poziom =new JSeparator(JSeparator.HORIZONTAL);
		poziom.setBounds(20, 130, 750, 1);
		panel.add(poziom);
		
		l2=new JLabel(napis2);
		l2.setBounds(285, 20, 300, 80);
		panel.add(l2);
		
		JSeparator pion3 =new JSeparator(JSeparator.VERTICAL);
		pion3.setBounds(540, 10, 1, 110);
		panel.add(pion3);
		
		String str = "<html><font size=5>Aktualizacja</font><font size=4>" +
					"<br><br>Konto zwyk³e:<br>Konto oszcz.:</font></html>";
		JLabel lab = new JLabel(str);
		lab.setBounds(560, 5, 200, 80);
		panel.add(lab);
		
		JTextField konto1=new JTextField(df.format(baza.get_konto1()));
		konto1.setBounds(660, 45, 80, 20);
		panel.add(konto1);
		
		JTextField konto2=new JTextField(df.format(baza.get_konto2()));
		konto2.setBounds(660, 68, 80, 20);
		panel.add(konto2);
		
		str="<html><font size=4>z³<br>z³</font></html>";
		JLabel lab2 = new JLabel(str);
		lab2.setBounds(745, 45, 40, 40);
		panel.add(lab2);
		
		JButton aktualizacja = new JButton("Aktualizuj");
		aktualizacja.setBounds(590, 100, 150, 20);
		aktualizacja.addActionListener(zapisz_kwoty(konto1, konto2, baza));
		aktualizacja.setToolTipText("Stan kont zostanie zmieniony.");
		panel.add(aktualizacja);
		
		l3=new JLabel(napis3);
		l3.setBounds(30, 110, 300, 200);
		panel.add(l3);
		
		ArrayList<Double> kwoty = new ArrayList<Double>();
		for (int i=0; i<baza.get_kat_wydatki().size(); i++) kwoty.add(baza.get_suma_kat_wydatki(i));
		
		model = new MyModel_kat(baza.get_kat_wydatki(), kwoty, false);
		
		int[] rozmiary={100,50};
		Tabela tabela = new Tabela(model,rozmiary);
		tabela.get_Table().setBounds(30,280,250,240);
		tabela.get_Scroll().setBounds(30,280,250,240);
		tabela.dodaj(panel);
		
		JSeparator pion2 =new JSeparator(JSeparator.VERTICAL);
		pion2.setBounds(300, 140, 1, 380);
		panel.add(pion2);

		l4=new JLabel(napis4);
		l4.setBounds(330, 70, 300, 200);
		panel.add(l4);
		
		JSeparator poziom2 =new JSeparator(JSeparator.HORIZONTAL);
		poziom2.setBounds(310, 220, 460, 1);
		panel.add(poziom2);
		
		String napis5="<html><font size=5>Moje d³ugi:<br><br><br><br><br><br>Lista d³u¿ników:</font></html>";
		l6=new JLabel(napis5);
		l6.setBounds(330, 210, 400, 200);
		panel.add(l6);
		
		String[] kolumny = {"Data","Osoba","Kwota","Opis"};
		model2= new MyModel_dlugi(kolumny, baza.get_Dlugi(), false); 
		model3= new MyModel_dlugi(kolumny, baza.get_Pozyczki(), false); 
		
		int[] rozmiary2={50,100,50,150};
		Tabela tabela2 = new Tabela(model2,rozmiary2);
		tabela2.get_Table().setBounds(320,260,450,100);
		tabela2.get_Scroll().setBounds(320,260,450,100);
		tabela2.dodaj(panel);
		
		Tabela tabela3 = new Tabela(model3,rozmiary2);
		tabela3.get_Table().setBounds(320,400,450,120);
		tabela3.get_Scroll().setBounds(320,400,450,120);
		tabela3.dodaj(panel);
		
	}
	public void odswiez(final Dane baza)
	{
		ArrayList<Double> kwoty = new ArrayList<Double>();
		for (int i=0; i<baza.get_kat_wydatki().size(); i++) kwoty.add(baza.get_suma_kat_wydatki(i));
				
		model.zmien_sumy(kwoty);
				
		model.fireTableDataChanged();
		model2.fireTableDataChanged();
		model3.fireTableDataChanged();
				
		etykiety(baza);
		zmien_labele();
	}
	public void etykiety(Dane baza)
	{
		double temp, temp2, temp3;
		temp=baza.get_konto1()+baza.get_konto2();
		temp2=baza.getPozostalo();
		temp3=baza.getSaldo();
		napis="<html><font size=5>Stan konta: ";
		if (temp<0) napis+="<font color=red>";
		else napis+="<font color=green>";
		napis+=df.format(temp)+" z³</font><br><br>Pozosta³o: ";
		if (temp2<0) napis+="<font color=red>";
		else napis+="<font color=green>";
		napis+=df.format(temp2)+" z³</font><br><br>Saldo:  ";
		if (temp3<0) napis+="<font color=red>";
		else napis+="<font color=green>";
		napis+=df.format(temp3)+" z³</font></html>";
		
		napis2="<html><font size=5>Konto zwyk³e:  <font color=purple>"+ df.format(baza.get_konto1())+
				" z³</font>"+
				"<br><br>Konto oszcz.:  <font color=purple>"+ 
				df.format(baza.get_konto2())+" z³</font></html>";
		
		temp=baza.get_suma_wydatki();
		napis3="<html><font size=4>Do tej pory wydano:</font><br><br><font size=5>"+ 
				"Z rachunkami:  <font color=purple>"+ df.format(temp)+" z³</font><br>"+
				"Bez rachunków: <font color=purple>"+ 
				df.format(temp-baza.get_suma_kat_wydatki(0))+" z³</font><br><br>"+
				"</font><font size=4>Poszczególne:</font></html>";
		
		temp=baza.get_suma_kat_wydatki(1);
		temp2=baza.get_suma_kat_przychody(2);
		temp3=baza.get_suma_kat_przychody(2)-temp;
		napis4="<html><font size=5>Moje zarobki:  ";
		if (temp2>=0) napis4+="<font color=green>";
		else napis4+="<font color=red>";
		napis4+=df.format(temp2)+" z³</font><br>Moje wydatki: ";
		if (temp>=0) napis4+="<font color=green>";
		else napis4+="<font color=red>";
		napis4+=df.format(temp)+" z³</font><br>Pozosta³o: ";
		if (temp3>=0) napis4+="<font color=green>";
		else napis4+="<font color=red>";
		napis4+=df.format(temp3)+" z³</font></html>";
	}
	public void zmien_labele()
	{
		l1.setText(napis);
		l2.setText(napis2);
		l3.setText(napis3);
		l4.setText(napis4);
	}
	
	public ActionListener zapisz_kwoty(final JTextField k1, final JTextField k2, final Dane baza)
	{
		ActionListener a= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double 	kwota1=Double.parseDouble(k1.getText().replace(",", ".").split(" ")[0]),
						kwota2=Double.parseDouble(k2.getText().replace(",", ".").split(" ")[0]);
				baza.ustaw_stan(kwota1, kwota2);
				odswiez(baza);
			}
		};
		return a;
		
	}
}
