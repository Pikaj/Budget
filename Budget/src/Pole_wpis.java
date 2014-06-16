import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Pole_wpis implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField data;
	JTextField kwota;
	JTextField nazwa;
	@SuppressWarnings("rawtypes")
	JComboBox combo;
	
	JTextField opis;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pole_wpis(JPanel card, int przesuniecie, Dane baza, boolean czy_wydatki, boolean czy_dlugi)
	{
		JLabel label;
		if (czy_dlugi)
		{
			label= new JLabel("<html><font size=4>Data:<br><br>Osoba:<br><br>Kwota:<br><br>Opis:</font></html>");
		}else
		{
			label= new JLabel("<html><font size=4>Data:<br><br>Nazwa:<br><br>Kwota:<br><br>Kategoria:</font></html>");
		}
		
		label.setBounds(30+przesuniecie, 50, 70, 150);
		card.add(label);
		
		JLabel label2= new JLabel("<html><font size=4>z³</font></html>");
		label2.setBounds(165+przesuniecie, 135, 20, 20);
		card.add(label2);
		
		data = new JTextField(sdf.format(new Date()));
		data.setBounds(80+przesuniecie, 55, 80, 25);
		data.setToolTipText("Wpisz datê w formacie: rrrr-mm-dd");
		card.add(data);
		
		nazwa = new JTextField();
		nazwa.setBounds(90+przesuniecie, 95, 200, 25);
		card.add(nazwa);
		
		kwota = new JTextField();
		kwota.setBounds(90+przesuniecie, 135, 70, 25);
		card.add(kwota);
		
		if (czy_dlugi)
		{
			opis = new JTextField();
			opis.setBounds(80+przesuniecie, 170, 200, 25);
			card.add(opis);
		}else{
			if (czy_wydatki) combo= new JComboBox(baza.get_kat_wydatki().toArray()); 
			else combo= new JComboBox(baza.get_kat_przychody().toArray());
			combo.setBounds(110+przesuniecie, 170, 200, 25);
			card.add(combo);
		} 
	}
	public JTextField getData() { return data; }
	public JTextField getKwota() { return kwota; }
	public JTextField getNazwa() { return nazwa; }
	public JTextField getOpis() { return opis; }
	@SuppressWarnings("rawtypes")
	public JComboBox getCombo() { return combo; }
}
