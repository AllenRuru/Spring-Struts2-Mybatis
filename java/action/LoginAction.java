package com.samsunganycar.agency;

import com.google.gson.GsonBuilder;
import com.samsunganycar.model.SFMN00TB;
import com.samsunganycar.model.SFUS04TB;
import com.samsunganycar.model.SFUSER;
import com.samsunganycar.model.SfTemporary;
import com.samsunganycar.util.Box;
import com.samsunganycar.util.HttpUtility;
import com.samsunganycar.util.PropertiesUtils;
import com.samsunganycar.util.Util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginAction extends BaseAction {

    public String login() {

        Map<String, Object> mapLoginedUser = new HashMap<String, Object>();
        mapLoginedUser = (Map<String, Object>) getSession(true).getAttribute("LOGINED_USER");

        if (mapLoginedUser == null) {
            return LOGIN;
        } else {
            return SUCCESS;
        }
    }

    public void userQuery() {
        Box box = HttpUtility.getBox(getRequest());

        SFUSER sfuser = new SFUSER();
        box.copyToEntity(sfuser);

        sfuser.setSESSION_ID(getRequest().getSession(true).getId());
        sfuser.setIP_ADDR(Util.getRemortIP(getRequest()));
        sfuser.setUSER_AGENT(getRequest().getHeader("User-Agent"));

        try {
            sfuser = getSpciUserService().userQuery(sfuser);
            //getRequest().getSession(true).setAttribute("LOGINED_USER", sfuser);

            getResponse().setCharacterEncoding("UTF-8");
            PrintWriter pw = getResponse().getWriter();
            pw.print(new GsonBuilder().serializeNulls().create().toJson(sfuser));
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            saveError(getRequest(), "", e);
        }

    }

    public void userOrAgentQuery() {
        Box box = HttpUtility.getBox(getRequest());

        SFUSER sfuser;
        if (getRequest().getSession(true).getAttribute("LOGINED_USER") != null) {
            sfuser = (SFUSER) getRequest().getSession(true).getAttribute("LOGINED_USER");
        } else {
            sfuser = new SFUSER();
        }
        box.copyToEntity(sfuser);

        sfuser.setAREACODE(sfuser.getCOMCODE().substring(0, 4));
        sfuser.setSESSION_ID(getRequest().getSession(true).getId());
        sfuser.setIP_ADDR(Util.getRemortIP(getRequest()));
        sfuser.setUSER_AGENT(getRequest().getHeader("User-Agent"));
        try {
            sfuser = getSpciUserService().userQuery(sfuser);

            if ("1".equals(sfuser.getERR_CD())) {
                SFMN00TB sfmn00TB = new SFMN00TB();
                sfmn00TB.setUserCode(sfuser.getUSERCODE());
                sfmn00TB.setUserName(sfuser.getUSERNAME());
                sfmn00TB.setLogonComCode(sfuser.getCOMCODE());
                sfmn00TB.setAREA_CODE(sfuser.getAREACODE());
                sfmn00TB.setRiskCode(sfuser.getRISKCODE());
                sfmn00TB.setSESSION_ID(sfuser.getSESSION_ID());
                sfmn00TB.setUSER_AGENT(sfuser.getUSER_AGENT());

                if ("0634".equals(sfmn00TB.getAREA_CODE())) {
                    if ("30130661".equals(sfuser.getUSERCODE())) {
                        sfmn00TB.setIP_ADDR("109.101.27.79");
                    } else {
                        sfmn00TB.setIP_ADDR("109.101.27.161");
                    }
                } else {
                    sfmn00TB.setIP_ADDR(sfuser.getIP_ADDR());
                }

                sfuser = getSpciUserService().userOrAgentQuery(sfuser);

                if ("1".equals(sfuser.getERR_CD())) {
                    sfmn00TB.setLogonComCode(PropertiesUtils.getLogonComCode(sfmn00TB.getAREA_CODE()));
                    sfmn00TB.setUserCode(PropertiesUtils.getUserCode(sfmn00TB.getAREA_CODE()));
                    sfmn00TB.setPassword(PropertiesUtils.getPassword(sfmn00TB.getAREA_CODE()));
                    sfmn00TB.setOPERATORCODE(sfmn00TB.getUserCode());
                    sfmn00TB.setOPERATORNAME(sfmn00TB.getUserName());

                    SFUS04TB sfus04TB = new SFUS04TB();
                    sfus04TB.setUSERCODE(sfuser.getUSERCODE());
                    sfus04TB.setUSERNAME(sfuser.getUSERNAME());
                    sfus04TB.setCOMCODE(sfuser.getCOMCODE());
                    sfus04TB.setAREACODE(sfuser.getAREACODE());
                    sfus04TB.setRISKCODE(sfuser.getRISKCODE());
                    sfus04TB.setSESSION_ID(sfuser.getSESSION_ID());
                    sfus04TB.setUSER_AGENT(sfuser.getUSER_AGENT());
                    sfus04TB.setIP_ADDR(sfuser.getIP_ADDR());
                    sfus04TB.setREFERER(getRequest().getHeader("Referer"));
                    getSpciUserService().saveSFUS04TB(sfus04TB);
                }

                SfTemporary sfTemporary = new SfTemporary();
                sfTemporary.setMain(sfmn00TB);

                getRequest().getSession(true).setAttribute("sfTemporary", sfTemporary);
                getRequest().getSession(true).setAttribute("LOGINED_USER", sfuser);
                getRequest().getSession(true).setAttribute("eBusinessToken", "SPCIAgency");
            } else {
                getSession(true).removeAttribute("sfTemporary");
                getSession(true).removeAttribute("LOGINED_USER");
                getSession(true).removeAttribute("eBusinessToken");
                getSession().invalidate();
            }

            getResponse().setCharacterEncoding("UTF-8");
            PrintWriter pw = getResponse().getWriter();
            pw.print(new GsonBuilder().serializeNulls().create().toJson(sfuser));
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            saveError(getRequest(), "", e);
        }
    }

    public String logOut() {
        getSession(true).removeAttribute("sfTemporary");
        getSession(true).removeAttribute("LOGINED_USER");
        getSession(true).removeAttribute("eBusinessToken");
        getSession().invalidate();
        return LOGIN;
    }

}
