package com.attendance.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 *
 * @author june
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期路径 即年-月 如2018-08
     */
    public static final String dateMonthString()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, YYYY_MM);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 时间返回年月月的形式
     */
    public static String formattedDate(Date nowDate)
    {
        String originalDateTimeStr=nowDate.toString();
        // 定义输入格式器，匹配原始字符串的格式
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        // 解析原始字符串为 ZonedDateTime
        ZonedDateTime originalDateTime = ZonedDateTime.parse(originalDateTimeStr, inputFormatter);

        // 定义输出格式器，指定所需的输出格式
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日", Locale.CHINA);

        // 格式化日期时间为所需格式
        String formattedDate = originalDateTime.format(outputFormatter);

        // 打印结果
        System.out.println(formattedDate);
        return formattedDate;
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static List<String> getBetweenDate(String startTime, String endTime, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        // 转化成日期类型
        Date startDate = new Date();
        Date endDate = new Date();
        if (!"".equals(startTime) && "".equals(endTime)) {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(startTime);
        } else if (!"".equals(endTime) && "".equals(startTime)) {
            startDate = sdf.parse(endTime);
            endDate = sdf.parse(endTime);
        } else if (!"".equals(endTime) && !"".equals(startTime)) {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        }
        //用Calendar 进行日期比较判断
        Calendar calendar = Calendar.getInstance();
        while (startDate.getTime() <= endDate.getTime()) {
            // 把日期添加到集合
            list.add(sdf.format(startDate));
            // 设置日期
            calendar.setTime(startDate);
            //把日期增加一天
            calendar.add(Calendar.DATE, 1);
            // 获取增加后的日期
            startDate = calendar.getTime();
        }
        return list;
    }

    public static String dateToWeek(Date date) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //时间格式化
    public static Date timeFormat(DateFormat df, String old, String standard) throws Exception{
        if (old.contains("年") || old.contains("月") || old.contains("日") || old.contains("-") || old.contains("/")){
            //转换为-连接
            String[] itemArr = old.split("[年|月|日|\\-|/]");
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<itemArr.length; i++){
                String item = itemArr[i];
                if (item.length() == 1){
                    item = "0"+item;
                }
                sb.append(item).append("-");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            String result = sb.toString();
            //标准化为年月日
            if (itemArr.length < 3 && standard != null){
                int sub = standard.length() - result.length();
                result = standard.substring(0, sub) + result;
            }
            if (itemArr.length < 3 && standard == null){
                Date curDate = new Date();
                String tmp = 1900+curDate.getYear()+"-"+result;
                /*if (df.parse(tmp).getTime() < curDate.getTime()){
                    result = (1900+curDate.getYear()+1)+"-" +result;
                } else {
                    result = tmp;
                }*/
                result = tmp;
            }
            return df.parse(result);
        } else {
            if (old.contains(".")){
                old = old.substring(0, old.indexOf("."));
            }
            if ("".equals(old)) return new Date();
            Long days = Long.valueOf(old);
            Long timestamp = datestampToTimestamp(days);
            Date result = new Date();
            result.setTime(timestamp);
            return result;
        }

    }

    static Long datestampToTimestamp(Long days){
        Long result = ((days - 70 * 365 - 19) * 86400 - 8 * 3600) * 1000;
        return result;
    }
}
