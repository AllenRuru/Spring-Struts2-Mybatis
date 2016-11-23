package com.samsunganycar.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class HttpUtility {

    private HttpUtility() {}

    public static String decode(String s) {
        return java.net.URLEncoder.encode(s);
    }

    public static String encode(String s) {
        return java.net.URLEncoder.encode(s);
    }

    public static Box getBox(HttpServletRequest req)  {
        Box box = new Box("requestbox");

        Enumeration e = req.getParameterNames();
        while(e.hasMoreElements()){
            String key = (String)e.nextElement();
            if ("InsuredReadCardPicture".equals(key) || "AppliReadCardPicture".equals(key)) {
                box.put(key, req.getParameter(key));
            } else {
                box.put(key, replaceSpecialCharater(req.getParameter(key)));
            }
        }

        e = req.getAttributeNames();
        while(e.hasMoreElements()){
            String key = (String)e.nextElement();
            box.put(key, replaceSpecialCharater(req.getAttribute(key).toString()));
        }

        box.put("IP_ADDR", Util.getRemortIP(req));
        box.put("USER_AGENT", (String) req.getHeader("user-agent"));
        box.put("SESSION_ID", req.getSession(true).getId());
        if (req.getHeader("Referer") == null) {
            box.put("REFERER", "");
        } else {
            box.put("REFERER", req.getHeader("Referer"));
        }

        return box;
    }

    public static Box getBoxFromCookie(HttpServletRequest req)  {
        Box cookiebox = new Box("cookiebox");
        javax.servlet.http.Cookie[] cookies = req.getCookies();
        if ( cookies == null ) return cookiebox;

        for(int i=0; cookies != null && i< cookies.length; i++) {
            String key = cookies[i].getName();
            String value = cookies[i].getValue();
            if ( value == null ) value = "";
            String[] values = new String[1];
            values[0] = value;
            cookiebox.put(key,values);
        }
        return cookiebox;
    }

    public static SessionBox getNewSessionBox(HttpServletRequest req)  {
        return getNewSessionBox(req, "shared session box");
    }

    public static SessionBox getNewSessionBox(HttpServletRequest req, String name)  {

        javax.servlet.http.HttpSession  session = req.getSession(true);

        SessionBox sessionbox = null;

        if (! session.isNew()) {
            Object o = session.getValue(name);
            if ( o != null ) {
                session.removeValue(name);
            }
        }

        if ( sessionbox == null ) {
            sessionbox = new SessionBox(session, name);
            session.putValue(name, sessionbox);
        }

        Enumeration e = req.getParameterNames();
        while(e.hasMoreElements()){
            String key = (String)e.nextElement();
            sessionbox.put(key, req.getParameterValues(key));
        }
        return sessionbox;
    }

    public static SessionBox getSessionBox(HttpServletRequest req)  {
        return getSessionBox(req, "shared session box");
    }

    public static SessionBox getSessionBox(HttpServletRequest req, String name)  {

        javax.servlet.http.HttpSession  session = req.getSession(true);

        SessionBox sessionbox = null;

        Object o = session.getValue(name);
        if ( o != null ) {
            if ( o instanceof SessionBox ) {
                sessionbox = (SessionBox)o;
            }
            else {
                session.removeValue(name);
            }
        }

        if ( sessionbox == null ) {
            sessionbox = new SessionBox(session, name);
            session.putValue(name, sessionbox);
        }

        Enumeration e = req.getParameterNames();
        while(e.hasMoreElements()){
            String key = (String)e.nextElement();
            sessionbox.put(key, req.getParameterValues(key));
        }
        return sessionbox;
    }

    public static boolean isOverIE50(HttpServletRequest req) {
        String user_agent = (String) req.getHeader("user-agent");

        if ( user_agent == null ) 	return false;

        int index = user_agent.indexOf("MSIE");
        if ( index == -1 ) return false;

        int version = 0;
        try {
            version = Integer.parseInt(user_agent.substring(index+5, index+5+1));
        }
        catch(Exception e){}
        if ( version < 5 ) return false;

        return true;
    }

    protected static String replaceSpecialCharater(String val) {
        String rtnVal = val;
        try {
//            rtnVal = rtnVal.replaceAll("\\&", "&amp;");
            rtnVal = rtnVal.replaceAll("\\<", "&lt;");
            rtnVal = rtnVal.replaceAll("\\>", "&gt;");
            rtnVal = rtnVal.replaceAll("\\\"", "&quot;");
            rtnVal = rtnVal.replaceAll("\\%", "&#37;");

            rtnVal = rtnVal.replaceAll("<","&lt;");
            rtnVal = rtnVal.replaceAll(">","&gt;");
            rtnVal = rtnVal.replaceAll("\"","&quot;");
            rtnVal = rtnVal.replaceAll("'","&apos;");
//            rtnVal = rtnVal.replaceAll("#","&#35;");
            rtnVal = rtnVal.replaceAll("\\(","&#40;");
            rtnVal = rtnVal.replaceAll("\\)","&#41;");
            rtnVal = rtnVal.replaceAll("/","&#47;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnVal;
    }
}
