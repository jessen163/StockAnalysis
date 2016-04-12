package swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.awt.*;

import javax.swing.*;

import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.protocol.NettyMessage;
import swing.ClientConstants;
import swing.frame.*;
import swing.service.impl.MessageServiceImpl;

public class QuotePriceListener extends MouseAdapter implements ActionListener {

	private JTextField textAccountName,textStockId,textAmount,textQuotePrice;
	private JRadioButton buyOrSellBuy,buyOrSellSell;
	private JButton ensure;
	private JButton cancel;


	public QuotePriceListener(JTextField textAccountName,JTextField textStockId,JTextField textQuotePrice,JTextField textAmount,
			JRadioButton buyOrSellBuy, JRadioButton buyOrSellSell, JButton ensure, JButton cancel) {
		this.textAccountName = textAccountName;
		this.textStockId = textStockId;
		this.textQuotePrice = textQuotePrice;
		this.textAmount = textAmount;
		this.buyOrSellSell = buyOrSellSell;
		this.buyOrSellBuy = buyOrSellBuy;
		this.ensure = ensure;
		this.cancel = cancel;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == ensure) {
			NettyMessage msg = new NettyMessage();

			StQuote quote = new StQuote();
			quote.setAccountId(QuotePriceJDialog.instance().accountId);
			quote.setStockId(QuotePriceJDialog.instance().stockCode);
			quote.setQuotePrice(Double.valueOf(textQuotePrice.getText()));
			quote.setAmount(Integer.valueOf(textAmount.getText()));
			quote.setType((buyOrSellBuy.isSelected()==true?1:2));

			msg.setMsgObj(quote);
			msg.setMsgType(ClientConstants.STQUOTE_PRICE);

			MessageServiceImpl.sendMessage(msg);

		} else if (e.getSource() == cancel) {
			QuotePriceJDialog.instance().setVisible(false);
		}
		
	}
}
