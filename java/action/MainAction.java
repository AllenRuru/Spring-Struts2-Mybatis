package com.samsunganycar.agency;

import com.google.gson.GsonBuilder;
import com.samsunganycar.model.FC_VEHICLE;
import com.samsunganycar.model.SFCD01TB;
import com.samsunganycar.model.SFUSER;
import com.samsunganycar.model.SfTemporary;
import com.samsunganycar.util.Box;
import com.samsunganycar.util.HttpUtility;
import com.sinosoft.prpall.service.dto.clauseQuery.ClauseInfo;

import java.io.PrintWriter;
import java.util.List;

public class MainAction extends BaseAction {

    public String main() {

        try {
            SfTemporary sfTemporary = (SfTemporary) getRequest().getSession().getAttribute("sfTemporary");
            SFUSER sfuser = (SFUSER) getRequest().getSession().getAttribute("LOGINED_USER");

            if ("1".equals(sfuser.getERR_CD())) {
                if (sfTemporary == null
                        || sfTemporary.getMain() == null
                        || sfTemporary.getMain().getUserName() == null
                        || "".equals(sfTemporary.getMain().getUserName())) {
                    return ERROR;
                }

            } else {
                return LOGIN;
            }

            String areaName = "";
            if ("0632".equals(sfuser.getAREACODE())) {
                areaName = "上海";
            } else if ("0634".equals(sfuser.getAREACODE())) {
                areaName = "北京";
            } else if ("0635".equals(sfuser.getAREACODE())) {
                areaName = "深圳";
            } else if ("0636".equals(sfuser.getAREACODE())) {
                areaName = "苏州";
            } else if ("0637".equals(sfuser.getAREACODE())) {
                areaName = "青岛";
            } else if ("0638".equals(sfuser.getAREACODE())) {
                areaName = "天津";
            }
            getRequest().setAttribute("AREANAME", areaName);

        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    public void selectHandler() {
        try {
            SFUSER loginedUser = (SFUSER) getSession(true).getAttribute("LOGINED_USER");
            Box box = HttpUtility.getBox(getRequest());
            loginedUser.setCOMCODE(box.get("COMPANYCODE"));

            List<SFCD01TB> lstSFCD01TB = getSpciUserService().comCodeHandler(loginedUser);

            getResponse().setCharacterEncoding("UTF-8");
            PrintWriter pw = getResponse().getWriter();
            pw.print(new GsonBuilder().serializeNulls().create().toJson(lstSFCD01TB));
            pw.flush();
            pw.close();

        } catch (Exception e) {
            saveError(getRequest(), "", e);
            e.printStackTrace();
        }
    }

    public void selectAgent() {
        try {
            SFUSER loginedUser = (SFUSER) getSession(true).getAttribute("LOGINED_USER");

            Box box = HttpUtility.getBox(getRequest());

            SFUSER sfuser = new SFUSER();
            sfuser.setLOGONCOMCODE(loginedUser.getLOGONCOMCODE());
            sfuser.setCOMCODE(box.get("COMPANYCODE"));
            sfuser.setHANDLERCODE(box.get("HANDLERCODE"));
            sfuser.setAREACODE(loginedUser.getAREACODE());
            sfuser.setUSERCODE(loginedUser.getUSERCODE());
            sfuser.setUSERNAME(loginedUser.getUSERNAME());
            sfuser.setPASSWORD(loginedUser.getPASSWORD());
            sfuser.setRISKCODE(loginedUser.getRISKCODE());

            List<SFCD01TB> lstSFCD01TB = getSpciUserService().comCodeAgent(sfuser);

            getResponse().setCharacterEncoding("UTF-8");
            PrintWriter pw = getResponse().getWriter();
            pw.print(new GsonBuilder().serializeNulls().create().toJson(lstSFCD01TB));
            pw.flush();
            pw.close();

        } catch (Exception e) {
            saveError(getRequest(), "", e);
            e.printStackTrace();
        }
    }

    public void selectAgentHandler() {
        try {
            SFUSER loginedUser = (SFUSER) getSession(true).getAttribute("LOGINED_USER");

            Box box = HttpUtility.getBox(getRequest());

            SFUSER sfuser = new SFUSER();
            sfuser.setLOGONCOMCODE(loginedUser.getLOGONCOMCODE());
            sfuser.setCOMCODE(box.get("COMPANYCODE"));
            sfuser.setHANDLERCODE(box.get("HANDLERCODE"));
            sfuser.setAGENTCODE(box.get("AGENTCODE"));
            sfuser.setAREACODE(loginedUser.getAREACODE());
            sfuser.setUSERCODE(loginedUser.getUSERCODE());
            sfuser.setUSERNAME(loginedUser.getUSERNAME());
            sfuser.setPASSWORD(loginedUser.getPASSWORD());
            sfuser.setRISKCODE(loginedUser.getRISKCODE());

            List<SFCD01TB> lstSFCD01TB = getSpciUserService().selectAgentHandler(sfuser);

            getResponse().setCharacterEncoding("UTF-8");
            PrintWriter pw = getResponse().getWriter();
            pw.print(new GsonBuilder().serializeNulls().create().toJson(lstSFCD01TB));
            pw.flush();
            pw.close();
        } catch (Exception e) {
            saveError(getRequest(), "", e);
            e.printStackTrace();
        }
    }

    public void vehicleName() {
        try {
            Box box = HttpUtility.getBox(getRequest());
            FC_VEHICLE fcVehicle = new FC_VEHICLE();
            box.copyToEntity(fcVehicle);

            List<FC_VEHICLE> lstFC_VEHICLE = getSpciCommonService().selectFC_VEHICLEByVEHICLE_NAME(fcVehicle);

            getResponse().setCharacterEncoding("UTF-8");
            PrintWriter pw = getResponse().getWriter();
            pw.print(new GsonBuilder().serializeNulls().create().toJson(lstFC_VEHICLE));
            pw.flush();
            pw.close();
        } catch (Exception e) {
            saveError(getRequest(), "", e);
            e.printStackTrace();
        }
    }

    public void engageList() {
        try {
            Box box = HttpUtility.getBox(getRequest());
            SfTemporary sfTemporary = (SfTemporary) getSession(true).getAttribute(box.get("TEMPORARYNO"));
            List<ClauseInfo> lstEngage = getSpciCommonService().engageList(sfTemporary.getMain());

            getResponse().setCharacterEncoding("UTF-8");
            PrintWriter pw = getResponse().getWriter();
            pw.print(new GsonBuilder().serializeNulls().create().toJson(lstEngage));
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
