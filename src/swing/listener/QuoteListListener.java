package swing.listener;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.protocol.NettyMessage;
import swing.ClientConstants;
import swing.frame.LoginFrame;
import swing.frame.MainFrame;
import swing.frame.QuoteListDialog;
import swing.service.impl.MessageServiceImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuoteListListener extends MouseAdapter implements ActionListener {

	public QuoteListListener() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

			NettyMessage msg = new NettyMessage();
			msg.setMsgObj(ClientConstants.stAccount);
			msg.setMsgType(ClientConstants.STQUOTE_PRICE_LIST);

			MessageServiceImpl.sendMessage(msg);

	}
}
