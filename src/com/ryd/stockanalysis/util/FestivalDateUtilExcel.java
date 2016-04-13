package com.ryd.stockanalysis.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ryd.stockanalysis.common.Constant;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;

/**
 * <p>标题:判断是否为工作日</p>
 * <p>描述:判断是否为工作日</p>
 * 包名：com.ryd.stockanalysis.util
 * 创建人：songby
 * 创建时间：2016/4/5 10:25
 */

public class FestivalDateUtilExcel {

    public static FestivalDateUtilExcel instance;

    public static String DATE_FORMAT = "yyyy-MM-dd";

    private final String FILE = "d:\\festival.xlsx";

    private List<Date> festival = new ArrayList<Date>();// 节假日
    private List<Date> workDay = new ArrayList<Date>();// 工作日

    public static FestivalDateUtilExcel getInstance() {
        if (instance == null) {
            instance = new FestivalDateUtilExcel();
        }
        return instance;
    }

    public FestivalDateUtilExcel(){
        readExcel();
    }

    public void readExcel() {
        File excel = this.getExcel();
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(excel);
            XSSFWorkbook hssfworkbook = new XSSFWorkbook(fin);
            XSSFSheet sheet = hssfworkbook.getSheetAt(0);
            int last = sheet.getLastRowNum();
            int index = 1;
            Date dt = null;
            //row 0 标识节假日、工作日
            while (index <= last) {
                XSSFRow row = sheet.getRow(index);

                /* 第一列 读取法定节假日 */
                XSSFCell cell = row.getCell((short) 0);
                if (cell != null) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        dt = DateUtil.getJavaDate(cell.getNumericCellValue());
                        if (dt != null && dt.getTime() > 0) {
                            this.festival.add(dt);
                        }
                    }
                }

                /* 第二列 读取特殊工作日 */
                cell = row.getCell((short) 1);
                if (cell != null) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        dt = DateUtil.getJavaDate(cell.getNumericCellValue());
                        if (dt != null && dt.getTime() > 0) {
                            this.workDay.add(dt);
                        }
                    }
                }
                index++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fin!=null){
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File getExcel() {
        File excel = null;
        try {
            excel = new File(FILE);
            return excel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excel;
    }

    /**
     * 从EXCEL文件中读取节假日
     *
     * @return
     */
    public List getFestival() {
        return this.festival;
    }

    public List getSpecialWorkDay() {
        return this.workDay;
    }

    /**
     * 判断一个日期是否日节假日 法定节假日只判断月份和天，不判断年
     *
     * @param date
     * @return
     */
    public boolean isFestival(Date date) {
        boolean festival = false;
        Calendar fcal = Calendar.getInstance();
        Calendar dcal = Calendar.getInstance();
        dcal.setTime(date);
        List<Date> list = this.getFestival();
        for (Date dt : list) {
            fcal.setTime(dt);

            // 法定节假日判断
            if (fcal.get(Calendar.MONTH) == dcal.get(Calendar.MONTH)
                    && fcal.get(Calendar.DATE) == dcal.get(Calendar.DATE)) {
                festival = true;
            }
        }
        return festival;
    }

    /**
     * 周六周日判断
     *
     * @param date
     * @return
     */
    public boolean isWeekend(Date date) {
        boolean weekend = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            weekend = true;
        }
        return weekend;
    }

    /**
     * 是否是工作日 法定节假日和周末为非工作日
     *
     * @param date
     * @return
     */
    public boolean isWorkDay(Date date) {
        boolean workday = true;
        if (this.isFestival(date) || this.isWeekend(date)) {
            workday = false;
        }

        /* 特殊工作日判断 */
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        for (Date dt : this.workDay) {
            cal2.setTime(dt);
            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                    && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE)) { // 年月日相等为特殊工作日
                workday = true;
            }
        }
        return workday;
    }

    public Date getDate(String str) {
        Date dt = null;
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            dt = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;

    }

    public String getDate(Date date) {
        String dt = null;
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        dt = df.format(date);
        return dt;
    }


    public int dateJudge(){
        //当前时间
        Calendar calNow = Calendar.getInstance();
        //如果当前时间是工作日
        if(isWorkDay(calNow.getTime())) {
            long tnow = calNow.getTime().getTime();
            //开盘时间
            long t9 = getDateHour(Constant.STOCK_OPEN_TIME).getTime();
            //休盘时间开始
            long t11 = getDateHour(Constant.STOCK_REST_TIME_START).getTime();
            //休盘时间结束
            long t13 = getDateHour(Constant.STOCK_REST_TIME_END).getTime();
            //收盘时间
            long t15 = getDateHour(Constant.STOCK_CLOSE_TIME).getTime();

            //如果当前时间在上午9：30~11：30之间，或者下午1：00~3：00之间，可以交易和报价
            if ((t9 < tnow && tnow < t11) || (t13 < tnow && tnow < t15)) {
                return Constant.STQUOTE_TRADE_TIMECOMPARE_1;
            } else if ((t11 < tnow && tnow < t13)) {//如果当前时间在11：30~13：00之间只允许报价
                return Constant.STQUOTE_TRADE_TIMECOMPARE_2;
            }else{//之外的时间，不允许报价，不允许交易
                return Constant.STQUOTE_TRADE_TIMECOMPARE_3;
            }
        }
        return Constant.STQUOTE_TRADE_TIMECOMPARE_3;
    }

    /**
     * 设置小时分钟
     * @param hourStr
     * @return
     */
    private Date getDateHour(String hourStr){
        String[] strarr = hourStr.split(":");
        Calendar cal = Calendar.getInstance();
        if(strarr.length>0){
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),Integer.parseInt(strarr[0]),Integer.parseInt(strarr[1]),0);
        }
        return cal.getTime();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Date date=new Date();//取时间

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        System.out.println(dateString);

        FestivalDateUtilExcel f = FestivalDateUtilExcel.getInstance();
        //当前日期
        Date dt = f.getDate(dateString);
        //判断当前日期是不是工作日
        boolean isWorkDay = f.isWorkDay(dt);

        System.out.println(isWorkDay);

        //判断当前时间状态
        System.out.println(f.dateJudge());
    }
}

