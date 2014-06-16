import java.io.Serializable;
import java.util.Date;



public class Wpis implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Date data;
	String nazwa;
	double kwota;
	int rodzaj;
	public Wpis(Date d, String n, double k, int r)
	{
		data=d;
		nazwa=n;
		kwota=k;
		rodzaj=r;
	}
	public Wpis (Wpis kopia)
	{
		data=kopia.getData();
		nazwa=kopia.getNazwa();
		kwota=kopia.getKwota();
		rodzaj=kopia.getRodzaj();
	}
	public void zmien_kwota(double k) { kwota=k; }
	public void zmien_data(Date d) { data=d; }
	public void zmien_nazwa(String n) { nazwa=n; }
	public void zmien_rodzaj(int r) { rodzaj=r; }
	public Date getData() { return data; }
	public String getNazwa() { return nazwa; }
	public double getKwota() { return kwota; }
	public int getRodzaj() { return rodzaj; }
}
