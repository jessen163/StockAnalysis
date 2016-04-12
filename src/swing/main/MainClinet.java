package swing.main;

import swing.frame.LoginFrame;
import swing.frame.MainFrame;
import swing.net.StockClient;

import java.awt.Font;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;


public class MainClinet {

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newCachedThreadPool();// 线程池
		es.execute(new ClientServer());

		LoginFrame.instance().open();

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}
	}
}

class ClientServer implements Runnable {
	@Override
	public void run() {
		try {
			StockClient client = new StockClient();
			client.connect("127.0.0.1", 8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
