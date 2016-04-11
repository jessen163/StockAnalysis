package swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.*;

import javax.swing.*;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.protocol.NettyMessage;
import swing.ClientConstants;
import swing.frame.*;
import swing.service.MessageServiceI;
import swing.service.impl.MessageServiceImpl;

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
//				StAccount acc = new StAccount();
//				acc.setAccountNumber(jtfUserName.getText());
//				//登录
//				NettyMessage msg = new NettyMessage();
//				msg.setMsgObj(acc);
//				msg.setMsgType(ClientConstants.CLIENT_LOGIN);
//
//				//持仓
//				MessageServiceImpl.sendMessage(msg);
//				NettyMessage msg2 = new NettyMessage();
//
//				msg2.setMsgObj(acc);
//				msg2.setMsgType(ClientConstants.STSTOCK_POSITION);
//
//				//股票
//				MessageServiceImpl.sendMessage(msg2);
//				NettyMessage msg3 = new NettyMessage();
//
//				msg3.setMsgObj(null);
//				msg3.setMsgType(ClientConstants.STSTOCK_LIST);
//
//				MessageServiceImpl.sendMessage(msg3);

				LoginFrame.instance().setVisible(false);
				MainFrame.instance().open();

			}
		} else if (e.getSource() == cancel) {
			 System.exit(0);
		}
	}
}
