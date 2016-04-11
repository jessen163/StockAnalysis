package swing.frame;

import javafx.stage.Screen;
import swing.ClientConstants;

import com.ryd.stockanalysis.bean.*;
import swing.common.ListToArray;
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

	public static String[] columnName = {"股票代码", "股票名称","现价", "持仓" };

	public static String[] columnName2 = {"股票代码", "股票名称", "现价", "今开", "昨收", "最高", "最低", "总手", "买一","卖一" };


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
		panelAdd(topPanel, g, c, account, 1, 0, 1, 1);
		
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

		panel3.add(search);
		panel3.add(Box.createHorizontalStrut(510));

		panel2.add(panel3);

		table2 = new JTable();
		JScrollPane scrollPane2 = new JScrollPane(table2);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		StockSearchListener searchListener = new StockSearchListener(stCode, table2, search);
		search.addActionListener(searchListener);

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

		List<StPosition> stPositionList = ClientConstants.stPositionList;
		List<StStock> stStockList = ClientConstants.stStockList;

		DefaultTableModel tableModel = new DefaultTableModel(ListToArray.positionListToArray(stPositionList), columnName);
		table.setModel(tableModel);

		DefaultTableModel tableModel2 = new DefaultTableModel(ListToArray.stockListToArray(stStockList),columnName2);
		table2.setModel(tableModel2);

		StAccount acc = ClientConstants.stAccount;

		account.setText(acc.getAccountName());
		totalMoney.setText(acc.getTotalMoney()+"");
		useMoney.setText(acc.getUseMoney()+"");
		
		setVisible(true);
		LoginFrame.instance().setVisible(false);
	}


	@Override
	public void run() {
		instance();
	}
}
