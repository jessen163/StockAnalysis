package swing.frame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import swing.listener.LoginListener;

public class LoginFrame extends JFrame implements Runnable {
	private static LoginFrame loginFrame;
	private JPanel outer;
	private JLabel one;
	private JTextField accountId;
	private JButton loginBtn, cancelBtn;

	public static LoginFrame instance() {
		if (loginFrame == null)
			loginFrame = new LoginFrame();
		return loginFrame;
	}
	
	GridBagLayout g=new GridBagLayout();
	GridBagConstraints c=new GridBagConstraints();

	public LoginFrame() {
		super("登录");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(g);
      
        addComponent();
		LoginListener loginListener = new LoginListener(accountId, loginBtn, cancelBtn);
		loginBtn.addActionListener(loginListener);
		cancelBtn.addActionListener(loginListener);
		
		setLocationRelativeTo(null);
	}
	
    public void addComponent(){
    	getContentPane().setLayout(null);
		{
			outer = new JPanel();
			getContentPane().add(outer);
			outer.setBounds(41, 34, 313, 194);
			outer.setLayout(null);
			{
				one = new JLabel();
				outer.add(one);
				one.setText("帐号");
				one.setBounds(44, 56, 69, 15);
			}
			{
				accountId = new JTextField();
				outer.add(accountId);
				accountId.setText("");
				accountId.setBounds(119, 52, 133, 22);
			}
			{
				loginBtn = new JButton("登录");
				outer.add(loginBtn);
				loginBtn.setBounds(65, 110, 70, 25);
			}
			{
				cancelBtn = new JButton("退出");
				outer.add(cancelBtn);
				cancelBtn.setBounds(169, 110, 70, 25);
			}
		}
    }

	public void open() {
		accountId.setText("");
		setVisible(true);
	}

	public void exit() {
		System.exit(0);
	}

	@Override
	public void run() {
		instance().open();
	}

}