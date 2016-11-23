package com.samsunganycar.agency;

import com.google.gson.GsonBuilder;
import com.samsunganycar.agency.common.service.SPCISearchInfoService;
import com.samsunganycar.model.*;
import com.samsunganycar.util.Box;
import com.samsunganycar.util.HttpUtility;
import com.samsunganycar.util.Util;

import java.io.PrintWriter;

public class SPCI01Action extends BaseAction {

    SPCISearchInfoService spciSearchInfoService;

    public String main() {
        Box box = HttpUtility.getBox(getRequest());
        try {
            SfTemporary sfTemporary = (SfTemporary) getRequest().getSession().getAttribute("sfTemporary");
            SFUSER sfuser = (SFUSER) getRequest().getSession().getAttribute("LOGINED_USER");
            if ("1".equals(sfuser.getERR_CD())) {
//                if (sfTemporary == null
//                        || sfTemporary.getMain() == null
//                        || sfTemporary.getMain().getUserName() == null
//                        || "".equals(sfTemporary.getMain().getUserName())) {
//                    return ERROR;
//                }
                getRequest().setAttribute("lstBusinessNature", getSpciCacheService().getBaseCodeInfoByCodeType("BusinessNature"));
                getRequest().setAttribute("lstCarCheckReason", getSpciCacheService().getBaseCodeInfoByCodeType("CarCheckReason"));
                getRequest().setAttribute("lstCarCheckStatus", getSpciCacheService().getBaseCodeInfoByCodeType("CarCheckStatus"));
                getRequest().setAttribute("lstKindCode", getSpciCacheService().getBaseCodeInfoByCodeType("KindCode"));
                getRequest().setAttribute("lstSexCode", getSpciCacheService().getBaseCodeInfoByCodeType("SexCode"));

                if ("0637".equals(sfTemporary.getMain().getAREA_CODE())) {
                    getRequest().setAttribute("lstCompany", getSpciCacheService().getBaseCodeInfoByCodeType("QingDaoCompany"));
                } else if ("0638".equals(sfTemporary.getMain().getAREA_CODE())) {
                    getRequest().setAttribute("lstCompany", getSpciCacheService().getBaseCodeInfoByCodeType("TianJinCompany"));
                } else if ("0634".equals(sfTemporary.getMain().getAREA_CODE())) {
                    getRequest().setAttribute("lstCompany", getSpciCacheService().getBaseCodeInfoByCodeType("BeiJingCompany"));
                } else if ("0635".equals(sfTemporary.getMain().getAREA_CODE())) {
                    getRequest().setAttribute("lstCompany", getSpciCacheService().getBaseCodeInfoByCodeType("ShenZhenCompany"));
                } else if ("0636".equals(sfTemporary.getMain().getAREA_CODE())) {
                    getRequest().setAttribute("lstCompany", getSpciCacheService().getBaseCodeInfoByCodeType("SuZhouCompany"));
                } else if ("0632".equals(sfTemporary.getMain().getAREA_CODE())) {
                    getRequest().setAttribute("lstCompany", getSpciCacheService().getBaseCodeInfoByCodeType("ShangHaiCompany"));
                }
                if (! "".equals(box.get("TEMPORARYNO"))) {
                    sfTemporary = getSpciSearchInfoService().getSfTemporary(box.get("TEMPORARYNO"));
                    getSession(true).setAttribute("sfTemporary", sfTemporary);
                    return SUCCESS;
                } else {
                    SFUS01TB sfus01TB = new SFUS01TB();
                    sfus01TB.setUSERCODE(sfuser.getUSERCODE());

                    sfus01TB = getSpciUserService().selectSFUS01TBByPK(sfus01TB);
                    if (sfus01TB != null && sfus01TB.getUSERCODE() != null && !"".equals(sfus01TB.getUSERCODE())) {
                        getRequest().setAttribute("SFUS01TB", sfus01TB);

                        sfTemporary.getMain().setCOMCODE(sfus01TB.getCOMCODE());
                        sfTemporary.getMain().setComName(sfus01TB.getCOMNAME());
                        sfTemporary.getMain().setHANDLER_CODE(sfus01TB.getHANDLERCODE());
                        sfTemporary.getMain().setHANDLER_NAME(sfus01TB.getHANDLERNAME());
                        sfTemporary.getMain().setBusinessNature(sfus01TB.getBUSINESSNATURE());
                        sfTemporary.getMain().setBUSINESS_NATURE(sfus01TB.getBUSINESSNATURE());
                        sfTemporary.getMain().setBusinessNatureName(sfus01TB.getBUSINESSNATURENAME());
                        sfTemporary.getMain().setAgentCode(sfus01TB.getAGENTCODE());
                        sfTemporary.getMain().setAGENT_CODE(sfus01TB.getAGENTCODE());
                        sfTemporary.getMain().setAGENT_NAME(sfus01TB.getAGENTNAME());
                        sfTemporary.getMain().setAGENTHANDLERCODE(sfus01TB.getAGENTHANDLER());
                        sfTemporary.getMain().setAGENTHANDLERNAME(sfus01TB.getAGENTHANDLERNAME());
                        sfTemporary.getMain().setAGREEMENT_NO(sfus01TB.getAGREEMENT());
                        sfTemporary.getMain().setSESSION_ID(getSession(true).getId());
                        sfTemporary.getMain().setSessionId(getSession(true).getId());

                        if (sfus01TB.getCOMCODE().startsWith("0634")) {
                            if ("30130661".equals(sfuser.getUSERCODE())) {
                                sfTemporary.getMain().setIP_ADDR("109.101.27.79");
                            } else {
                                sfTemporary.getMain().setIP_ADDR("109.101.27.161");
                            }
                        } else {
                            sfTemporary.getMain().setIP_ADDR(Util.getRemortIP(getRequest()));
                        }
                        sfTemporary.getMain().setUSER_AGENT(getRequest().getHeader("User-Agent"));

                        sfTemporary.setMain(getSpciUserService().saveSFMN00TB(sfTemporary.getMain()));
                        sfTemporary.getMain().setERR_CD("00");
                        getSession(true).setAttribute("sfTemporary", sfTemporary);

                        return SUCCESS;
                    } else {
                        return "step1";
                    }
                }
            } else {
                return LOGIN;
            }

        } catch (Exception e) {
            saveError(getRequest(), "", e);
            return ERROR;
        }
    }

    public String saveStep1() {
        Box box = HttpUtility.getBox(getRequest());
        try {
            SfTemporary sfTemporary = (SfTemporary) getSession(true).getAttribute("sfTemporary");
            SFUSER loginedUser = (SFUSER) getSession(true).getAttribute("LOGINED_USER");

            sfTemporary.getMain().setCOMCODE(box.get("COMPANYCODE"));
            sfTemporary.getMain().setComName(box.get("COMPANYNAME"));
            sfTemporary.getMain().setHANDLER_CODE(box.get("HANDLERCODE"));
            sfTemporary.getMain().setHANDLER_NAME(box.get("HANDLERNAME"));
            sfTemporary.getMain().setBusinessNature(box.get("BUSINESSNATURE"));
            sfTemporary.getMain().setBUSINESS_NATURE(box.get("BUSINESSNATURE"));
            sfTemporary.getMain().setBusinessNatureName(box.get("BUSINESSNATURENAME"));
            sfTemporary.getMain().setAgentCode(box.get("AGENTCODE"));
            sfTemporary.getMain().setAGENT_CODE(box.get("AGENTCODE"));
            sfTemporary.getMain().setAGENT_NAME(box.get("AGENTNAME"));
            sfTemporary.getMain().setAGENTHANDLERCODE(box.get("AGENTHANDLER"));
            sfTemporary.getMain().setAGENTHANDLERNAME(box.get("AGENTHANDLERNAME"));
            sfTemporary.getMain().setAGREEMENT_NO(box.get("AGREEMENT"));
            sfTemporary.getMain().setSESSION_ID(getSession(true).getId());
            sfTemporary.getMain().setSessionId(getSession(true).getId());

            if (box.get("COMPANYCODE").startsWith("0634")) {
                if ("30130661".equals(loginedUser.getUSERCODE())) {
                    sfTemporary.getMain().setIP_ADDR("109.101.27.79");
                } else {
                    sfTemporary.getMain().setIP_ADDR("109.101.27.161");
                }
            } else {
                sfTemporary.getMain().setIP_ADDR(Util.getRemortIP(getRequest()));
            }
            sfTemporary.getMain().setUSER_AGENT(getRequest().getHeader("User-Agent"));

            sfTemporary.setMain(getSpciUserService().saveSFMN00TB(sfTemporary.getMain()));
            sfTemporary.getMain().setERR_CD("00");
            getSession(true).setAttribute("sfTemporary", sfTemporary);
            getSession(true).setAttribute(sfTemporary.getMain().getTEMPORARYNO(), sfTemporary);

        } catch (Exception e) {
            saveError(getRequest(), "", e);
            return ERROR;
        }
        return SUCCESS;
    }

    public String proposal() {
        Box box = HttpUtility.getBox(getRequest());
        try {
            SFUSER sfuser = (SFUSER) getRequest().getSession().getAttribute("LOGINED_USER");

            getRequest().setAttribute("lstIdentifyType", getSpciCacheService().getBaseCodeInfoByCodeType("IdentifyType"));
            getRequest().setAttribute("lstLicenseKindCode", getSpciCacheService().getBaseCodeInfoByCodeType("LicenseKindCode"));
            getRequest().setAttribute("lstUseNature", getSpciCacheService().getBaseCodeInfoByCodeType("UseNature"));
            getRequest().setAttribute("lstCarKindPlat", getSpciCacheService().getBaseCodeInfoByCodeType("CarKindPlat"));
            getRequest().setAttribute("lstCarKindJQ", getSpciCacheService().getBaseCodeInfoByCodeType("CarKindJQ"));
            getRequest().setAttribute("lstGlassType", getSpciCacheService().getBaseCodeInfoByCodeType("GlassType"));
            getRequest().setAttribute("lstSelectMode", getSpciCacheService().getBaseCodeInfoByCodeType("SelectMode"));
            getRequest().setAttribute("lstChargeJQ", getSpciCacheService().getBaseCodeInfoByCodeType("ChargeJQ"));
            getRequest().setAttribute("lstTaxRelifFlag", getSpciCacheService().getBaseCodeInfoByCodeType("TaxRelifFlag"));
            getRequest().setAttribute("lstTaxPayerCertiType", getSpciCacheService().getBaseCodeInfoByCodeType("TaxPayerCertiType"));
            getRequest().setAttribute("lstCalculateMode", getSpciCacheService().getBaseCodeInfoByCodeType("CalculateMode"));
            getRequest().setAttribute("lstBaseTaxAtion", getSpciCacheService().getBaseCodeInfoByCodeType("BaseTaxAtion"));
            getRequest().setAttribute("lstRelifReason", getSpciCacheService().getBaseCodeInfoByCodeType("RelifReason"));
            getRequest().setAttribute("lstInsuredClassIdv", getSpciCacheService().getBaseCodeInfoByCodeType("InsuredClassIdv"));
            getRequest().setAttribute("lstInsuredClassUnit", getSpciCacheService().getBaseCodeInfoByCodeType("InsuredClassUnit"));
            getRequest().setAttribute("lstCountryCode", getSpciCacheService().getBaseCodeInfoByCodeType("CountryCode"));
            getRequest().setAttribute("lstHkFlag", getSpciCacheService().getBaseCodeInfoByCodeType("HkFlag"));
            getRequest().setAttribute("lstFuelType", getSpciCacheService().getBaseCodeInfoByCodeType("FuelType"));
            getRequest().setAttribute("lstRunMiles", getSpciCacheService().getBaseCodeInfoByCodeType("RunMiles"));
            getRequest().setAttribute("lstSpecialCarType", getSpciCacheService().getBaseCodeInfoByCodeType("SpecialCarType"));
            getRequest().setAttribute("lstVehicleCategory", getSpciCacheService().getBaseCodeInfoByCodeType("VehicleCategory"));

            SFUS02TB sfus02TB = new SFUS02TB();
            sfus02TB.setUSERCODE(sfuser.getUSERCODE());
            getRequest().setAttribute("SFUS02TB", getSpciUserService().selectSFUS02TBByUserCode(sfus02TB));

            SFUS01TB sfus01TB = new SFUS01TB();
            sfus01TB.setUSERCODE(sfuser.getUSERCODE());
            sfus01TB = getSpciUserService().selectSFUS01TBByPK(sfus01TB);
            if (sfus01TB != null && sfus01TB.getUSERCODE() != null && ! "".equals(sfus01TB.getUSERCODE())) {
                getRequest().setAttribute("SFUS01TB", sfus01TB);

                SfTemporary sfTemporary = null;
                if (! "".equals(box.get("TEMPORARYNO"))) {
                    sfTemporary = getSpciSearchInfoService().getSfTemporary(box.get("TEMPORARYNO"));
                }

                if (sfTemporary == null) {
                    SFMN00TB sfmn00TB = new SFMN00TB();
                    sfmn00TB.setUserCode(sfus01TB.getUSERCODE());
                    sfmn00TB.setUserName(sfus01TB.getUSERNAME());
                    sfmn00TB.setPassword(sfus01TB.getPASSWORD());
                    sfmn00TB.setAREA_CODE(sfus01TB.getAREACODE());
                    sfmn00TB.setBUSINESS_NATURE(sfus01TB.getBUSINESSNATURE());
                    sfmn00TB.setBusinessNature(sfus01TB.getBUSINESSNATURE());
                    sfmn00TB.setBusinessNatureName(sfus01TB.getBUSINESSNATURENAME());
                    sfmn00TB.setCOMCODE(sfus01TB.getCOMCODE());
                    sfmn00TB.setLogonComCode(sfus01TB.getLOGONCOMCODE());
                    sfmn00TB.setComName(sfus01TB.getCOMNAME());
                    sfmn00TB.setHANDLER_CODE(sfus01TB.getHANDLERCODE());
                    sfmn00TB.setHANDLER_NAME(sfus01TB.getHANDLERNAME());
                    sfmn00TB.setAGREEMENT_NO(sfus01TB.getAGREEMENT());
                    sfmn00TB.setAgentCode(sfus01TB.getAGENTCODE());
                    sfmn00TB.setAGENT_CODE(sfus01TB.getAGENTCODE());
                    sfmn00TB.setAgentName(sfus01TB.getAGENTNAME());
                    sfmn00TB.setAGENT_NAME(sfus01TB.getAGENTNAME());
                    sfmn00TB.setAGENTHANDLERCODE(sfus01TB.getAGENTHANDLER());
                    sfmn00TB.setAGENTHANDLERNAME(sfus01TB.getAGENTHANDLERNAME());
                    sfmn00TB.setOPERATORCODE(sfus01TB.getOPERATORCODE());
                    sfmn00TB.setOPERATORNAME(sfus01TB.getOPERATORNAME());
                    sfmn00TB.setIP_ADDR(Util.getRemortIP(getRequest()));
                    sfmn00TB.setUSER_AGENT(getRequest().getHeader("User-Agent"));
                    sfmn00TB.setSessionId(getSession(true).getId());
                    sfmn00TB.setSESSION_ID(getSession(true).getId());

                    sfmn00TB = getSpciCommonService().selectMaxTemporaryNo(sfmn00TB);

                    getRequest().setAttribute("SFMN00TB", sfmn00TB);
                    sfTemporary = new SfTemporary();
                    sfTemporary.setMain(sfmn00TB);
                    getSession(true).setAttribute(sfmn00TB.getTEMPORARYNO(), sfTemporary);
                } else {
                    getRequest().setAttribute("SFMN00TB", sfTemporary.getMain());
                    getSession(true).setAttribute(box.get("TEMPORARYNO"), sfTemporary);
                }
                return SUCCESS;
            } else {
                SfTemporary sfTemporary = (SfTemporary) getSession().getAttribute("sfTemporary");
                getRequest().setAttribute("SFMN00TB", sfTemporary.getMain());
                getSession(true).setAttribute(box.get("TEMPORARYNO"), sfTemporary);

                return SUCCESS;
            }
        } catch (Exception e) {
            saveError(getRequest(), "", e);
            return ERROR;
        }
    }

    public SPCISearchInfoService getSpciSearchInfoService() {
        return spciSearchInfoService;
    }

    public void setSpciSearchInfoService(SPCISearchInfoService spciSearchInfoService) {
        this.spciSearchInfoService = spciSearchInfoService;
    }
}
