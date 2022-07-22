package com.hoiuc.io;

import com.hoiuc.assembly.WaitLogin;
import com.hoiuc.server.Ip;
import org.joda.time.DateTime;

import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Util {
    private static boolean debug = false;
    private static final Locale locale = new Locale("vi");
    private static final NumberFormat en = NumberFormat.getInstance(locale);
    private static final Random rand = new Random();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormatWeek = new SimpleDateFormat("yyyy-MM-ww");
    private static SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDate2(String dateString) {
        try {
            return dateFormatDay.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean percent(int maxPercent, int percent) {
        return percent >= nextInt(1, maxPercent);
    }

    public static void getDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static long TimeDay(int nDays) {
        return System.currentTimeMillis() + nDays * 86400000L;
    }
    public static long TimeHours(int nHours) {
        return System.currentTimeMillis() + nHours * 3600000L;
    }
    public static long TimeMinutes(int nMinutes) {
        return System.currentTimeMillis() + nMinutes * 60000L;
    }
    public static long TimeSeconds(long nSeconds) {
        return System.currentTimeMillis() + nSeconds * 1000L;
    }
    public static long TimeMillis(long nMillis) {
        return System.currentTimeMillis() + nMillis;
    }

    public static Date DateDay(int nDays) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nDays * 86400000L);
        return dat;
    }

    public static String toDateString(Date date) {
        try {
            String a = Util.dateFormat.format(date);
            return a;
        } catch (Exception e) {
            return "2021-01-01 01:01:00";
        }
    }
    public static Date DateHours(int nHours) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nHours * 3600000L);
        return dat;
    }

    public static Date DateMinutes(int nMinutes) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nMinutes * 60000L);
        return dat;
    }

    public static Date DateSeconds(long nSeconds) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nSeconds * 1000L);
        return dat;
    }

    public static String getFormatNumber(long num) {
        return Util.en.format(num);
    }

    public static boolean compare_Week(Date now, Date when) {
        try {
            Date date1 = Util.dateFormatWeek.parse(Util.dateFormatWeek.format(now));
            Date date2 = Util.dateFormatWeek.parse(Util.dateFormatWeek.format(when));
            return !date1.equals(date2) && !date1.before(date2);
        }
        catch (Exception p) {
            p.printStackTrace();
            return false;
        }
    }

    public static synchronized boolean compare_Day(Date now, Date when) {
        try {
            Date date1 = Util.dateFormatDay.parse(Util.dateFormatDay.format(now));
            Date date2 = Util.dateFormatDay.parse(Util.dateFormatDay.format(when));
            return !date1.equals(date2) && !date1.before(date2);
        }
        catch (Exception p) {
            p.printStackTrace();
            return false;
        }
    }

    public static boolean checkNumInt(String num) {
        return Pattern.compile("^[0-9]+$").matcher(num).find();
    }

    public static int UnsignedByte(byte b) {
        int ch = b;
        if (ch < 0) {
            return ch + 256;
        }
        return ch;
    }

    public static String parseString(String str, String wall) {
        return str.contains(wall) ? str.substring(str.indexOf(wall) + 1) : null;
    }

    public static boolean CheckString(String str, String c) {
        return Pattern.compile(c).matcher(str).find();
    }

    public static String strSQL(String str) {
        return str.replaceAll("['\"\\\\]", "\\\\$0");
    }

    public static int nextInt(int x1, int x2) {
        int to = x2;
        int from = x1;
        if (x2 < x1) {
            to = x1;
            from = x2;
        }
        return from + Util.rand.nextInt(to + 1 - from);
    }

    public static int nextInt(int max) {
        return Util.rand.nextInt(max);
    }

    protected static String getStrTime(long time) {
        if (time >= 86400000L)
            return (time / 86400000L) + " Ngay";
        if (time >= 3600000L)
            return (time / 3600000L) + " giờ";
        if (time >= 60000L)
            return (time / 60000L) + " phút";
        if (time >= 1000L)
            return (time / 1000L) + " giây";
        return ((float)time / 1000.0F) + " giây";
    }

    public static void setDebug(boolean v) {
        Util.debug = v;
    }

    public static void Debug(String v) {
        if (Util.debug) {
            System.out.println(v);
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isNumericInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isNumericLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static int countIP(ArrayList<String> list, String ip) {
        return Collections.frequency(list, ip);
    }

    public static int countUsername(ArrayList<WaitLogin> list, String username) {
        return Collections.frequency(list, username);
    }

    public static List<Ip> ReadIp(){
        ArrayList<Ip> list = new ArrayList<>();
        try{
            FileReader fr = new FileReader("log/ip.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while(true){
                line = br.readLine();
                if (line == null){
                    break;
                }
                /*String txt[] = line.split(";");
                String name = txt[0];
                byte log = Byte.parseByte(txt[1]);*/
                list.add(new Ip(line));
            }
            fr.close();
            br.close();
        }catch (Exception e){
        }
        return list;
    }
     public static List<Ip> ReadIp1(){
        ArrayList<Ip> list = new ArrayList<>();
        try{
            FileReader fr = new FileReader("log/ipmember.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while(true){
                line = br.readLine();
                if (line == null){
                    break;
                }
                /*String txt[] = line.split(";");
                String name = txt[0];
                byte log = Byte.parseByte(txt[1]);*/
                list.add(new Ip(line));
            }
            fr.close();
            br.close();
        }catch (Exception e){
        }
        return list;
    }
    
    public static void WriteIp(String IP){
        try{
            FileWriter fw = new FileWriter("log/ip.txt",true);
            BufferedWriter bs = new BufferedWriter(fw);
            bs.write(IP);
            bs.newLine();
            bs.close();
            fw.close();
        }catch (Exception e){
        }
    }

    public static boolean isSameWeek(Date d1, Date d2) {
        if ((d1 == null) || (d2 == null))
            throw new IllegalArgumentException("The date must not be null");
        return isSameWeek(new DateTime(d1), new DateTime(d2));
    }

    public static boolean isSameWeek(DateTime d1, DateTime d2) {
        if ((d1 == null) || (d2 == null))
            throw new IllegalArgumentException("The date must not be null");
        final int week1 = d1.getWeekOfWeekyear();
        final int week2 = d2.getWeekOfWeekyear();
        final int year1 = d1.getWeekyear();
        final int year2 = d2.getWeekyear();
        if ((week1 == week2) && (year1 == year2))
            return true;
        return false;
    }

    public static String getDay(Date date) {
        try {
            DateFormat format2 = new SimpleDateFormat("EEEE");
            return format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
