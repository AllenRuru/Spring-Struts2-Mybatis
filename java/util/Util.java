package com.samsunganycar.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {
   private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   public static String decrypt(String data)
    {
        char[] tmpByte = data.toCharArray();

        for (int i = 0; i < tmpByte.length ; i++)
        {
            tmpByte[i] = (char)(~tmpByte[i]);
        }
        
        return new String(tmpByte);
    }

    public static boolean isLogin(HttpServletRequest req) {
        HttpSession ses = req.getSession(true);
        boolean bLogin = false;        
        if (ses == null || ses.getAttribute("LOGIN_ID") == null || ses.getAttribute("LOGIN_ID").equals("")) {
            bLogin = false;
        } else {
            bLogin = true;
        }
        return bLogin;
    }

    public static boolean isExistEscapeString(HttpServletRequest req) {
        boolean bExist = false;

        String[] filter_word = {".","?","/","~","!"
                                ,"@","#","$","%","^"
                                ,"&","*","(",")"//,"_"
                                ,"+","=","|","\\","}"
                                ,"]","{","[","\"","'"
                                ,":",";","<",",",">"
                                };

        Box box = HttpUtility.getBox(req);
        String reqValue = "";

        for (Enumeration e = box.keys() ; e.hasMoreElements() ;) {
            reqValue = box.get(e.nextElement().toString());

            for (int i = 0 ; i < filter_word.length ; i++) {
                if (! reqValue.trim().equals("") && reqValue.indexOf(filter_word[i]) >= 0) {
                   System.out.println("hhhhhhhhhhhh:"+reqValue);
                    bExist = true;
                    break;
                }
            }
            if (bExist) break;
        }        
        return bExist;
    }

    public static boolean isExistEscapeString2(HttpServletRequest req) {
        boolean bExist = false;

        String[] filter_word = {"?","/","~","!"
                                ,"@","#","$","%","^"
                                ,"&","*","(",")"//,"_"
                                ,"+","=","|","\\","}"
                                ,"]","{","[","\"","'"
                                ,":",";","<",",",">"
                                };

        Box box = HttpUtility.getBox(req);
        String reqValue = "";

        for (Enumeration e = box.keys() ; e.hasMoreElements() ;) {
            reqValue = box.get(e.nextElement().toString());

            for (int i = 0 ; i < filter_word.length ; i++) {
                if (! reqValue.trim().equals("") && reqValue.indexOf(filter_word[i]) >= 0) {
                    bExist = true;
                    break;
                }
            }
            if (bExist) break;
        }
        return bExist;
    }

    public static String nullnbsp(String strData) {
        String rtnData = null;
        rtnData = strData;
        if (strData == null || strData.equals("")) {
            rtnData = "&nbsp;";
        }
        return rtnData;
    }
    
    public static String nbspnull(String strData) {
       if ("&nbsp;".equals(strData)) {
          return "";
       }
       return strData;
    }

    public static String nullempty(String strData) {
        String rtnData = null;
        rtnData = strData;
        if (strData == null || strData.equals("")) {
            rtnData = "";
        }
        return rtnData;
    }

    public static String nullempty(Object objData) {
        String rtnData = null;
        rtnData = String.valueOf(objData);
        if (objData == null || objData.equals("")) {
            rtnData = "";
        }
        return rtnData;
    }

    public static String nullreplace(String strData, String strReplace) {
        String rtnData = null;
        rtnData = strData;
        if (strData == null || strData.equals("")) {
            rtnData = strReplace;
        }
        return rtnData;
    }

    public static String date2split(String strData) {
        String rtnData = null;
        if (strData.length() == 8) {
            rtnData = strData.substring(0, 4) + "-" + strData.substring(4, 6) + "-" + strData.substring(6, 8);
        } else {
            rtnData = strData;
        }
        return rtnData;
    }

    public static String loadingHTML() {
        String rtnHTML = "";
        rtnHTML += "<div id=";
        return rtnHTML;
    }

    public static String getCurrentDate(String strPattern) {
        Date date = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST")).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(strPattern, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getCurrentAddDate(String strPattern, int addDay) {
        Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
        cal.add(Calendar.DATE, addDay);
        Date date = cal.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat(strPattern, Locale.getDefault());
        return formatter.format(date);
    }

    public static String removeComma(String str) {
      String oldstr = ",";
      String newstr = "";
        return str.replaceAll(oldstr, newstr);
   }

    public static String setComma(String str) {
        if (str == null || str.equals("")) str = "0";
        String rtnData = "";
        try {
            DecimalFormat decFormat = new DecimalFormat("###,###,###,###");
            rtnData = decFormat.format(Double.parseDouble(str));
        } catch (Exception e) {
            e.printStackTrace();
            rtnData = str;
        }
        return rtnData;
    }

    public static String setComma(String str, String format) {
        if (str == null || str.equals("")) str = "0";
        String rtnData = "";
        try {
            DecimalFormat decFormat = new DecimalFormat(format);
            rtnData = decFormat.format(Double.parseDouble(str)); 
        } catch (Exception e) {
            e.printStackTrace();
            rtnData = str;
        }
        return rtnData;
    }

   public static String EncodeBySType ( String strData )
   {
        if (strData.equals("")) return "";
      String strRet = null;
      strRet = Encrypt.com_Encode(":" + strData + ":sisenc");
      return strRet;
   }

   public static String DecodeBySType ( String strData )
   {
        if (strData.equals("")) return "";
      String strRet = null;
      int e, d, s, i=0;
      strRet = Encrypt.com_Decode(strData);
      e = strRet.indexOf ( ":" );
      d = strRet.indexOf ( ":sisenc" );
      strRet = strRet.substring(e + 1, d);
      return strRet;
   }

    public static String replace(String str, String oldstr, String newstr) {
      StringBuffer buf = new StringBuffer();

      if( str == null )
         return null;

      int savedpos = 0;
      while(true)
      {
         int pos = str.indexOf(oldstr, savedpos);
         if(pos != -1)
         {
            buf.append(str.substring(savedpos, pos));
            buf.append(newstr);

            savedpos = pos + oldstr.length();
            if(savedpos >= str.length())
               break;
         }
         else
            break;
      }

      buf.append(str.substring(savedpos, str.length()));
      return buf.toString();
   }
    
    public static String replaceAllErr(String str){
//        str = str.replaceAll("&", "&amp;");
       str = str.replaceAll("\"", "&quot;");
       str = str.replaceAll("\'", "&#39;");
       str = str.replaceAll("\r\n", " ").replaceAll("\n", " ");
       str = str.replaceAll("<", "&lt;");
       str = str.replaceAll(">", "&gt;");
        return str;
    }
    
    public static String reverseAllErr(String str) {
        if (str == null || "".equals(str)) {
            str = "";
        } else {
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll("&quot;", "\"");
            str = str.replaceAll("&#34;", "\"");
            str = str.replaceAll("&#39;", "\'");
            str = str.replaceAll("&nbsp;", " ");
            str = str.replaceAll("&lt;", "<");
            str = str.replaceAll("&gt;", ">");

            str = str.replaceAll("\r\n", " ").replaceAll("\n", " ");
        }
        return str;
    }

    public static String replaceDirectCharset(String str) {
        if (str == null || "".equals(str)) {
            str = "";
        } else {
            str = str.replaceAll("<%@ page contentType=\"text/html; charset=utf-8\" %>", "");
        }
        return str;
    }

    //<if test="@com.sdsc.core.util.Util@isEmpty(userId)">
    public static boolean isEmpty(Object o) throws IllegalArgumentException {
        if (o == null) return true;
        
        if (o instanceof String) {
            if (((String)o).length() == 0) {
                return true;
            }
        } else if (o instanceof Collection) {
            if (((Collection)o).isEmpty()) {
                return true;
            }
        } else if (o.getClass().isArray()) {
            if (Array.getLength(o) == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            return true;
        } else {
            return false;
        }

        return false;
    }

    //<if test="@com.sdsc.core.util.Util@isNotEmpty(userId)">
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static String getRemortIP(HttpServletRequest request) {
        String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (checkIP(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (checkIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (checkIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (checkIP(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (checkIP(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean checkIP(String ip) {
        return isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }
    
    public static String setReformIP(String ip) {   //127.0.0.1 -> 127.000.000.001
        String[] arrIP = Split(ip, ".");
        String rtnIP = "";
        for (int i = 0 ; i < arrIP.length ; i++) {
            rtnIP += ("000" + arrIP[i]).substring(("000" + arrIP[i]).length() - 3) + ".";
        }
        return rtnIP.substring(0, 15);
    }

    public static String setReformReverseIP(String ip) {   //127.000.000.001 -> 127.0.0.1
        String[] arrIP = Split(ip, ".");
        String rtnIP = "";
        for (int i = 0 ; i < arrIP.length ; i++) {
            rtnIP += String.valueOf(Integer.parseInt(arrIP[i])) + ".";
        }
        return rtnIP.substring(0, rtnIP.length() - 1);
    }
    
    public static String[] Split(String text, String parser) {
        int count = 0, index = 0, i = 0, endIdx = 0;
        
        if (text == null || "".equals(text)) return null;
        
        do {
            count++;
            index++;
            index = text.indexOf(parser, index);
        } while (index != -1);
        
        String[] data = new String[count];
        
        index = 0;
        
        while (i < count) {
            endIdx = text.indexOf(parser, index);
            if (endIdx == -1) {
                data[i] = text.substring(index).trim();
            } else {
                data[i] = text.substring(index, endIdx).trim();
            }
            index = endIdx + 1;
            i++;
        }

        return data;
    }

    public static boolean isNumber(String str) {
        boolean check = true;
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))){
                check = false;
                break;
            }
        }
        return check;
    }
    
    public static String setDate(String d) {
        String rtnDate = "";
        if (d.length() != 8) {
            rtnDate = d;
        }
        if (! isNumber(d)) {
            rtnDate = d;
        }
        if ("".equals(rtnDate)) {
            rtnDate = d.substring(0, 4) + "-" + d.substring(4, 6) + "-" + d.substring(6, 8);

            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                Date dt = df.parse(rtnDate);
            } catch (Exception e) {
                rtnDate = d;
            }
        }
        return rtnDate;
    }
    
    public static String file2Icon(String fileName) {
        String rtnIconFile = "";

        if (fileName.toLowerCase().endsWith("bmp")) rtnIconFile = "/img/file_icon/bmp.gif";
        else if (fileName.toLowerCase().endsWith("doc")) rtnIconFile = "/img/file_icon/doc.gif";
        else if (fileName.toLowerCase().endsWith("docx")) rtnIconFile = "/img/file_icon/doc.gif";
        else if (fileName.toLowerCase().endsWith("gif")) rtnIconFile = "/img/file_icon/gif.gif";
        else if (fileName.toLowerCase().endsWith("gul")) rtnIconFile = "/img/file_icon/gul.gif";
        else if (fileName.toLowerCase().endsWith("htm")) rtnIconFile = "/img/file_icon/html.gif";
        else if (fileName.toLowerCase().endsWith("html")) rtnIconFile = "/img/file_icon/html.gif";
        else if (fileName.toLowerCase().endsWith("hwp")) rtnIconFile = "/img/file_icon/hwp.gif";
        else if (fileName.toLowerCase().endsWith("jpg")) rtnIconFile = "/img/file_icon/jpg.gif";
        else if (fileName.toLowerCase().endsWith("jpeg")) rtnIconFile = "/img/file_icon/jpg.gif";
        else if (fileName.toLowerCase().endsWith("ppt")) rtnIconFile = "/img/file_icon/ppt.gif";
        else if (fileName.toLowerCase().endsWith("pptx")) rtnIconFile = "/img/file_icon/ppt.gif";
        else if (fileName.toLowerCase().endsWith("txt")) rtnIconFile = "/img/file_icon/txt.gif";
        else if (fileName.toLowerCase().endsWith("log")) rtnIconFile = "/img/file_icon/txt.gif";
        else if (fileName.toLowerCase().endsWith("xls")) rtnIconFile = "/img/file_icon/xls.gif";
        else if (fileName.toLowerCase().endsWith("xlsx")) rtnIconFile = "/img/file_icon/xls.gif";
        else if (fileName.toLowerCase().endsWith("zip")) rtnIconFile = "/img/file_icon/zip.gif";
        else if (fileName.toLowerCase().endsWith("rar")) rtnIconFile = "/img/file_icon/zip.gif";
        else if (fileName.toLowerCase().endsWith("wav")) rtnIconFile = "/img/file_icon/wav.gif";
        else if (fileName.toLowerCase().endsWith("mp3")) rtnIconFile = "/img/file_icon/wav.gif";
        else if (fileName.toLowerCase().endsWith("pdf")) rtnIconFile = "/img/file_icon/pdf.gif";
        else rtnIconFile = "/img/file_icon/any.gif";

        return rtnIconFile;
    }

    public static int calculateMonthBetweenTwoDaysPlus(String startDate, String endDate) {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(new DateTime(startDate));
        endCalendar.setTime(new DateTime(endDate));

        int useMonths = (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12
                + (endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH));

        if (endCalendar.get(Calendar.DATE) - startCalendar.get(Calendar.DATE) > 0
                || endCalendar.get(Calendar.DATE) == startCalendar.get(Calendar.DATE)) {
            useMonths++;
        }
        return useMonths;
    }

    /*
     * 使用年限
     */
    public static int getUseYears(String enrollDate, String startDateString) throws Exception {
       return Math.abs(diffYear(sdf.parse(enrollDate), sdf.parse(startDateString)));
    }
    
    /*
     * 使用月数
     */
    public static int getUseMonths(String enrollDate, String startDateString) throws Exception {
       return Math.abs(diffMonth(sdf.parse(enrollDate), sdf.parse(startDateString)));
    }
    
    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差年数
     */
    public static int diffYear(Date one, Date two) {
 
        int diffMonth = diffMonth(one, two);
        return (int) Math.floor(diffMonth / 12);
    }
    
    public static int diffMonth(Date one, Date two) {
        Calendar calendar = Calendar.getInstance();

        //得到第一个日期的年分和月份数
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);
        int monthOne = calendar.get(Calendar.MONTH);

        //得到第二个日期的年份和月份
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);
        int monthTwo = calendar.get(Calendar.MONTH);

        return ((yearTwo - yearOne) * 12) + (monthTwo - monthOne);
    }
    
    /**
     * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }

}
