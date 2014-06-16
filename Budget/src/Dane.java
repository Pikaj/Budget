import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Dane implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double konto1;
	double konto2;
	ArrayList<Wpis> wydatki=new ArrayList<Wpis>();
	ArrayList<Wpis> przychody=new ArrayList<Wpis>();
	ArrayList<String> kat_wydatki=new ArrayList<String>();
	ArrayList<String> kat_przychody=new ArrayList<String>();
	ArrayList<Dlugi> dlugi=new ArrayList<Dlugi>();
	ArrayList<Dlugi> pozyczki=new ArrayList<Dlugi>();
	ArrayList<MyModel> lista_zakupow=new ArrayList<MyModel>();
	ArrayList<String> tytuly=new ArrayList<String>();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df= new DecimalFormat("#.##");
	
	public Dane()
	{
		konto1=0;
		konto2=0;
	}
	
	public double get_suma_wydatki() 
	{ 
		double suma=0;
		for (int i=0; i<wydatki.size(); i++) suma+=wydatki.get(i).getKwota();
		return suma; 
	}
	public double get_suma_przychody() 
	{ 
		double suma=0;
		for (int i=0; i<przychody.size(); i++) suma+=przychody.get(i).getKwota();
		return suma; 
	}
	public double get_konto1() { return konto1; }
	public double get_konto2() { return konto2; }
	
	public ArrayList<MyModel> get_lista_zakupow() { return lista_zakupow; }
	public ArrayList<String> get_lista_tytulow() { return tytuly; }
	
	public void ustaw_stan(double k1, double k2) 
	{ 
		konto1=k1;
		konto2=k2;
	}
	
	public ArrayList<String> get_kat_wydatki() { return kat_wydatki; }
	public ArrayList<String> get_kat_przychody() {return kat_przychody; }
	
	public ArrayList<Wpis> get_Wydatki() { return wydatki; }
	public ArrayList<Wpis> get_Przychody() { return przychody; }
	
	public ArrayList<Dlugi> get_Dlugi() { return dlugi; }
	public ArrayList<Dlugi> get_Pozyczki() { return pozyczki; }
	
	public double getPozostalo() { return get_suma_przychody()-get_suma_wydatki(); }
	public double getSaldo() { return (	(konto1+konto2)-(getPozostalo()-suma_pozyczki())); }
	
	public double get_suma_kat_wydatki(int r)
	{
		double suma=0;
		for (int i=0; i<wydatki.size(); i++)
			if (wydatki.get(i).getRodzaj()==r) suma+=wydatki.get(i).getKwota();
		return suma;
	}
	
	public double get_suma_kat_przychody(int r)
	{
		double suma=0;
		for (int i=0; i<przychody.size(); i++)
			if (przychody.get(i).getRodzaj()==r) suma+=przychody.get(i).getKwota();
		return suma;
	}
	
	public ArrayList<Wpis> get_Wydatki_wg_kat(int kat)
	{
		ArrayList<Wpis> dane = new ArrayList<Wpis>();
		for (int i=0; i<wydatki.size(); i++)
			if (wydatki.get(i).getRodzaj()==kat) dane.add(wydatki.get(i));
		return dane;
	}
	
	public ArrayList<Wpis> get_Przychody_wg_kat(int kat)
	{
		ArrayList<Wpis> dane = new ArrayList<Wpis>();
		for (int i=0; i<przychody.size(); i++)
			if (przychody.get(i).getRodzaj()==kat) dane.add(przychody.get(i));
		return dane;
	}
	public Date znajdz_date()
	{
		Date min=new Date();
		for(int i=0; i<wydatki.size(); i++)
		{
			if(wydatki.get(i).getData().before(min)) min=wydatki.get(i).getData();
		}
		return min;
	}
	@SuppressWarnings("deprecation")
	public double suma_wydatki_data(int m, int y)
	{
		double suma=0;
		for (int i=0; i<wydatki.size(); i++) 
			if (wydatki.get(i).getData().getMonth()==m && 
				wydatki.get(i).getData().getYear()==y) 
					suma+=wydatki.get(i).getKwota();
		return suma;
	}
	@SuppressWarnings("deprecation")
	public double suma_wydatki_data_kat(int m, int y, int kat)
	{
		double suma=0;
		for (int i=0; i<wydatki.size(); i++) 
			if (wydatki.get(i).getData().getMonth()==m && 
				wydatki.get(i).getData().getYear()==y &&
				wydatki.get(i).getRodzaj()==kat) 
					suma+=wydatki.get(i).getKwota();
		return suma;
	}
	public double suma_pozyczki()
	{
		double suma=0;
		for (int i=0; i<pozyczki.size(); i++) suma+=pozyczki.get(i).getKwota();
		return suma;
	}
	public double suma_dlugi()
	{
		double suma=0;
		for (int i=0; i<dlugi.size(); i++) suma+=dlugi.get(i).getKwota();
		return suma;
	}
}
