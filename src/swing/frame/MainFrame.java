package swing.frame;

import com.ryd.stockanalysis.protocol.NettyMessage;
import javafx.stage.Screen;
import swing.ClientConstants;

import com.ryd.stockanalysis.bean.*;
import swing.common.ListToArray;
import swing.listener.QuoteListListener;
import swing.listener.StockSearchListener;
import swing.service.impl.MessageServiceImpl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

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

		//-------------------------------------------------------------1
		JPanel panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createTitledBorder("持仓"));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

		table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel1.add(scrollPane);

//		JPanel panel3 = new JPanel();
//		panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
//
//		JButton positionBtn = new JButton("持仓明细");
//		positionBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int selectedRow = table.getSelectedRow();
//				if (selectedRow != -1) {
//					String stockCode = (String) table.getValueAt(selectedRow, 0);
//					QuotePriceJDialog.instance().open(stockCode);
//				}
//			}
//		});
//
//		panel1.add(positionBtn);

        middlePanel.add(panel1);
        //-------------------------------------------------------------1

		//-------------------------------------------------------------2
		JPanel panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createTitledBorder("股票行情"));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

		JPanel panel4 = new JPanel();
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));

		JLabel stCodeLab = new JLabel("股票代码:");
		panel4.add(stCodeLab);
		stCode = new JTextField();
		stCode.setMaximumSize(new Dimension(200, 30));
		panel4.add(stCode);
		search = new JButton("查询");

		panel4.add(search);

		JButton quoteButton = new JButton("报价");
		quoteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table2.getSelectedRow();
				if (selectedRow != -1) {
					String stockCode = (String) table2.getValueAt(selectedRow, 0);
					QuotePriceJDialog.instance().open(stockCode);
				}else{
					JOptionPane.showMessageDialog(null, "请选择对应股票", "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton stockInfoButton = new JButton("股票详细");
		stockInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table2.getSelectedRow();
				if (selectedRow != -1) {
					String stockCode = (String) table2.getValueAt(selectedRow, 0);
					String stockName = (String) table2.getValueAt(selectedRow, 1);
					StockInfoDialog.instance(instance()).open(stockCode);
				}else{
					JOptionPane.showMessageDialog(null, "请选择对应股票", "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		panel4.add(quoteButton);
		panel4.add(stockInfoButton);

		panel4.add(Box.createHorizontalStrut(410));

		panel2.add(panel4);

		table2 = new JTable();
		JScrollPane scrollPane2 = new JScrollPane(table2);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		StockSearchListener searchListener = new StockSearchListener(stCode, table2, search);
		search.addActionListener(searchListener);

		panel2.add(scrollPane2);

		middlePanel.add(panel2);
		//-------------------------------------------------------------2
	}

	 public void createBottomPanel() {

		 JButton quoteListButton = new JButton("我的报价列表");
		 QuoteListListener quoteListListener = new QuoteListListener();
		 quoteListButton.addActionListener(quoteListListener);

		 JButton refreshButton = new JButton("刷新");
		 refreshButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 dispose();
				 NettyMessage msg = new NettyMessage();
				 msg.setMsgObj(ClientConstants.stAccount);
				 msg.setMsgType(ClientConstants.CLIENT_LOGIN);

				 MessageServiceImpl.sendMessage(msg);
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

		 buttonPanel.add(quoteListButton);
	      
		 buttonPanel.add(Box.createHorizontalGlue ());

		 buttonPanel.add(refreshButton);

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
		LoginFrame.instance().dispose();
	}
}
