package com.samsunganycar.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

public class DataUtils {

	private static final Log logger = LogFactory.getLog(DataUtils.class);
	  private static final Object[] ZERO_OBJECT_ARRAY = new Object[0];
	  private static final BigDecimal ONE = new BigDecimal("1");
	  private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("###0");
	  private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("###0.00");
	  private static final String[] TRUE_ARRAY = { "y", "yes", "true", "t", "是", "1" };
	  private static final String[] FALSE_ARRAY = { "n", "no", "false", "f", "否", "0" };

	  private static Map<Class, String> supportTypeMap = new HashMap();

	  public static String zeroToEmpty(int value)
	  {
	    return ((value == 0) ? "" : String.valueOf(value));
	  }

	  public static String zeroToEmpty(double value)
	  {
	    return ((value == 0.0D) ? "" : String.valueOf(value));
	  }

	  public static String nullToEmpty(String str)
	  {
	    return ((str == null) ? "" : str);
	  }

	  public static String emptyToNull(String str)
	  {
	    if ((str == null) || (str.trim().length() == 0)) {
	      return null;
	    }
	    return str;
	  }

	  public static String dbNullToEmpty(String str)
	  {
	    if ((str == null) || (str.equalsIgnoreCase("null"))) {
	      return "";
	    }
	    return str;
	  }

	  public static String nullToZero(String str)
	  {
	    if ((str == null) || (str.trim().length() == 0)) {
	      return "0";
	    }
	    return str;
	  }

	  public static String getBooleanDescribe(String str)
	  {
	    if (str == null) {
	      throw new IllegalArgumentException("argument is null");
	    }
	    String retValue = null;
	    if (str.trim().equals("")) {
	      retValue = "";
	    }
	    for (int i = 0; i < TRUE_ARRAY.length; ++i) {
	      if (str.equalsIgnoreCase(TRUE_ARRAY[i])) {
	        retValue = "是";
	        break;
	      }
	    }
	    for (int i = 0; i < FALSE_ARRAY.length; ++i) {
	      if (str.equalsIgnoreCase(FALSE_ARRAY[i])) {
	        retValue = "否";
	        break;
	      }
	    }
	    if (retValue == null) {
	      throw new IllegalArgumentException("argument not in ('y','n','yes','no','true','false','t','f','是','否','1','0','')");
	    }

	    return retValue;
	  }

	  public static boolean getBoolean(String str)
	  {
	    if (str == null) {
	      throw new IllegalArgumentException("argument is null");
	    }
	    for (int i = 0; i < TRUE_ARRAY.length; ++i) {
	      if (str.equalsIgnoreCase(TRUE_ARRAY[i])) {
	        return true;
	      }
	    }
	    for (int i = 0; i < FALSE_ARRAY.length; ++i) {
	      if (str.equalsIgnoreCase(FALSE_ARRAY[i])) {
	        return false;
	      }
	    }
	    if (str.trim().equals("")) {
	      return false;
	    }
	    throw new IllegalArgumentException("argument not in ('y','n','yes','no','true','false','t','f','是','否','1','0','')");
	  }

	  public static String getBooleanDescribe(boolean value)
	  {
	    if (value) {
	      return getBooleanDescribe("true");
	    }
	    return getBooleanDescribe("false");
	  }

	  public static int compareByValue(String str1, String str2)
	  {
	    return new BigDecimal(str1).compareTo(new BigDecimal(str2));
	  }

	  public static double round(double value, int scale)
	  {
	    BigDecimal obj = new BigDecimal(Double.toString(value));
	    return obj.divide(ONE, scale, 4).doubleValue();
	  }

	  public static void copySimpleObject(Object target, Object source)
	  {
	    Map map;
	    Method method;
	    Object value;
	    Object[] objArray;
	    StringBuilder sb;
	     
	    if ((target != null) && (source != null)) {
	      List targetMethodList = ObjectUtils.getSetter(target.getClass());
	      List sourceMethodList = ObjectUtils.getGetter(source.getClass());
	      map = new HashMap();

	      for (Iterator iter = sourceMethodList.iterator(); iter.hasNext(); ) {
	        method = (Method)iter.next();
	        map.put(method.getName(), method);
	      }
	      value = null;
	      objArray = new Object[1];

	      sb = new StringBuilder();

	      for (Iterator iter = targetMethodList.iterator(); iter.hasNext(); ) {
	        method = (Method)iter.next();
	        String fieldName = method.getName().substring(3);
	        try { Method sourceMethod;
	          do { sb.setLength(0);
	            sb.append("get").append(fieldName);
	            sourceMethod = (Method)map.get(sb.toString());
	            if (sourceMethod == null) {
	              sb.setLength(0);
	              sb.append("is").append(fieldName);
	              sourceMethod = (Method)map.get(sb.toString()); }
	          }
	          while (sourceMethod == null);

	          value = sourceMethod.invoke(source, ZERO_OBJECT_ARRAY);
	          if (value != null) {
	            objArray[0] = value;
	            method.invoke(target, objArray);
	          }
	        } catch (Exception e) {
	          if (logger.isDebugEnabled())
	            logger.debug(e);
	        }
	      }
	    }
	  }

	  public static void copySimpleObject(Object target, Object source, boolean isCopyNull)
	  {
	    Method method;
	    if ((target == null) || (source == null)) {
	      return;
	    }
	    List targetMethodList = ObjectUtils.getSetter(target.getClass());
	    List sourceMethodList = ObjectUtils.getGetter(source.getClass());
	    Map map = new HashMap();

	    for (Iterator iter = sourceMethodList.iterator(); iter.hasNext(); ) {
	      method = (Method)iter.next();
	      map.put(method.getName(), method);
	    }
	    Object value = null;
	    Object[] objArray = new Object[1];

	    StringBuilder sb = new StringBuilder();

	    for (Iterator iter = targetMethodList.iterator(); iter.hasNext(); ) {
	      method = (Method)iter.next();
	      String fieldName = method.getName().substring(3);
	      try { Method sourceMethod;
	        do do { sb.setLength(0);
	            sb.append("get").append(fieldName);
	            sourceMethod = (Method)map.get(sb.toString());
	            if (sourceMethod == null) {
	              sb.setLength(0);
	              sb.append("is").append(fieldName);
	              sourceMethod = (Method)map.get(sb.toString()); }
	          }
	          while (sourceMethod == null);

	        while (!(supportTypeMap.containsKey(sourceMethod.getReturnType())));

	        value = sourceMethod.invoke(source, ZERO_OBJECT_ARRAY);
	        if (isCopyNull) {
	          objArray[0] = value;
	          method.invoke(target, objArray);
	        }
	        else if (value != null) {
	          objArray[0] = value;
	          method.invoke(target, objArray);
	        }
	      }
	      catch (Exception e) {
	        if (logger.isDebugEnabled())
	          logger.debug(e);
	      }
	    }
	  }

	  public static List generateListFromJdbcResult(List jdbcResultList, Class clazz)
	  {
	    List objectList = new ArrayList();
	    Object[] objArray = new Object[1];
	    try {
	      List methodList = ObjectUtils.getSetter(clazz);
	      for (int i = 0; i < jdbcResultList.size(); ++i) {
	        Map rowMap = (Map)jdbcResultList.get(i);
	        Object[] rowKeys = rowMap.keySet().toArray();
	        Object object = clazz.newInstance();
	        for (int j = 0; j < rowKeys.length; ++j) {
	          String column = (String)rowKeys[j];
	          for (int k = 0; k < methodList.size(); ++k) {
	            Method method = (Method)methodList.get(k);
	            String upperMethodName = method.getName().toUpperCase();
	            if (upperMethodName.equals("SET" + column)) {
	              Class type = method.getParameterTypes()[0];
	              Object value = rowMap.get(column);
	              if (value != null) {
	                if (type == Integer.class)
	                  value = Integer.valueOf(value.toString());
	                else if (type == Double.class)
	                  value = Double.valueOf(value.toString());
	                else if (type == Long.class) {
	                  value = Long.valueOf(value.toString());
	                }
	              }
	              objArray[0] = value;
	              method.invoke(object, objArray);
	              break;
	            }
	          }
	        }
	        objectList.add(object);
	      }
	    } catch (IllegalAccessException ex) {
	      throw new RuntimeException(ex);
	    }
	    catch (InvocationTargetException ex) {
	      throw new RuntimeException(ex);
	    }
	    catch (InstantiationException e) {
	      throw new RuntimeException(e);
	    }

	    return objectList;
	  }

	  public static Integer getInteger(Object object)
	  {
	    Integer value = null;
	    if (object != null) {
	      value = Integer.valueOf(object.toString());
	    }
	    return value;
	  }

	  public static Long getLong(Object object)
	  {
	    Long value = null;
	    if (object != null) {
	      value = Long.valueOf(object.toString());
	    }
	    return value;
	  }

	  public static Double getDouble(Object object)
	  {
	    Double _double = null;
	    if (object != null) {
	      _double = new Double(object.toString());
	    }
	    return _double;
	  }

	  public static DateTime getDateTime(Object object)
	  {
	    DateTime dateTime = null;
	    if (object != null) {
	      if (object.getClass() == Date.class)
	        dateTime = new DateTime((Date)object, 13);
	      else if (object.getClass() == Timestamp.class)
	        dateTime = new DateTime((Timestamp)object, 16);
	      else {
	        dateTime = new DateTime((Date)object);
	      }
	    }
	    return dateTime;
	  }

	  public static String getString(Object object)
	  {
	    String string = null;
	    if (object != null) {
	      string = object.toString();
	    }
	    return string;
	  }

	  public static String getPlainNumber(Integer value) {
	    if (value == null) {
	      return "";
	    }
	    return NUMBER_FORMAT.format(value);
	  }

	  public static String getPlainNumber(Long value) {
	    if (value == null) {
	      return "";
	    }
	    return NUMBER_FORMAT.format(value);
	  }

	  public static String getPlainNumber(Double value) {
	    if (value == null) {
	      return "";
	    }
	    return DOUBLE_FORMAT.format(value); }

	  public static String correctNumber(String numberString) {
	    String value = numberString;
	    if ((value != null) && (!(value.equals("")))) {
	      value = StringUtils.replace(value, ',', "");
	    }
	    return value;
	  }

	  public static DateTime getDateTime(String value) {
	    DateTime d = null;
	    if (value.trim().length() == 0) {
	      return d;
	    }

	    if (value.indexOf(" 00:00:00 UTC+0800") != -1) {
	      try {
	        d = new DateTime(new Date(value), 13);
	      } catch (IllegalArgumentException e4) {
	        logger.error(e4.toString());
	      }
	      return d;
	    }
	    try {
	      d = new DateTime(value, 17);
	    } catch (IllegalArgumentException e1) {
	      try {
	        d = new DateTime(value, 16);
	      } catch (IllegalArgumentException e2) {
	        try {
	          d = new DateTime(value, 14);
	        } catch (IllegalArgumentException e3) {
	          try {
	            d = new DateTime(value, 13);
	          } catch (IllegalArgumentException e4) {
	            logger.error(e4.toString());
	          }
	        }
	      }
	    }
	    return d;
	  }

	  static
	  {
	    supportTypeMap.put(Integer.class, "");
	    supportTypeMap.put(Long.class, "");
	    supportTypeMap.put(Double.class, "");
	    supportTypeMap.put(BigDecimal.class, "");
	    supportTypeMap.put(String.class, "");
	    supportTypeMap.put(Date.class, "");
	    supportTypeMap.put(Boolean.class, "");
	  }

}
