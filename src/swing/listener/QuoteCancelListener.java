package swing.listener;

import com.ryd.stockanalysis.protocol.NettyMessage;
import swing.ClientConstants;
import swing.frame.QuotePriceJDialog;
import swing.service.impl.MessageServiceImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuoteCancelListener extends MouseAdapter implements ActionListener {

	JTable table;

	public QuoteCancelListener(JTable table) {
          this.table = table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			String quoteId = (String) table.getValueAt(selectedRow,7);

			NettyMessage msg = new NettyMessage();
			msg.setMsgObj(ClientConstants.stQuoteMap.get(quoteId));
			msg.setMsgType(ClientConstants.STQUOTE_RECALL);

			MessageServiceImpl.sendMessage(msg);
		}
	}
}
