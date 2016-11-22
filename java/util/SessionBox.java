package com.samsunganycar.util;

import java.util.Enumeration;
import java.util.Hashtable;

public class SessionBox extends Box {
    private javax.servlet.http.HttpSession  session = null;
    /**
     *
     */
    public SessionBox(javax.servlet.http.HttpSession session, String name) {
        super(name);
        this.session = session;
    }
    /**
     *
     * @param target com.lgeds.jdf.servlet.Box
     */
    public Object clone() {

        SessionBox sessionbox = new SessionBox(session, name);

        Hashtable src = (Hashtable)this;
        Hashtable target = (Hashtable)sessionbox;

        Enumeration e = src.keys();
        while(e.hasMoreElements()) {
            String key = (String) e.nextElement();
            Object value =  src.get(key);
            target.put(key,value);
        }
        return sessionbox;
    }

    public String getName() {
        return name;
    }

    public Object getObject(String key) {
        return super.get((Object)key);
    }

    public void putObject(String key, Object object) {
        super.put((Object)key, object);
    }

    public void release() {
        try {
            session.removeValue(name);
        }
        catch(Exception  e){
        }
        clear();
    }

    @Override
    public String toString() {
        return "Sessoin" + super.toString();
    }
}
