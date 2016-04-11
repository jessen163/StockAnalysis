package com.ryd.stockanalysis.protocol;

import java.io.Serializable;

/**
 * 消息对象
 * Created by Administrator on 2016/4/11.
 */
public class NettyMessage implements Serializable {
    private static final long serialVersionUID = -1600102115204109016L;
    // 消息类型
    private Integer msgType;
    // 消息实体
    private Object msgObj;

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Object getMsgObj() {
        return msgObj;
    }

    public void setMsgObj(Object msgObj) {
        this.msgObj = msgObj;
    }
}
