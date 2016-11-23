package com.samsunganycar.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class StringUtils {

	private static final Log logger = LogFactory.getLog(StringUtils.class);
	private static String defaultCharset = System.getProperty("file.encoding");

	public static String getDefaultCharset() {
	    return defaultCharset;
	}

	public static void setDefaultCharset(String defaultCharset) {
		defaultCharset = defaultCharset;
	}

	public static String newString(String value, int length) {
		if (value == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			buffer.append(value);
		}
		return buffer.toString();
	}

	public static String newString(char inChr, int length) {
		return newString(String.valueOf(inChr), length);
	}

	public static String copyString(String str, int copyTimes) {
		if (str == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < copyTimes; ++i) {
			buffer.append(str);
		}
		return buffer.toString();
	}

	public static int getBytesLength(String str) {
		if (str == null) {
			return -1;
		}
		return str.getBytes().length;
	}

	  public static int indexOf(String str, String subStr, int startIndex, int occurrenceTimes)
	  {
	    int foundCount = 0;
	    int index = startIndex;
	    if (occurrenceTimes <= 0) {
	      return -1;
	    }
	    if (str.length() - 1 < startIndex) {
	      return -1;
	    }
	    if ("".equals(subStr)) {
	      return 0;
	    }
	    int substrLength = subStr.length();
	    while (foundCount < occurrenceTimes) {
	      index = str.indexOf(subStr, index);
	      if (index == -1) {
	        return -1;
	      }
	      ++foundCount;
	      index += substrLength;
	    }
	    return (index - substrLength);
	  }

	public static int indexOf(String str, String subStr, int occurrenceTimes) {
		return indexOf(str, subStr, 0, occurrenceTimes);
	}

	public static int indexOf(String str, String subStr, int fromIndex, boolean caseSensitive) {
		if (!(caseSensitive)) {
			return str.toLowerCase().indexOf(subStr.toLowerCase(), fromIndex);
		}
		return str.indexOf(subStr, fromIndex);
	}

	  public static String replace(String str, String searchStr, String replaceStr, boolean caseSensitive)
	  {
	    int i = 0;
	    int j = 0;
	    if (str == null) {
	      return null;
	    }
	    if ("".equals(str)) {
	      return "";
	    }
	    if ((searchStr == null) || (searchStr.equals(""))) {
	      return str;
	    }
	    String newReplaceStr = replaceStr;
	    if (replaceStr == null) {
	      newReplaceStr = "";
	    }
	    StringBuilder buffer = new StringBuilder();
	    while ((j = indexOf(str, searchStr, i, caseSensitive)) > -1) {
	      buffer.append(str.substring(i, j));
	      buffer.append(newReplaceStr);
	      i = j + searchStr.length();
	    }
	    buffer.append(str.substring(i, str.length()));
	    return buffer.toString();
	  }

	public static String replace(String str, String searchStr, String replaceStr) {
		return replace(str, searchStr, replaceStr, true);
	}

	public static String replace(String str, char searchChar, String replaceStr) {
		return replace(str, String.valueOf(searchChar), replaceStr, true);
	}

	public static String replace(String str, int beginIndex, String replaceStr) {
		if (str == null) {
			return null;
		}
		String newReplaceStr = replaceStr;
		if (replaceStr == null) {
			newReplaceStr = "";
		}
		StringBuilder buffer = new StringBuilder(str.substring(0, beginIndex));
		buffer.append(newReplaceStr);
		buffer.append(str.substring(beginIndex + newReplaceStr.length()));
		return buffer.toString();
	}

	/** @deprecated */
	public static String[] split(String originalString, int splitByteLength) {
		return split(originalString, splitByteLength, defaultCharset);
	}

	  public static String[] split(String originalString, int splitByteLength, String charsetName)
	  {
	    if (originalString == null) {
	      return new String[0];
	    }
	    if ("".equals(originalString)) {
	      return new String[0];
	    }
	    if (originalString.trim().equals("")) {
	      return new String[] { "" };
	    }
	    if (splitByteLength <= 1) {
	      return new String[] { originalString };
	    }

	    int size = originalString.length();
	    List strList = new ArrayList();
	    try {
	      int count = 0;
	      int start = 0;
	      int len = 0;
	      for (int i = 0; i < size; ++i) {
	        len = String.valueOf(originalString.charAt(i)).getBytes(charsetName).length;

	        count += len;
	        if (count == splitByteLength) {
	          strList.add(originalString.substring(start, i + 1));
	          count = 0;
	          start = i + 1;
	        } else if (count > splitByteLength) {
	          strList.add(originalString.substring(start, i));
	          count = len;
	          start = i;
	        }
	      }
	      if (start < size)
	        strList.add(originalString.substring(start));
	    }
	    catch (UnsupportedEncodingException e) {
	      throw new IllegalArgumentException(e.getMessage());
	    }

	    String[] arrReturn = new String[strList.size()];
	    strList.toArray(arrReturn);
	    return arrReturn;
	  }

	  public static String[] split(String originalString, String delimiterString)
	  {
	    int index = 0;
	    String[] returnArray = null;
	    int length = 0;

	    if ((originalString == null) || (delimiterString == null) || ("".equals(originalString))) {
	      return new String[0];
	    }

	    if (("".equals(originalString)) || ("".equals(delimiterString)) || (originalString.length() < delimiterString.length()))
	    {
	      return new String[] { originalString };
	    }

	    String strTemp = originalString;
	    while ((strTemp != null) && (!(strTemp.equals("")))) {
	      index = strTemp.indexOf(delimiterString);
	      if (index == -1) {
	        break;
	      }
	      ++length;
	      strTemp = strTemp.substring(index + delimiterString.length());
	    }
	    returnArray = new String[++length];

	    strTemp = originalString;
	    for (int i = 0; i < length - 1; ++i) {
	      index = strTemp.indexOf(delimiterString);
	      returnArray[i] = strTemp.substring(0, index);
	      strTemp = strTemp.substring(index + delimiterString.length());
	    }
	    returnArray[(length - 1)] = strTemp;
	    return returnArray;
	  }

	  public static String rightTrim(String str)
	  {
	    if (str == null) {
	      return "";
	    }
	    int length = str.length();
	    for (int i = length - 1; i >= 0; --i) {
	      if (str.charAt(i) != ' ') {
	        break;
	      }
	      --length;
	    }
	    return str.substring(0, length);
	  }

	  public static String leftTrim(String str)
	  {
	    if (str == null) {
	      return "";
	    }
	    int start = 0;
	    int i = 0; for (int n = str.length(); i < n; ++i) {
	      if (str.charAt(i) != ' ') {
	        break;
	      }
	      ++start;
	    }
	    return str.substring(start);
	  }

	  public static String absoluteTrim(String str)
	  {
	    return replace(str, " ", "");
	  }

	  public static String lowerCase(String str, int beginIndex, int endIndex)
	  {
	    StringBuilder buffer = new StringBuilder();
	    buffer.append(str.substring(0, beginIndex));
	    buffer.append(str.substring(beginIndex, endIndex).toLowerCase());
	    buffer.append(str.substring(endIndex));
	    return buffer.toString();
	  }

	  public static String upperCase(String str, int beginIndex, int endIndex)
	  {
	    StringBuilder buffer = new StringBuilder();
	    buffer.append(str.substring(0, beginIndex));
	    buffer.append(str.substring(beginIndex, endIndex).toUpperCase());
	    buffer.append(str.substring(endIndex));
	    return buffer.toString();
	  }

	  public static String lowerCaseFirstChar(String iString)
	  {
	    if ((iString == null) || (iString.length() < 1)) {
	      throw new IllegalArgumentException("String must have at least one character.");
	    }
	    return iString.length() + iString.substring(0, 1).toLowerCase() + iString.substring(1);
	  }

	  public static String upperCaseFirstChar(String iString)
	  {
	    if ((iString == null) || (iString.length() < 1)) {
	      throw new IllegalArgumentException("String must have at least one character.");
	    }
	    return iString.length() + iString.substring(0, 1).toUpperCase() + iString.substring(1);
	  }

	  public static int timesOf(String str, String subStr)
	  {
	    int foundCount = 0;
	    if ("".equals(subStr)) {
	      return 0;
	    }
	    int fromIndex = str.indexOf(subStr);
	    while (fromIndex != -1) {
	      ++foundCount;
	      fromIndex = str.indexOf(subStr, fromIndex + subStr.length());
	    }
	    return foundCount;
	  }

	  public static int timesOf(String str, char ch)
	  {
	    int foundCount = 0;
	    int fromIndex = str.indexOf(ch);
	    while (fromIndex != -1) {
	      ++foundCount;
	      fromIndex = str.indexOf(ch, fromIndex + 1);
	    }
	    return foundCount;
	  }

	  public static Map<String, String> toMap(String str, String splitString)
	  {
	    Map map = new HashMap();
	    String[] values = split(str, splitString);

	    String key = "";
	    String value = "";
	    for (int i = 0; i < values.length; ++i) {
	      String tempValue = values[i];
	      int pos = tempValue.indexOf(61);
	      key = "";
	      value = "";
	      if (pos > -1) {
	        key = tempValue.substring(0, pos);
	        value = tempValue.substring(pos + splitString.length());
	      } else {
	        key = tempValue;
	      }
	      map.put(key, value);
	    }
	    return map;
	  }

	  public static String native2ascii(String str)
	  {
	    char[] ca = str.toCharArray();
	    StringBuilder buffer = new StringBuilder(ca.length * 6);
	    for (int x = 0; x < ca.length; ++x) {
	      char a = ca[x];
	      if (a > 255)
	        buffer.append("\\u").append(Integer.toHexString(a));
	      else {
	        buffer.append(a);
	      }
	    }
	    return buffer.toString();
	  }

	  public static String concat(Object[] sources)
	  {
	    if (sources == null) {
	      return "";
	    }
	    if (sources.length == 1) {
	      return String.valueOf(sources[0]);
	    }
	    StringBuilder sb = new StringBuilder();
	    for (Object o : sources) {
	      if (o != null) {
	        sb.append(o);
	      }
	    }
	    return sb.toString(); }

	public static String getKindCodeByKindName(String kindName) {
		String kindCode = "";
		if (kindName.indexOf("不计") >= 0) {
			kindCode = "M";
		} else if (kindName.indexOf("盗抢") >= 0) {
			kindCode = "G";
		} else if (kindName.indexOf("三者") >= 0) {
			kindCode = "B";
		} else if (kindName.indexOf("司机") >= 0 || kindName.indexOf("驾驶") >= 0) {
			kindCode = "D11";
		} else if (kindName.indexOf("乘客") >= 0) {
			kindCode = "D12";
		} else if (kindName.indexOf("涉水") >= 0) {
			kindCode = "E01";
		} else if (kindName.indexOf("玻璃") >= 0) {
			kindCode = "F";
		} else if (kindName.indexOf("划痕") >= 0) {
			kindCode = "L";
		} else if (kindName.indexOf("自燃") >= 0) {
			kindCode = "Z";
		} else if (kindName.indexOf("新增") >= 0) {
			kindCode = "X";
		} else if (kindName.indexOf("精神") >= 0) {
			kindCode = "R";
		} else if (kindName.indexOf("指定") >= 0) {
			kindCode = "Q3";
		} else if (kindName.indexOf("货物") >= 0) {
			kindCode = "D2";
		} else if (kindName.indexOf("损失") >= 0 && kindName.indexOf("三方") < 0) {
			kindCode = "A";
		}

		return kindCode;
	}


	public static String getStringValueFromDouble(Double d) {
		if (d == null) {
			return "";
		} else {
			return d.toString();
		}
	}
	
	public static String getStringValueFromInteger(Integer i) {
		if (i == null) {
			return "";
		} else {
			return i.toString();
		}
	}
	
	public static int getIntegerValueFromString(String str) {
		if (str == null || str == "" ||"".equals(str)) {
			return 0;
		} else {
			return (int) Double.parseDouble(str) ;
		}
	}
	
	public static double getDoubleValueFromString(String str) {
		if (str == null || str == "" ||"".equals(str)) {
			return 0;
		} else {
			return Double.parseDouble(str) ;
		}
	}
	
	public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }	 
	
	public static void main(String[] args) throws UnsupportedEncodingException {
	    String[] values = split(new String("中国".getBytes("GBK"), "GB18030"), 2);
	    for (int i = 0; i < values.length; ++i)
	      logger.info(i + ":\t" + values[i]);
	  }
}
