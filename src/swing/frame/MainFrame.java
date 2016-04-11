package swing.frame;

import javafx.stage.Screen;
import swing.ClientConstants;
import swing.bean.*;
import swing.listener.StockSearchListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame implements Runnable {

	private static MainFrame mainFrame;

	public static MainFrame instance() {
		if (mainFrame == null)
			mainFrame = new MainFrame();
		return mainFrame;
	}

	GridBagLayout g = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	public MainFrame() {
		super("个人信息");
		addComponent();
       
	}
	
	JLabel accountLab, totalMoneyLab, useMoneyLab;
	JLabel account, totalMoney, useMoney;

	JPanel topPanel,middlePanel,bottomPanel;
	
	JTable table,table2;

	JTextField stCode;
	JButton search;
	
	public void createTopPanel() {
		topPanel = new JPanel();
		topPanel.add(Box.createVerticalStrut(10));
		
		accountLab = new JLabel("帐号");
		topPanel.add(accountLab);
		panelAdd(topPanel,g,c,accountLab,0,0,1,1);
		account = new JLabel("A");
		topPanel.add(account);
		panelAdd(topPanel,g,c,account,1,0,1,1);
		
		totalMoneyLab = new JLabel("总资产");
		topPanel.add(totalMoneyLab);
		panelAdd(topPanel,g,c,totalMoneyLab,2,0,1,1);
		totalMoney = new JLabel("26.6");
		topPanel.add(totalMoney);
		panelAdd(topPanel, g, c, totalMoney, 3, 0, 1, 1);
		
		useMoneyLab = new JLabel("可用金额");
		topPanel.add(useMoneyLab);
		panelAdd(topPanel, g, c, useMoneyLab, 4, 0, 1, 1);
		useMoney = new JLabel("26.6");
		topPanel.add(useMoney);
		panelAdd(topPanel, g, c, useMoney, 5, 0, 1, 1);
	}
	
	public void createMiddlePanel() {
    
        middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

		JPanel panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createTitledBorder("持仓"));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

		table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel1.add(scrollPane);

        middlePanel.add(panel1);

		JPanel panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createTitledBorder("股票行情"));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

		JPanel panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));

		JLabel stCodeLab = new JLabel("股票代码:");
		panel3.add(stCodeLab);
		stCode = new JTextField();
		stCode.setMaximumSize(new Dimension(200, 30));
		panel3.add(stCode);
		search = new JButton("查询");
		StockSearchListener searchListener = new StockSearchListener(stCode, search);
		search.addActionListener(searchListener);
		panel3.add(search);
		panel3.add(Box.createHorizontalStrut(510));

		panel2.add(panel3);

		table2 = new JTable();
		JScrollPane scrollPane2 = new JScrollPane(table2);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel2.add(scrollPane2);

		middlePanel.add(panel2);

	}

	 public void createBottomPanel() {
	        
	        JButton quoteButton = new JButton("报价");
	        quoteButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	                int selectedRow = table2.getSelectedRow();
	                if(selectedRow!= -1)  
	                {
	                	String stockCode = (String) table2.getValueAt(selectedRow, 0);
	                	String stockName = (String) table2.getValueAt(selectedRow, 1);
	                	QuotePriceJDialog.instance().open(account.getText(),stockCode,stockName);
	                }
	            }
	        });
	        
	        JButton stockInfoButton = new JButton("股票详细");
	        stockInfoButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	                int selectedRow = table2.getSelectedRow();
	                if(selectedRow!= -1)  
	                {
	                	String stockCode = (String) table2.getValueAt(selectedRow, 0);
	                	String stockName = (String) table2.getValueAt(selectedRow, 1);
	                	StockInfoDialog.instance(instance()).open(stockCode);
	                }
	            }
	        });

	        JButton closeButton = new JButton("退出");
	        closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
	        
	        bottomPanel = new JPanel();
	      
	        bottomPanel .setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
	       
	        JPanel buttonPanel = new JPanel();
	   
	        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	       
	        buttonPanel.add(quoteButton);
	        buttonPanel.add(stockInfoButton);
	      
	        buttonPanel.add(Box.createHorizontalGlue ());
	      
	        buttonPanel.add(closeButton);
	      
	        bottomPanel .add(Box.createVerticalStrut(10));
	        
	        bottomPanel .add(buttonPanel);
	       
	        bottomPanel .add(Box.createVerticalStrut(10));
	    }
	
	public void addComponent() {
	
        createTopPanel();
        createMiddlePanel();
        createBottomPanel();
        
        JPanel panelContainer = new JPanel();

        setSize(880, 650);
		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		add(middlePanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
	}

	public void panelAdd(JPanel panel, GridBagLayout g, GridBagConstraints c, JComponent jc, int x, int y, int gw, int gh) {
		c.gridx = x;
		c.gridy = y;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = gw;
		c.gridheight = gh;
		panel.add(jc, c);
	}

	public void open() {

        String[] columnName = {"股票代码", "股票名称","现价", "持仓" };

		String[] columnName2 = {"股票代码", "股票名称", "现价", "今开", "昨收", "最高", "最低", "总手", "买一","卖一" };

		List<StPosition> stPositionList = ClientConstants.stPositionList;
		List<StStock> stStockList = ClientConstants.stStockList;

		DefaultTableModel tableModel = new DefaultTableModel(positionListToArray(stPositionList), columnName);
		table.setModel(tableModel);

		DefaultTableModel tableModel2 = new DefaultTableModel(stockListToArray(stStockList),columnName2);
		table2.setModel(tableModel2);

		StAccount acc = ClientConstants.stAccount;

		account.setText(acc.getAccountName());
		totalMoney.setText(acc.getTotalMoney()+"");
		useMoney.setText(acc.getUseMoney()+"");
		
		setVisible(true);
		LoginFrame.instance().setVisible(false);
	}

	/**
	 * {"股票代码", "股票名称", "现价", "今开", "昨收", "最高", "最低", "总手", "买一","卖一" };
	 * @param stockList
	 * @return
	 */
	private Object[][] stockListToArray(List<StStock> stockList){
		Object[][] arr = new Object[stockList.size()][10];
		for(int i=0;i<stockList.size();i++){
			StStock st = stockList.get(i);
			arr[i][0] = st.getStockCode();
			arr[i][1] = st.getStockName();
			arr[i][2] = st.getCurrentPrice();
			arr[i][3] = st.getOpenPrice();
			arr[i][4] = st.getBfclosePrice();
			arr[i][5] = st.getMaxPrice();
			arr[i][6] = st.getMinPrice();
			arr[i][7] = st.getTradeAmount();
			arr[i][8] = st.getBuyOnePrice();
			arr[i][9] = st.getSellOnePrice();
		}

		return arr;
	}


	/**
	 * {"股票代码", "股票名称", "现价", "持仓"};
	 * @param stPositionList
	 * @return
	 */
	private Object[][] positionListToArray(List<StPosition> stPositionList){
		Object[][] arr = new Object[stPositionList.size()][4];
		for(int i=0;i<stPositionList.size();i++){
			StPosition stq = stPositionList.get(i);
			StStock stock = ClientConstants.stStockMap.get(stq.getStockId());

			arr[i][0] = stock.getStockCode();
			arr[i][1] = stock.getStockName();
			arr[i][2] = stock.getCurrentPrice();
			arr[i][3] = stq.getAmount();
		}

		return arr;
	}

	@Override
	public void run() {
		instance();
	}
}
