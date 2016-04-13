package com.ryd.stockanalysis.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>标题:日期表</p>
 * <p>描述:日期表</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/4/13 11:31
 */
public class DateSchedule implements Serializable {

    private static final long serialVersionUID = -4604907279222174696L;

    private String id;
    private Date date;
    private Integer type;//1.节假日 2.特殊工作日

    public DateSchedule() {
    }

    public DateSchedule(String id, Date date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
