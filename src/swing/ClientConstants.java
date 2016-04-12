package swing;

import com.ryd.stockanalysis.bean.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 *
 * 客户端全局常量/变量表
 * Created by Administrator on 2016/4/11.
 */
public class ClientConstants {
    // 股票信息
    public static List<StStock> stStockList = null;
    // 股票信息
    public static Map<String, StStock> stStockMap = new HashMap<String, StStock>();
    // 账户信息
    public static StAccount stAccount = null;
    // 仓位信息
    public static List<StPosition> stPositionList = null;
    // 仓位信息
    public static Map<String, StPosition> stPositionMap = new HashMap<String, StPosition>();

    // 仓位报价信息
    public static List<StQuote> stQuoteList = null;
    // 仓位报价信息
    public static Map<String, StQuote> stQuoteMap = new HashMap<String, StQuote>();

    //1、登陆 2、股票行情 3、我的报价信息 4、报价 5、撤单 6、持仓信息
    public static final Integer CLIENT_LOGIN = 1;
    public static final Integer STSTOCK_LIST = 2;
    public static final Integer STQUOTE_PRICE_LIST = 3;
    public static final Integer STQUOTE_PRICE = 4;
    public static final Integer STQUOTE_RECALL = 5;
    public static final Integer STSTOCK_POSITION = 6;


    public static void stockListToMap() {
        if (CollectionUtils.isNotEmpty(stStockList)) {
            stStockMap.clear();
            for (StStock st : stStockList) {
                stStockMap.put(st.getStockId(), st);
            }
        }
    }

    public static void positionListToMap(){
        if(CollectionUtils.isNotEmpty(stPositionList)) {
            stPositionMap.clear();
            for (StPosition sp : stPositionList) {
                stPositionMap.put(sp.getStockId(),sp);
            }
        }
    }


    public static void quoteListToMap(){
        if(CollectionUtils.isNotEmpty(stQuoteList)) {
            stQuoteMap.clear();
            for (StQuote sq : stQuoteList) {
                stQuoteMap.put(sq.getQuoteId(),sq);
            }
        }
    }
}
