package swing.common;

import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import swing.ClientConstants;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：swing.common
 * 创建人：songby
 * 创建时间：2016/4/11 18:02
 */
public class ListToArray {


    /**
     * {"股票代码", "股票名称", "现价", "今开", "昨收", "最高", "最低", "总手", "买一","卖一" };
     * @param stockList
     * @return
     */
    public static Object[][] stockListToArray(List<StStock> stockList){
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
    public static Object[][] positionListToArray(List<StPosition> stPositionList){
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

    /**
     * {"股票代码", "股票名称","报价", "申报数量", "类型", "冻结资金", "报价时间"}
     * @param stQuoteList
     * @return
     */
    public static Object[][] quoteListToArray(List<StQuote> stQuoteList){
        Object[][] arr = new Object[stQuoteList.size()][8];
        SimpleDateFormat format=new SimpleDateFormat("MM/dd HH:MM");
        for(int i=0;i<stQuoteList.size();i++){
            StQuote stq = stQuoteList.get(i);
            StStock stock = ClientConstants.stStockMap.get(stq.getStockId());

            arr[i][0] = stock.getStockCode();
            arr[i][1] = stock.getStockName();
            arr[i][2] = stq.getQuotePrice();
            arr[i][3] = stq.getAmount();
            arr[i][4] = stq.getType()==1?"买":"卖";
            arr[i][5] = stq.getFrozeMoney()==null?0d:stq.getFrozeMoney();
            arr[i][6] = format.format(stq.getDateTime());
            arr[i][7] = stq.getQuoteId();
        }

        return arr;
    }

}
