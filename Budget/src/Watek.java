import java.io.Serializable;
import java.util.Date;

import javax.swing.JLabel;


public class Watek extends Thread implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JLabel jpgLabel;
	public Watek(JLabel jpg)
	{
		jpgLabel=jpg;
	}
	public void run(){
		jpgLabel.setVisible(true);
		long temp=(new Date().getTime())+2000;
		while (new Date().getTime()<temp) {};
		jpgLabel.setVisible(false);
	}
}
