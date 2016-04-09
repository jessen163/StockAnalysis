package swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.*;

import javax.swing.*;


import com.ryd.stockanalysis.bean.StAccount;
import swing.frame.*;

public class LoginListener extends MouseAdapter implements ActionListener {

	private JTextField jtfUserName;
	private JButton ensure;
	private JButton cancel;

	public LoginListener(JTextField jtfUserName,
			JButton ensure, JButton cancel) {
		this.jtfUserName = jtfUserName;
		this.ensure = ensure;
		this.cancel = cancel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jtfUserName || e.getSource() == ensure) {
			if (jtfUserName.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入帐号信息", "提示",
						JOptionPane.ERROR_MESSAGE);

			}else{
				 StAccount acc = new StAccount();
				 LoginFrame.instance().setVisible(false);
				 MainFrame.instance().open(acc);
			}
		} else if (e.getSource() == cancel) {
			 System.exit(0);
		}
	}
}
