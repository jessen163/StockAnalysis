package swing.frame;

import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import swing.ClientConstants;
import swing.common.ListToArray;
import swing.listener.QuoteCancelListener;
import swing.listener.QuotePriceListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class QuoteListDialog extends JDialog {

	public static String[] columnName = {"股票代码", "股票名称","报价", "申报数量", "类型", "冻结资金", "报价时间","quoteId"};

	private static QuoteListDialog quoteListDialog;

	JTable table;

	public static QuoteListDialog instance() {
		if (quoteListDialog == null)
			quoteListDialog = new QuoteListDialog();
		return quoteListDialog;
	}

	public QuoteListDialog() {
		super(MainFrame.instance(), "报价列表", true);
		setLayout(new BorderLayout());
		setSize(600, 450);
		setLocationRelativeTo(null);

		JPanel panel1 = new JPanel();
//		panel1.setBorder(BorderFactory.createTitledBorder("当前委托"));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel1.add(scrollPane);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));


		JButton canelQuoteBtn = new JButton("撤单");
		QuoteCancelListener quoteCancelListener = new QuoteCancelListener(table);
		canelQuoteBtn.addActionListener(quoteCancelListener);

		JButton jcloseButton = new JButton("退出");
		jcloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		buttonPanel.add(canelQuoteBtn);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(jcloseButton);

        add(panel1, BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);

	}

	public void open() {

		List<StQuote> stQuoteList = ClientConstants.stQuoteList;
		table.removeAll();
		DefaultTableModel tableModel = new DefaultTableModel(ListToArray.quoteListToArray(stQuoteList), columnName);
		table.setModel(tableModel);
		hideColumn(table,7);

		setVisible(true);
	}

	/**
	 * 隐藏表格中的某一列
	 * @param table  表格
	 * @param index  要隐藏的列 的索引
	 */
	protected void hideColumn(JTable table,int index){
		TableColumn tc= table.getColumnModel().getColumn(index);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setMinWidth(0);
		tc.setWidth(0);
		table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
		table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
	}
}