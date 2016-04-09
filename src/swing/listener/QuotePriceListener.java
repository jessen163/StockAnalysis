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

import swing.frame.*;

public class QuotePriceListener extends MouseAdapter implements ActionListener {

	private String stockCode;
	private JTextField textAccountId,textStockId,textAmount,textQuotePrice;
	private JRadioButton buyOrSellBuy,buyOrSellSell;
	private JButton ensure;
	private JButton cancel;


	public QuotePriceListener(JTextField textAccountId,JTextField textStockId,JTextField textQuotePrice,JTextField textAmount,
			JRadioButton buyOrSellBuy, JRadioButton buyOrSellSell,String stockCode, JButton ensure, JButton cancel) {
		this.textAccountId = textAccountId;
		this.textStockId = textStockId;
		this.textQuotePrice = textQuotePrice;
		this.textAmount = textAmount;
		this.buyOrSellSell = buyOrSellSell;
		this.buyOrSellBuy = buyOrSellBuy;
		this.stockCode = stockCode;
		this.ensure = ensure;
		this.cancel = cancel;
	}
	
	   private String paramStr(){
	    	 String accId=textAccountId.getText();
	         String stId=stockCode;
	         String price=textQuotePrice.getText();
	         String amout=textAmount.getText();
	         Long dateTime=System.currentTimeMillis();
	         String info = "A@"+stId+"@"+accId+"@"+price+"@"+amout+"@"+(buyOrSellBuy.isSelected()==true?1:2)+"@"+dateTime.toString();
	         
	         return info;
	    }


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == ensure) {
			 try {  
	          
	             Socket socket = new Socket("192.168.5.37",8888);  
	   
	             try {  
	               
	            	 InputStreamReader isr=new InputStreamReader(socket.getInputStream());
	                 BufferedReader br=new BufferedReader(isr);
	     
	                 DataOutputStream out = new DataOutputStream(socket.getOutputStream());  

	                out.write(paramStr().getBytes());  
	                
//	                String readline = "";
//	                readline = br.readLine();
//	                while(!readline.equals("")){
//	                    result.append(readline);
//	                }
	                
	                out.close();
	                isr.close();
	                br.close();
	             } finally {  
	                 socket.close();  
	             }  
	         } catch (IOException xe) {  
	             xe.printStackTrace();  
	         }  
		} else if (e.getSource() == cancel) {
			QuotePriceJDialog.instance().setVisible(false);
		}
		
	}
}
