package com.samsunganycar.agency;

import com.opensymphony.xwork2.ActionSupport;
import com.samsunganycar.agency.common.service.SPCICacheService;
import com.samsunganycar.agency.common.service.SPCICommonService;
import com.samsunganycar.agency.common.service.SPCIUserService;
import com.samsunganycar.model.SFER01TB;
import com.samsunganycar.util.Util;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BaseAction extends ActionSupport {

    private SPCICacheService spciCacheService;
    private SPCICommonService spciCommonService;
    private SPCIUserService spciUserService;

    public void saveError(HttpServletRequest req, String errCode, Exception e) {
        SFER01TB sfer01TB = new SFER01TB();
        sfer01TB.setTEMPORARYNO((String) req.getSession(true).getAttribute("TEMPORARYNO"));
        sfer01TB.setSESSION_ID(req.getSession(true).getId());
        sfer01TB.setIP_ADDR(Util.getRemortIP(req));
        sfer01TB.setUSER_AGENT(req.getHeader("User-Agent"));
        sfer01TB.setERR_CD(errCode);

        StringBuffer sb = new StringBuffer();
        try {
            StackTraceElement element[] = e.getStackTrace();
            for (StackTraceElement anElement : element) {
                sb.append(anElement.toString());
                sb.append("\n");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        e.printStackTrace();

        sfer01TB.setSYSERR_MSG(sb.toString());
        spciCommonService.saveError(sfer01TB);
    }

    protected HttpServletRequest getRequest() {
            return ServletActionContext.getRequest();
        }

    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected HttpSession getSession(boolean flag) {
        return getRequest().getSession(flag);
    }

    public SPCICacheService getSpciCacheService() {
        return spciCacheService;
    }

    public void setSpciCacheService(SPCICacheService spciCacheService) {
        this.spciCacheService = spciCacheService;
    }

    public SPCIUserService getSpciUserService() {
        return spciUserService;
    }

    public void setSpciUserService(SPCIUserService spciUserService) {
        this.spciUserService = spciUserService;
    }

    public SPCICommonService getSpciCommonService() {
        return spciCommonService;
    }

    public void setSpciCommonService(SPCICommonService spciCommonService) {
        this.spciCommonService = spciCommonService;
    }
}
