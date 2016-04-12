package swing.main;

import swing.frame.LoginFrame;
import swing.frame.MainFrame;
import swing.net.StockClient;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;


public class MainClinet {

	public static void main(String[] args) throws Exception {
		
		new Thread(new LoginFrame()).start();
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}

		StockClient client = new StockClient();
		client.connect("127.0.0.1", 8888);

		new Thread(new MainFrame()).start();
	}
}
