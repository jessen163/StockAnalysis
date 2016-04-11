package com.ryd.stockanalysis.protocol;

/**
 * 消息对象
 * Created by Administrator on 2016/4/11.
 */
public class NettyMessage {
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
