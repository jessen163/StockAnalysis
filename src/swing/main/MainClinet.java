package swing.main;

import swing.frame.LoginFrame;
import swing.frame.MainFrame;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;


public class MainClinet {

	public static void main(String[] args) {
		
		new Thread(new LoginFrame()).start();
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}
		new Thread(new MainFrame()).start();
	}
}
