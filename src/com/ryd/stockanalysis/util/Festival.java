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

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;

/**
 * <p>标题:判断是否为工作日</p>
 * <p>描述:判断是否为工作日</p>
 * 包名：com.ryd.stockanalysis.util
 * 创建人：songby
 * 创建时间：2016/4/5 10:25
 */

public class Festival {

    public static Festival instance;

    private final String FILE = "d:\\festival.xlsx";

    private List<Date> festival = new ArrayList<Date>();// 节假日
    private List<Date> workDay = new ArrayList<Date>();// 工作日

    public static Festival getInstance() {
        if (instance == null) {
            instance = new Festival();
        }
        return instance;
    }

    public Festival() {
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dt = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;

    }

    public String getDate(Date date) {
        String dt = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dt = df.format(date);
        return dt;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Date date=new Date();//取时间

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        System.out.println(dateString);

        Festival f = Festival.getInstance();
        //当前日期
        Date dt = f.getDate(dateString);
        //判断当前日期是不是工作日
        boolean isWorkDay = f.isWorkDay(dt);

        System.out.println(isWorkDay);
    }
}

