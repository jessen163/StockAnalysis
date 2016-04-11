package swing.listener;

import swing.frame.LoginFrame;
import swing.frame.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

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

    public StockSearchListener(JTextField stockCode,
                         JButton search) {
        this.stockCode = stockCode;
        this.search = search;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            if (stockCode.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "请输入股票代码", "提示",
                        JOptionPane.ERROR_MESSAGE);

            }else{

            }
        }
    }
}
