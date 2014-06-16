import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.io.Serializable;


public class PasekMenu implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MenuBar pasek;
	
	public PasekMenu()
	{
		pasek = new MenuBar();
	}
	public void add(String name, String[] items, ActionListener[] list)
	{
		if (items.length==list.length)
		{
			Menu nowy= new Menu(name);
			for (int i=0; i<items.length; i++)
			{
				MenuItem item = new MenuItem(items[i]);
				item.addActionListener(list[i]);
				nowy.add(item);
			}
			pasek.add(nowy);
		}
	}
	public MenuBar get() { return pasek; }

}
