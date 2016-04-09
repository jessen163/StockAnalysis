package swing.frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockInfoFrame  extends JFrame implements Runnable {

	private static StockInfoFrame stockInfoFrame;

	public static StockInfoFrame instance() {
		if (stockInfoFrame == null)
			stockInfoFrame = new StockInfoFrame();
		return stockInfoFrame;
	}


	public StockInfoFrame() {
		super("股票信息");
		addComponent();
	}
	
	JLabel currentPriceLab, openPriceLab, closePriceLab,maxPriceLab,minPriceLab,amountLab;
	JLabel stock, currentPrice, openPrice, closePrice,maxPrice,minPrice,amount;
	
	JPanel topPanel,middlePanel,bottomPanel;
	
	GridBagLayout g = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	
	public void createTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(g);
		
		stock = new JLabel("西部证券    ");
		topPanel.add(stock);
		panelAdd(topPanel,g,c,stock,0,0,1,1);
		
		currentPriceLab = new JLabel("现价：");
		topPanel.add(currentPriceLab);
		panelAdd(topPanel,g,c,currentPriceLab,1,0,1,1);
		currentPrice = new JLabel("28");
		topPanel.add(currentPrice);
		panelAdd(topPanel,g,c,currentPrice,2,0,1,1);
		
		amountLab = new JLabel("总手：");
		topPanel.add(amountLab);
		panelAdd(topPanel,g,c,amountLab,3,0,1,1);
		amount = new JLabel("100");
		topPanel.add(amount);
		panelAdd(topPanel,g,c,amount,4,0,1,1);
		
		openPriceLab = new JLabel("今开：");
		topPanel.add(openPriceLab);
		panelAdd(topPanel,g,c,openPriceLab,0,1,1,1);
		openPrice = new JLabel("26.6");
		topPanel.add(openPrice);
		panelAdd(topPanel,g,c,openPrice,1,1,1,1);
		
		closePriceLab = new JLabel("昨收：");
		topPanel.add(closePriceLab);
		panelAdd(topPanel,g,c,closePriceLab,2,1,1,1);
		closePrice = new JLabel("26.6");
		topPanel.add(closePrice);
		panelAdd(topPanel,g,c,closePrice,3,1,1,1);
		
		maxPriceLab = new JLabel("最高：");
		topPanel.add(maxPriceLab);
		panelAdd(topPanel,g,c,maxPriceLab,4,1,1,1);
		maxPrice = new JLabel("26.6");
		topPanel.add(maxPrice);
		panelAdd(topPanel,g,c,maxPrice,5,1,1,1);
		
		minPriceLab = new JLabel("最低：");
		topPanel.add(minPriceLab);
		panelAdd(topPanel,g,c,minPriceLab,6,1,1,1);
		minPrice = new JLabel("26.6");
		topPanel.add(minPrice);
		panelAdd(topPanel,g,c,minPrice,7,1,1,1);
		
		topPanel.add(Box.createVerticalStrut (10)); 
	}
	
	public void createMiddlePanel() {
    
        middlePanel = new JPanel();
    
        middlePanel .setLayout(new BoxLayout(middlePanel,BoxLayout.X_AXIS ));
        JLabel sourceLabel = new JLabel("买卖：");
        sourceLabel.setAlignmentY(Component.TOP_ALIGNMENT );
        sourceLabel.setBorder(BorderFactory.createEmptyBorder (4, 5, 0, 5));
     
        DefaultListModel listModel = new DefaultListModel();
        listModel.addElement("100 米 100 米");
        listModel.addElement("200 米");
        listModel.addElement("400 米");
        listModel.addElement("跳远");
        listModel.addElement("跳高");
        listModel.addElement("铅球");
        listModel.addElement("400 米");
        listModel.addElement("跳远");
        listModel.addElement("跳高");
        listModel.addElement("铅球");
        JList sourceList = new JList(listModel);
       
        sourceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        sourceList.setVisibleRowCount(10);
      
        JPanel sourceListPanel = new JPanel();
       
        sourceListPanel.setLayout(new BoxLayout(sourceListPanel, BoxLayout.X_AXIS ));
       
        sourceListPanel.add(sourceLabel);
       
        sourceListPanel.add(sourceList);
        
        sourceListPanel.setAlignmentY(Component.TOP_ALIGNMENT );
        sourceListPanel.setBorder(BorderFactory.createEmptyBorder (0, 0, 0, 30));
       
        middlePanel .add(sourceListPanel);
	}
	
	 public void createBottomPanel() {
	        
	       
	        JButton closeButton = new JButton("退出");
	        closeButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
                	setVisible(false);
            		MainFrame.instance().setVisible(true);
	            }
	        });
	        
	        bottomPanel = new JPanel();
	      
	        bottomPanel .setLayout(new BoxLayout(bottomPanel,BoxLayout.Y_AXIS ));
	       
	        JPanel buttonPanel = new JPanel();
	   
	        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS ));
	       

	      
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

        setSize(500,350);
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
		c.fill = GridBagConstraints.BOTH ;
		panel.add(jc,c);
	}

	public void open(String stockCode,String stockName) {
		setVisible(true);
		MainFrame.instance().setVisible(false);
	}

	@Override
	public void run() {
		instance();
	}
}
