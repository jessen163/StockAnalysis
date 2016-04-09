package swing;

import javax.swing.*;


import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuoteClient extends JFrame implements ActionListener
{

    GridBagLayout g=new GridBagLayout();
    GridBagConstraints c=new GridBagConstraints();

    QuoteClient(String str)
    {
        super(str);
        setSize(350,430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(g);
        //调用方法
        addComponent();
        submit.addActionListener(this);
        setVisible(true);
        setLocationRelativeTo(null);//设居中显示;
    }

    DataOutputStream  out;

    JLabel accountId,stockId,amount,quotePrice;
    JLabel buyOrSell;
    JTextField textAccountId,textStockId,textAmount,textQuotePrice;
    JRadioButton buyOrSellBuy,buyOrSellSell;
    ButtonGroup group;
    JButton submit;
    JTextArea result;

    //在这个方法中将会添加所有的组件;
    public void addComponent()
    {
        //用户帐户
        accountId=new JLabel("用户帐户：");
        add(g,c,accountId,0,0,1,1);

        //帐户输入框
        textAccountId=new JTextField(10);
        add(g,c,textAccountId,1,0,2,1);


        //用户名
        stockId=new JLabel("股票编码：");
        add(g,c,stockId,0,1,1,1);

        //股票名输入框
        textStockId=new JTextField(10);
        add(g,c,textStockId,1,1,2,1);

        //价格：
        quotePrice=new JLabel("价格：");
        add(g,c,quotePrice,0,2,1,1);

        //价格输入框
        textQuotePrice=new JTextField(10);
        add(g,c,textQuotePrice,1,2,2,1);

        //数量：
        amount=new JLabel("数量：");
        add(g,c,amount,0,3,1,1);

        //数量输入框
        textAmount=new JTextField(10);
        add(g,c,textAmount,1,3,2,1);

        //买卖
        buyOrSell=new JLabel("买或卖:");
        add(g,c,buyOrSell,0,4,1,1);

        //买卖单选框
        buyOrSellBuy=new JRadioButton("买");
        add(g,c,buyOrSellBuy,1,4,1,1);
        buyOrSellBuy.setSelected(true);

        buyOrSellSell=new JRadioButton("卖");
        add(g,c,buyOrSellSell,2,4,1,1);

        group=new ButtonGroup();
        group.add(buyOrSellBuy);
        group.add(buyOrSellSell);

        //submit按钮
        submit=new JButton("提交");

        c.insets=new Insets(7,0,4,0);
        add(g,c,submit,1,5,1,1);

        result=new JTextArea(10,22);
        add(g, c, result, 0, 6, 3, 4);
    }

    public void add(GridBagLayout g,GridBagConstraints c,JComponent jc,int x ,int y,int gw,int gh)
    {
        c.gridx=x;
        c.gridy=y;
        c.anchor=GridBagConstraints.WEST;
        c.gridwidth=gw;
        c.gridheight=gh;
        g.setConstraints(jc,c);
        add(jc);
    }


    public static void main(String args[])
    {
        new QuoteClient("买入或卖出报价");
    }



    @Override
    public void actionPerformed(ActionEvent arg0)
    {

        Socket client = null;
        BufferedReader is = null;
        Writer writer = null;
        try {
            client = new Socket("127.0.0.1", 8888);

            writer = new OutputStreamWriter(client.getOutputStream());
            writer.write(paramStr());
            writer.flush();

//            is = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            StringBuffer sb = new StringBuffer();
//            String temp;
//            int index;
//            while ((temp = is.readLine()) != null) {
//                sb.append(temp);
//            }
//            System.out.println(sb.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String paramStr(){
        String accId=textAccountId.getText();
        String stId=textStockId.getText();
        String price=textQuotePrice.getText();
        String amout=textAmount.getText();
        Long dateTime=System.currentTimeMillis();
        String info = "A@"+stId+"@"+accId+"@"+price+"@"+amout+"@"+(buyOrSellBuy.isSelected()==true?1:2)+"@"+dateTime.toString();

        return info;
    }

}
