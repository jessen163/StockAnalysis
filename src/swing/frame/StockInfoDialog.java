package swing.frame;


import com.ryd.stockanalysis.bean.*;
import swing.ClientConstants;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockInfoDialog extends JDialog {

	private static StockInfoDialog stockInfoDialog;

	public static StockInfoDialog instance(JFrame object) {
		if (stockInfoDialog == null)
			stockInfoDialog = new StockInfoDialog(object);
		return stockInfoDialog;
	}


	public StockInfoDialog(JFrame object) {
		super(object, "股票信息", true);
		addComponent();
	}
	
	JLabel currentPriceLab, openPriceLab, closePriceLab,maxPriceLab,minPriceLab,amountLab;
	JLabel stock, currentPrice, openPrice, closePrice,maxPrice,minPrice,amount;

	JLabel buyOnePrice, buyTwoPrice, buyThreePrice, buyFourPrice, buyFivePrice;
	JLabel buyOneAmount, buyTwoAmount, buyThreeAmount, buyFourAmount, buyFiveAmount;

	JLabel sellOnePrice, sellTwoPrice, sellThreePrice, sellFourPrice, sellFivePrice;
	JLabel sellOneAmount, sellTwoAmount, sellThreeAmount, sellFourAmount, sellFiveAmount;

	JPanel topPanel,westPanel,middlePanel,bottomPanel;
	
	GridBagLayout g = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	
	public void createTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(g);

		stock = new JLabel("西部证券    ");
		topPanel.add(stock);
		panelAdd(topPanel, g, c, stock, 0, 0, 1, 1);

	}

	public void createWestPanel(){
		westPanel = new JPanel();
		westPanel.setLayout(g);

		currentPriceLab = new JLabel("现价:");
		westPanel.add(currentPriceLab);
		panelAdd(westPanel, g, c, currentPriceLab, 1, 1, 1, 1);
		currentPrice = new JLabel("28");
		westPanel.add(currentPrice);
		panelAdd(westPanel, g, c, currentPrice, 2, 1, 1, 1);
		
		amountLab = new JLabel("总手:");
		westPanel.add(amountLab);
		panelAdd(westPanel, g, c, amountLab, 3, 1, 1, 1);
		amount = new JLabel("100");
		westPanel.add(amount);
		panelAdd(westPanel, g, c, amount, 4, 1, 1, 1);
		
		openPriceLab = new JLabel("今开:");
		westPanel.add(openPriceLab);
		panelAdd(westPanel, g, c, openPriceLab, 1, 2, 1, 1);
		openPrice = new JLabel("26.6");
		westPanel.add(openPrice);
		panelAdd(westPanel, g, c, openPrice, 2, 2, 1, 1);
		
		closePriceLab = new JLabel("昨收:");
		westPanel.add(closePriceLab);
		panelAdd(westPanel, g, c, closePriceLab, 3, 2, 1, 1);
		closePrice = new JLabel("26.6");
		westPanel.add(closePrice);
		panelAdd(westPanel, g, c, closePrice, 4, 2, 1, 1);
		
		maxPriceLab = new JLabel("最高:");
		westPanel.add(maxPriceLab);
		panelAdd(westPanel, g, c, maxPriceLab, 1, 3, 1, 1);
		maxPrice = new JLabel("26.6");
		westPanel.add(maxPrice);
		panelAdd(westPanel, g, c, maxPrice, 2, 3, 1, 1);
		
		minPriceLab = new JLabel("最低:");
		westPanel.add(minPriceLab);
		panelAdd(westPanel, g, c, minPriceLab, 3, 3, 1, 1);
		minPrice = new JLabel("26.6");
		westPanel.add(minPrice);
		panelAdd(westPanel, g, c, minPrice, 4, 3, 1, 1);

		westPanel.add(Box.createHorizontalStrut(10));
	}
	
	public void createMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(g);

		JLabel sellFiveLab = new JLabel("卖五: ");
		middlePanel.add(sellFiveLab);
		panelAdd(middlePanel, g, c, sellFiveLab, 0, 0, 1, 1);
		sellFivePrice  = new JLabel("28 ");
		middlePanel.add(sellFivePrice);
		panelAdd(middlePanel,g,c,sellFivePrice,1,0,1,1);
		sellFiveAmount  = new JLabel("28 ");
		middlePanel.add(sellFiveAmount);
		panelAdd(middlePanel, g, c, sellFiveAmount, 3, 0, 1, 1);

		JLabel sellFourLab = new JLabel("卖四: ");
		middlePanel.add(sellFourLab);
		panelAdd(middlePanel, g, c, sellFourLab, 0, 1, 1, 1);
		sellFourPrice  = new JLabel("28 ");
		middlePanel.add(sellFourPrice);
		panelAdd(middlePanel,g,c,sellFourPrice,1,1,1,1);
		sellFourAmount  = new JLabel("28 ");
		middlePanel.add(sellFourAmount);
		panelAdd(middlePanel,g,c,sellFourAmount,3,1,1,1);

		JLabel sellThreeLab = new JLabel("卖三: ");
		middlePanel.add(sellThreeLab);
		panelAdd(middlePanel, g, c, sellThreeLab, 0, 2, 1, 1);
		sellThreePrice  = new JLabel("28 ");
		middlePanel.add(sellThreePrice);
		panelAdd(middlePanel,g,c,sellThreePrice,1,2,1,1);
		sellThreeAmount  = new JLabel("28 ");
		middlePanel.add(sellThreeAmount);
		panelAdd(middlePanel,g,c,sellThreeAmount,3,2,1,1);

		JLabel sellTwoLab = new JLabel("卖二: ");
		middlePanel.add(sellTwoLab);
		panelAdd(middlePanel, g, c, sellTwoLab, 0, 3, 1, 1);
		sellTwoPrice  = new JLabel("28 ");
		middlePanel.add(sellTwoPrice);
		panelAdd(middlePanel,g,c,sellTwoPrice,1,3,1,1);
		sellTwoAmount  = new JLabel("28 ");
		middlePanel.add(sellTwoAmount);
		panelAdd(middlePanel,g,c,sellTwoAmount,3,3,1,1);

		JLabel sellOneLab = new JLabel("卖一: ");
		middlePanel.add(sellOneLab);
		panelAdd(middlePanel, g, c, sellOneLab, 0, 4, 1, 1);
		sellOnePrice  = new JLabel("28 ");
		middlePanel.add(sellOnePrice);
		panelAdd(middlePanel,g,c,sellOnePrice,1,4,1,1);
		sellOneAmount  = new JLabel("28 ");
		middlePanel.add(sellOneAmount);
		panelAdd(middlePanel,g,c,sellOneAmount,3,4,1,1);

		JLabel buyOneLab = new JLabel("买一: ");
		middlePanel.add(buyOneLab);
		panelAdd(middlePanel, g, c, buyOneLab, 0, 5, 1, 1);
		buyOnePrice  = new JLabel("28 ");
		middlePanel.add(buyOnePrice);
		panelAdd(middlePanel,g,c,buyOnePrice,1,5,1,1);
		buyOneAmount  = new JLabel("28 ");
		middlePanel.add(buyOneAmount);
		panelAdd(middlePanel,g,c,buyOneAmount,3,5,1,1);

		JLabel buyTwoLab = new JLabel("买二: ");
		middlePanel.add(buyTwoLab);
		panelAdd(middlePanel, g, c, buyTwoLab, 0, 6, 1, 1);
		buyTwoPrice  = new JLabel("28 ");
		middlePanel.add(buyTwoPrice);
		panelAdd(middlePanel,g,c,buyTwoPrice,1,6,1,1);
		buyTwoAmount  = new JLabel("28 ");
		middlePanel.add(buyTwoAmount);
		panelAdd(middlePanel,g,c,buyTwoAmount,3,6,1,1);

		JLabel buyThreeLab = new JLabel("买三: ");
		middlePanel.add(buyThreeLab);
		panelAdd(middlePanel, g, c, buyThreeLab, 0, 7, 1, 1);
		buyThreePrice  = new JLabel("28 ");
		middlePanel.add(buyThreePrice);
		panelAdd(middlePanel,g,c,buyThreePrice,1,7,1,1);
		buyThreeAmount  = new JLabel("28 ");
		middlePanel.add(buyThreeAmount);
		panelAdd(middlePanel,g,c,buyThreeAmount,3,7,1,1);

		JLabel buyFourLab = new JLabel("买四: ");
		middlePanel.add(buyFourLab);
		panelAdd(middlePanel, g, c, buyFourLab, 0, 8, 1, 1);
		buyFourPrice  = new JLabel("28 ");
		middlePanel.add(buyFourPrice);
		panelAdd(middlePanel,g,c,buyFourPrice,1,8,1,1);
		buyFourAmount  = new JLabel("28 ");
		middlePanel.add(buyFourAmount);
		panelAdd(middlePanel,g,c,buyFourAmount,3,8,1,1);

		JLabel buyFiveLab = new JLabel("买五: ");
		middlePanel.add(buyFiveLab);
		panelAdd(middlePanel, g, c, buyFiveLab, 0, 9, 1, 1);
		buyFivePrice  = new JLabel("28 ");
		middlePanel.add(buyFivePrice);
		panelAdd(middlePanel, g, c, buyFivePrice, 1, 9, 1, 1);
		buyFiveAmount  = new JLabel("28 ");
		middlePanel.add(buyFiveAmount);
		panelAdd(middlePanel, g, c, buyFiveAmount, 3, 9, 1, 1);

		middlePanel .add(Box.createHorizontalStrut(10));
	}
	
	 public void createBottomPanel() {
	        
	       
	        JButton closeButton = new JButton("关闭");
	        closeButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
                	setVisible(false);
	            }
	        });
	        
	        bottomPanel = new JPanel();
	      
	        bottomPanel .setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
	       
	        JPanel buttonPanel = new JPanel();
	   
	        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	       

	      
	        buttonPanel.add(Box.createHorizontalGlue());
	      
	        buttonPanel.add(closeButton);
	      
	        bottomPanel .add(Box.createHorizontalStrut(10));
	        
	        bottomPanel .add(buttonPanel);
	       
	        bottomPanel .add(Box.createVerticalStrut (10));
	    }
	
	public void addComponent() {
	
        createTopPanel();
		createWestPanel();
        createMiddlePanel ();
 
        createBottomPanel ();
        
        JPanel panelContainer = new JPanel();

        setSize(500,350);
		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		add(westPanel, BorderLayout.WEST);
		add(middlePanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
	}

	public void panelAdd(JPanel panel, GridBagLayout g, GridBagConstraints c, JComponent jc, int x, int y, int gw, int gh) {
		c.gridx = x;
		c.gridy = y;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = gw;
		c.gridheight = gh;
		c.fill = GridBagConstraints.BOTH ;
		panel.add(jc,c);

		c.insets = new Insets(2, 10, 2, 10);

	}

	public void open(String stockId) {
		StStock st = ClientConstants.stStockMap.get(stockId);
		StAccount account = ClientConstants.stAccount;
		stock.setText(st.getStockName()+"  ");
		currentPrice.setText(String.valueOf(st.getCurrentPrice()));
		openPrice.setText(String.valueOf(st.getOpenPrice()));
		closePrice.setText(String.valueOf(st.getBfclosePrice()));
		maxPrice.setText(String.valueOf(st.getMaxPrice()));
		minPrice.setText(String.valueOf(st.getMinPrice()));
		amount.setText(String.valueOf(st.getTradeAmount()));


//		buyOneAmount.setText(String.valueOf(st.getBuyOneAmount()));
//		buyOnePrice.setText(String.valueOf(st.getBuyOnePrice()));
//		buyTwoAmount.setText(String.valueOf(st.getBuyTwoAmount()));
//		buyTwoPrice.setText(String.valueOf(st.getBuyTwoPrice()));
//		buyThreeAmount.setText(String.valueOf(st.getBuyThreeAmount()));
//		buyThreePrice.setText(String.valueOf(st.getBuyThreePrice()));
//		buyFourAmount.setText(String.valueOf(st.getBuyFourAmount()));
//		buyFourPrice.setText(String.valueOf(st.getBuyFourPrice()));
//		buyFiveAmount.setText(String.valueOf(st.getBuyFiveAmount()));
//		buyFivePrice.setText(String.valueOf(st.getBuyFivePrice()));
//
//		sellOneAmount.setText(String.valueOf(st.getSellOneAmount()));
//		sellOnePrice.setText(String.valueOf(st.getSellOnePrice()));
//		sellTwoAmount.setText(String.valueOf(st.getSellTwoAmount()));
//		sellTwoPrice.setText(String.valueOf(st.getSellTwoPrice()));
//		sellThreeAmount.setText(String.valueOf(st.getSellThreeAmount()));
//		sellThreePrice.setText(String.valueOf(st.getSellThreePrice()));
//		sellFourAmount.setText(String.valueOf(st.getSellFourAmount()));
//		sellFourPrice.setText(String.valueOf(st.getSellFourPrice()));
//		sellFiveAmount.setText(String.valueOf(st.getSellFiveAmount()));
//		sellFivePrice.setText(String.valueOf(st.getSellFivePrice()));

		setVisible(true);
	}

}
