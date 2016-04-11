package swing;

import swing.bean.StAccount;
import swing.bean.StPosition;
import swing.bean.StStock;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 客户端全局常量/变量表
 * Created by Administrator on 2016/4/11.
 */
public class ClientConstants {
    // 股票信息
    public static List<StStock> stStockList = null;
    // 账户信息
    public static StAccount stAccount = null;
    // 仓位信息
    public static List<StPosition> stPositionList = null;

    static {
        stStockList = new ArrayList<StStock>();
        stAccount = new StAccount();
        stPositionList = new ArrayList<StPosition>();
    }
}
