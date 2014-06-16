import java.io.Serializable;
import java.util.Date;

public class Dlugi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String osoba;
	String opis;
	double kwota;
	Date data;
	
	public Dlugi(Date date,String os, double k, String op)
	{
		osoba=os;
		opis=op;
		kwota=k;
		data=date;
	}
	public String getOsoba() { return osoba; }
	public String getOpis() { return opis; }
	public double getKwota() { return kwota; }
	public Date getData() { return data; }
	
	public void zmien_data(Date nowa) { data=nowa; }
	public void zmien_opis(String nowy) { opis=nowy; }
	public void zmien_kwota(double nowa) { kwota=nowa; }
	public void zmien_osoba(String nowa) { osoba=nowa; }
}
