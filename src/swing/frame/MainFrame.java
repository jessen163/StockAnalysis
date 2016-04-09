package swing.frame;

import com.ryd.stockanalysis.bean.StAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	JTable table;
	
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
		panelAdd(topPanel,g,c,totalMoney,3,0,1,1);
		
		useMoneyLab = new JLabel("可用金额");
		topPanel.add(useMoneyLab);
		panelAdd(topPanel,g,c,useMoneyLab,4,0,1,1);
		useMoney = new JLabel("26.6");
		topPanel.add(useMoney);
		panelAdd(topPanel,g,c,useMoney,5,0,1,1);
	}
	
	public void createMiddlePanel() {
    
        middlePanel = new JPanel();
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
       
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        
        middlePanel.add(Box.createVerticalStrut(10));
      
        middlePanel.add(scrollPane);
     
        middlePanel.add(Box.createVerticalStrut(10));
	   
	}
	
	 public void createBottomPanel() {
	        
	        JButton quoteButton = new JButton("报价");
	        quoteButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	                int selectedRow = table.getSelectedRow();
	                if(selectedRow!= -1)  
	                {
	                	String stockCode = (String) table.getValueAt(selectedRow, 0);
	                	String stockName = (String) table.getValueAt(selectedRow, 1);
	                	QuotePriceJDialog.instance().open(account.getText(),stockCode,stockName);
	                }
	            }
	        });
	        
	        JButton stockInfoButton = new JButton("股票详细");
	        stockInfoButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	                int selectedRow = table.getSelectedRow();
	                if(selectedRow!= -1)  
	                {
	                	String stockCode = (String) table.getValueAt(selectedRow, 0);
	                	String stockName = (String) table.getValueAt(selectedRow, 1);
	                	StockInfoFrame.instance().open(stockCode, stockName);
	                }
	            }
	        });
	      
	        JButton closeButton = new JButton("退出");
	        closeButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	            	 System.exit(0);
	            }
	        });
	        
	        bottomPanel = new JPanel();
	      
	        bottomPanel .setLayout(new BoxLayout(bottomPanel,BoxLayout.Y_AXIS ));
	       
	        JPanel buttonPanel = new JPanel();
	   
	        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS ));
	       
	        buttonPanel.add(quoteButton);
	        buttonPanel.add(stockInfoButton);
	      
	        buttonPanel.add(Box.createHorizontalGlue ());
	      
	        buttonPanel.add(closeButton);
	      
	        bottomPanel .add(Box.createVerticalStrut (10));
	        
	        bottomPanel .add(buttonPanel);
	       
	        bottomPanel .add(Box.createVerticalStrut (10));
	    }
	
	public void addComponent() {
	
        createTopPanel ();
        createMiddlePanel ();
 
        createBottomPanel ();
        
        JPanel panelContainer = new JPanel();

        setSize(600,350);
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
		c.anchor = GridBagConstraints.BOTH;
		c.gridwidth = gw;
		c.gridheight = gh;
		panel.add(jc,c);
	}

	public void open(StAccount acc) {

        String[] columnName = {"股票代码", "股票名称", "买入价格", "持仓", "现价", "买入时间" };
        String[][] rowData = { { "1", "中国平安", "26.1", "10000", "27.1" , "2016/04/08 11:20:22" },
                               { "2", "广发证券", "28.1", "10000", "25.1" , "2016/04/05 11:20:22"},
        };
		DefaultTableModel tableModel = new DefaultTableModel(rowData, columnName);
		table.setModel(tableModel);
		
//		account.setText(acc.getAccountName());
//		totalMoney.setText(acc.getTotalMoney()+"");
//		useMoney.setText(acc.getUseMoney()+"");
		
		setVisible(true);
		LoginFrame.instance().setVisible(false);
	}

	@Override
	public void run() {
		instance();
	}
}
