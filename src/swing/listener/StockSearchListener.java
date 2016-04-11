package swing.listener;

import com.ryd.stockanalysis.bean.StStock;
import swing.ClientConstants;
import swing.common.ListToArray;
import swing.frame.LoginFrame;
import swing.frame.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：swing.listener
 * 创建人：songby
 * 创建时间：2016/4/11 15:37
 */
public class StockSearchListener extends MouseAdapter implements ActionListener {
    private JTextField stockCode;
    private JButton search;
    private JTable table;

    public StockSearchListener(JTextField stockCode,JTable table,
                         JButton search) {
        this.stockCode = stockCode;
        this.table = table;
        this.search = search;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            if (stockCode.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "请输入股票代码", "提示",
                        JOptionPane.ERROR_MESSAGE);
            }else{

                StStock sst = ClientConstants.stStockMap.get(stockCode.getText());

                if(sst == null){
                    JOptionPane.showMessageDialog(null, "没有这支股票", "提示",
                            JOptionPane.ERROR_MESSAGE);
                }else{
                    List<StStock> list = new ArrayList<StStock>();
                    list.add(sst);

                    table.setModel(new DefaultTableModel(ListToArray.stockListToArray(list), MainFrame.columnName2));
                }
            }
        }
    }
}
