package tech.yeez.investment.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: xiangbin
 * @create: 2022-03-02 13:40
 **/
public class DateUtil {

    public static Date addMinute(Date date, int minite){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minite);
        return calendar.getTime();
    }

    public static Date addDay(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static String getThisDayBeginTime(LocalDate localDate) {
       LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        long localDateStr = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        return String.valueOf(localDateStr);
    }


    public static void main(String[] args) throws Exception {
        String date = "2022-02-28 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        System.out.println(sdf.format(calendar.getTime()));
    }

}
