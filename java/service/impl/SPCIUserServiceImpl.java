package com.samsunganycar.agency.common.service.impl;

import com.google.gson.GsonBuilder;
import com.samsunganycar.agency.common.service.SPCICommonService;
import com.samsunganycar.agency.common.service.SPCIUserService;
import com.samsunganycar.model.*;
import com.samsunganycar.util.Constants;
import com.samsunganycar.util.PropertiesUtils;
import com.sinosoft.prpall.service.component.useroragentquery.UserOrAgentQueryComponent;
import com.sinosoft.prpall.service.component.useroragentquery.UserOrAgentQueryComponentProxy;
import com.sinosoft.prpall.service.component.userquery.UserQueryComponent;
import com.sinosoft.prpall.service.component.userquery.UserQueryComponentProxy;
import com.sinosoft.prpall.service.dto.common.RequestHeadDto;
import com.sinosoft.prpall.service.dto.useroragentquery.ComCodeHandlerInputDto;
import com.sinosoft.prpall.service.dto.useroragentquery.UserOrAgentQueryRequest;
import com.sinosoft.prpall.service.dto.useroragentquery.UserOrAgentQueryRequestBody;
import com.sinosoft.prpall.service.dto.useroragentquery.UserOrAgentQueryResponse;
import com.sinosoft.prpall.service.dto.userquery.UserQueryInputDto;
import com.sinosoft.prpall.service.dto.userquery.UserQueryRequest;
import com.sinosoft.prpall.service.dto.userquery.UserQueryRequestBody;
import com.sinosoft.prpall.service.dto.userquery.UserQueryResponse;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SPCIUserServiceImpl implements SPCIUserService {

    SPCICommonService spciCommonService;
    private SqlSession sqlSession;

    public SFUSER userQuery(SFUSER sfuser) throws Exception {
        UserQueryComponentProxy proxy = new UserQueryComponentProxy();
        UserQueryComponent component = proxy.getUserQueryComponent();
        UserQueryRequest request = new UserQueryRequest();

        RequestHeadDto head = new RequestHeadDto();
        UserQueryRequestBody body = new UserQueryRequestBody();
        head.setLogonComCode(PropertiesUtils.getLogonComCode(sfuser.getAREACODE()));
        head.setUserCode(PropertiesUtils.getUserCode(sfuser.getAREACODE()));
        head.setPassword(PropertiesUtils.getPassword(sfuser.getAREACODE()));
        head.setRequestType(Constants.RequestType.UserQuery);
        head.setRiskCode(PropertiesUtils.getRiskCode());
        head.setSystemCode(PropertiesUtils.getSystemCode());
        request.setHead(head);

        UserQueryInputDto userQueryInputDto = new UserQueryInputDto();
        userQueryInputDto.setUserCode(sfuser.getUSERCODE());
        body.setUser(userQueryInputDto);
        request.setBody(body);

        System.out.println(">>>>用户查询入参====" + new GsonBuilder().serializeNulls().create().toJson(request));
        UserQueryResponse response = component.query(request);
        System.out.println(">>>>用户查询出参====" + new GsonBuilder().serializeNulls().create().toJson(response));

        sfuser.setERR_CD(response.getHead().getAppResCode());
        sfuser.setERR_MSG(response.getHead().getAppResMessage());
        if ("1".equals(response.getHead().getAppResCode())) {
            sfuser.setIDENTIFYNUMBER(response.getBody().getIdentifyNumber());
            if (response.getBody().getUsers().length > 0) {
                sfuser.setCOMCODE(response.getBody().getUsers()[0].getComCode());
                sfuser.setAREACODE(sfuser.getCOMCODE().substring(0, 4));
                sfuser.setEMAIL(response.getBody().getUsers()[0].getEmail());
                sfuser.setMOBILE(response.getBody().getUsers()[0].getMobile());
                sfuser.setUSERNAME(response.getBody().getUsers()[0].getUserName());
                sfuser.setRISKCODE(PropertiesUtils.getRiskCode());

            }
        } else {
            SFER01TB sfer01TB = new SFER01TB();
            sfer01TB.setCOREERR_MSG(response.getHead().getAppResMessage());
            sfer01TB.setSESSION_ID(sfuser.getSESSION_ID());
            sfer01TB.setIP_ADDR(sfuser.getIP_ADDR());
            sfer01TB.setUSER_AGENT(sfuser.getUSER_AGENT());
            sfer01TB.setIN_JSON(new GsonBuilder().serializeNulls().create().toJson(request));
            sfer01TB.setOUT_JSON(new GsonBuilder().serializeNulls().create().toJson(response));
            spciCommonService.saveError(sfer01TB);
        }

        return sfuser;
    }

    public SFUSER userOrAgentQuery(SFUSER sfuser) throws Exception {
        UserOrAgentQueryComponentProxy proxy = new UserOrAgentQueryComponentProxy();
        UserOrAgentQueryComponent component = proxy.getUserOrAgentQueryComponent();
        UserOrAgentQueryRequest request = new UserOrAgentQueryRequest();

        RequestHeadDto head = new RequestHeadDto();
        UserOrAgentQueryRequestBody body = new UserOrAgentQueryRequestBody();
//        head.setLogonComCode(PropertiesUtils.getLogonComCode(sfuser.getAREACODE()));
//        head.setUserCode(PropertiesUtils.getUserCode(sfuser.getAREACODE()));
//        head.setPassword(PropertiesUtils.getPassword(sfuser.getAREACODE()));
        head.setLogonComCode(sfuser.getCOMCODE());
        head.setUserCode(sfuser.getUSERCODE());
        head.setPassword(sfuser.getPASSWORD());
        head.setRequestType(Constants.RequestType.UserOrAgentQuery);
        head.setRiskCode(sfuser.getRISKCODE());
        head.setSystemCode(PropertiesUtils.getSystemCode());
        request.setHead(head);

        ComCodeHandlerInputDto inputDto = new ComCodeHandlerInputDto();
        inputDto.setQueryType(Constants.QueryType.QueryType_3);
        inputDto.setComCode(sfuser.getCOMCODE());
        inputDto.setUserCode(sfuser.getUSERCODE());
        body.setComCodeHandlerInputDto(inputDto);
        request.setBody(body);

        System.out.println(">>>>用户或者代理点查询入参====" + new GsonBuilder().serializeNulls().create().toJson(request));
        UserOrAgentQueryResponse response = component.query(request);
        System.out.println(">>>>用户或者代理点查询出参====" + new GsonBuilder().serializeNulls().create().toJson(response));

        if ("1".equals(response.getHead().getResponseCode())) {
            sfuser.setERR_CD(response.getHead().getResponseCode());
            if ("1".equals(response.getHead().getAppResCode())) {
                sfuser.setAGENTCODE(response.getBody().getComCodeHandlerOutputDtos()[0].getAgentCode());
                sfuser.setHANDLERCODE(response.getBody().getComCodeHandlerOutputDtos()[0].getHandlerCode());
                sfuser.setBUSINESSNATURE(response.getBody().getComCodeHandlerOutputDtos()[0].getBusinessNature());
//
//            } else {
//                sfuser.setERR_CD(response.getHead().getAppResCode());
//                sfuser.setERR_MSG(response.getHead().getAppResMessage());
//
//                SFER01TB sfer01TB = new SFER01TB();
//                sfer01TB.setCOREERR_MSG(response.getHead().getAppResMessage());
//                sfer01TB.setSESSION_ID(sfuser.getSESSION_ID());
//                sfer01TB.setIP_ADDR(sfuser.getIP_ADDR());
//                sfer01TB.setUSER_AGENT(sfuser.getUSER_AGENT());
//                sfer01TB.setIN_JSON(new GsonBuilder().serializeNulls().create().toJson(request));
//                sfer01TB.setOUT_JSON(new GsonBuilder().serializeNulls().create().toJson(response));
//                spciCommonService.saveError(sfer01TB);
            }
        } else {
            sfuser.setERR_CD(response.getHead().getAppResCode());
            sfuser.setERR_MSG(response.getHead().getAppResMessage());

            SFER01TB sfer01TB = new SFER01TB();
            sfer01TB.setCOREERR_MSG(response.getHead().getAppResMessage());
            sfer01TB.setSESSION_ID(sfuser.getSESSION_ID());
            sfer01TB.setIP_ADDR(sfuser.getIP_ADDR());
            sfer01TB.setUSER_AGENT(sfuser.getUSER_AGENT());
            sfer01TB.setIN_JSON(new GsonBuilder().serializeNulls().create().toJson(request));
            sfer01TB.setOUT_JSON(new GsonBuilder().serializeNulls().create().toJson(response));
            spciCommonService.saveError(sfer01TB);
        }

        return sfuser;
    }

    public List<SFCD01TB> comCodeHandler(SFUSER sfuser) throws Exception {
        List<SFCD01TB> lstSFCD01TB = new ArrayList<SFCD01TB>();

        UserOrAgentQueryComponentProxy proxy = new UserOrAgentQueryComponentProxy();
        UserOrAgentQueryComponent component = proxy.getUserOrAgentQueryComponent();
        UserOrAgentQueryRequest request = new UserOrAgentQueryRequest();
        RequestHeadDto head = new RequestHeadDto();
        UserOrAgentQueryRequestBody body = new UserOrAgentQueryRequestBody();

        head.setLogonComCode(PropertiesUtils.getLogonComCode(sfuser.getAREACODE()));
        head.setUserCode(PropertiesUtils.getUserCode(sfuser.getAREACODE()));
        head.setPassword(PropertiesUtils.getPassword(sfuser.getAREACODE()));
        head.setRequestType(Constants.RequestType.UserOrAgentQuery);
        head.setRiskCode(PropertiesUtils.getRiskCode());
        head.setSystemCode(PropertiesUtils.getSystemCode());
        request.setHead(head);

        ComCodeHandlerInputDto inputDto = new ComCodeHandlerInputDto();
        inputDto.setQueryType(Constants.QueryType.QueryType_1);
        inputDto.setComCode(sfuser.getCOMCODE());
        inputDto.setUserCode(sfuser.getUSERCODE());
//        inputDto.setAgentCode(sfuser.getAGENTCODE());
        body.setComCodeHandlerInputDto(inputDto);
        request.setBody(body);

        System.out.println(">>>>comCodeHandler入参====" + new GsonBuilder().serializeNulls().create().toJson(request));
        UserOrAgentQueryResponse response = component.query(request);
        System.out.println(">>>>comCodeHandler出参====" + new GsonBuilder().serializeNulls().create().toJson(response));

        if (! "1".equals(response.getHead().getAppResCode())) {
            SFER01TB sfer01TB = new SFER01TB();
            sfer01TB.setCOREERR_MSG(response.getHead().getAppResMessage());
            sfer01TB.setSESSION_ID(sfuser.getSESSION_ID());
            sfer01TB.setIP_ADDR(sfuser.getIP_ADDR());
            sfer01TB.setUSER_AGENT(sfuser.getUSER_AGENT());
            sfer01TB.setIN_JSON(new GsonBuilder().serializeNulls().create().toJson(request));
            sfer01TB.setOUT_JSON(new GsonBuilder().serializeNulls().create().toJson(response));
            spciCommonService.saveError(sfer01TB);
        } else {
            SFCD01TB sfcd01TB;
            for (int i = 0 ; response.getBody().getComCodeHandlerOutputDtos() != null && i < response.getBody().getComCodeHandlerOutputDtos().length ; i++) {
                sfcd01TB = new SFCD01TB();
                sfcd01TB.setCODE_ID(response.getBody().getComCodeHandlerOutputDtos()[i].getHandlerCode());
                sfcd01TB.setCODE_NAME(response.getBody().getComCodeHandlerOutputDtos()[i].getHandlerName());

                lstSFCD01TB.add(sfcd01TB);
            }
        }
        return lstSFCD01TB;
    }

    public List<SFCD01TB> comCodeAgent(SFUSER sfuser) throws Exception {
        List<SFCD01TB> lstSFCD01TB = new ArrayList<SFCD01TB>();

        UserOrAgentQueryComponentProxy proxy = new UserOrAgentQueryComponentProxy();
        UserOrAgentQueryComponent component = proxy.getUserOrAgentQueryComponent();
        UserOrAgentQueryRequest request = new UserOrAgentQueryRequest();
        RequestHeadDto head = new RequestHeadDto();
        UserOrAgentQueryRequestBody body = new UserOrAgentQueryRequestBody();

        head.setLogonComCode(PropertiesUtils.getLogonComCode(sfuser.getAREACODE()));
        head.setUserCode(PropertiesUtils.getUserCode(sfuser.getAREACODE()));
        head.setPassword(PropertiesUtils.getPassword(sfuser.getAREACODE()));
        head.setRequestType(Constants.RequestType.UserOrAgentQuery);
        head.setRiskCode(PropertiesUtils.getRiskCode());
        head.setSystemCode(PropertiesUtils.getSystemCode());
        request.setHead(head);

        ComCodeHandlerInputDto inputDto = new ComCodeHandlerInputDto();
        inputDto.setQueryType(Constants.QueryType.QueryType_3);
        inputDto.setComCode(sfuser.getCOMCODE());
        inputDto.setUserCode(sfuser.getUSERCODE());
        inputDto.setHandlerCode(sfuser.getHANDLERCODE());
        body.setComCodeHandlerInputDto(inputDto);
        request.setBody(body);

        System.out.println(">>>>comCodeAgent入参====" + new GsonBuilder().serializeNulls().create().toJson(request));
        UserOrAgentQueryResponse response = component.query(request);
        System.out.println(">>>>comCodeAgent出参====" + new GsonBuilder().serializeNulls().create().toJson(response));

        if (! "1".equals(response.getHead().getAppResCode())) {
            SFER01TB sfer01TB = new SFER01TB();
            sfer01TB.setCOREERR_MSG(response.getHead().getAppResMessage());
            sfer01TB.setSESSION_ID(sfuser.getSESSION_ID());
            sfer01TB.setIP_ADDR(sfuser.getIP_ADDR());
            sfer01TB.setUSER_AGENT(sfuser.getUSER_AGENT());
            sfer01TB.setIN_JSON(new GsonBuilder().serializeNulls().create().toJson(request));
            sfer01TB.setOUT_JSON(new GsonBuilder().serializeNulls().create().toJson(response));
            spciCommonService.saveError(sfer01TB);
        } else {
            SFCD01TB sfcd01TB;
            for (int i = 0 ; response.getBody().getComCodeHandlerOutputDtos() != null && i < response.getBody().getComCodeHandlerOutputDtos().length ; i++) {
                sfcd01TB = new SFCD01TB();
                sfcd01TB.setCODE_ID(response.getBody().getComCodeHandlerOutputDtos()[i].getAgentCode());
                sfcd01TB.setCODE_NAME(response.getBody().getComCodeHandlerOutputDtos()[i].getAgentName());
                sfcd01TB.setOPTION01(response.getBody().getComCodeHandlerOutputDtos()[i].getBusinessNature());

                lstSFCD01TB.add(sfcd01TB);
            }
        }
        return lstSFCD01TB;
    }

    public List<SFCD01TB> selectAgentHandler(SFUSER sfuser) throws Exception {
        List<SFCD01TB> lstSFCD01TB = new ArrayList<SFCD01TB>();

        UserOrAgentQueryComponentProxy proxy = new UserOrAgentQueryComponentProxy();
        UserOrAgentQueryComponent component = proxy.getUserOrAgentQueryComponent();
        UserOrAgentQueryRequest request = new UserOrAgentQueryRequest();
        RequestHeadDto head = new RequestHeadDto();
        UserOrAgentQueryRequestBody body = new UserOrAgentQueryRequestBody();

        head.setLogonComCode(PropertiesUtils.getLogonComCode(sfuser.getAREACODE()));
        head.setUserCode(PropertiesUtils.getUserCode(sfuser.getAREACODE()));
        head.setPassword(PropertiesUtils.getPassword(sfuser.getAREACODE()));
        head.setRequestType(Constants.RequestType.UserOrAgentQuery);
        head.setRiskCode(PropertiesUtils.getRiskCode());
        head.setSystemCode(PropertiesUtils.getSystemCode());
        request.setHead(head);

        ComCodeHandlerInputDto inputDto = new ComCodeHandlerInputDto();
        inputDto.setQueryType(Constants.QueryType.QueryType_2);
        inputDto.setComCode(sfuser.getCOMCODE());
        inputDto.setUserCode(sfuser.getUSERCODE());
        inputDto.setHandlerCode(sfuser.getHANDLERCODE());
        inputDto.setAgentCode(sfuser.getAGENTCODE());
        body.setComCodeHandlerInputDto(inputDto);
        request.setBody(body);

        System.out.println(">>>>comCodeAgentHandler入参====" + new GsonBuilder().serializeNulls().create().toJson(request));
        UserOrAgentQueryResponse response = component.query(request);
        System.out.println(">>>>comCodeAgentHandler出参====" + new GsonBuilder().serializeNulls().create().toJson(response));

        if (! "1".equals(response.getHead().getAppResCode())) {
            SFER01TB sfer01TB = new SFER01TB();
            sfer01TB.setCOREERR_MSG(response.getHead().getAppResMessage());
            sfer01TB.setSESSION_ID(sfuser.getSESSION_ID());
            sfer01TB.setIP_ADDR(sfuser.getIP_ADDR());
            sfer01TB.setUSER_AGENT(sfuser.getUSER_AGENT());
            sfer01TB.setIN_JSON(new GsonBuilder().serializeNulls().create().toJson(request));
            sfer01TB.setOUT_JSON(new GsonBuilder().serializeNulls().create().toJson(response));
            spciCommonService.saveError(sfer01TB);
        } else {
            SFCD01TB sfcd01TB;
            for (int i = 0 ; response.getBody().getComCodeHandlerOutputDtos() != null && i < response.getBody().getComCodeHandlerOutputDtos().length ; i++) {
                sfcd01TB = new SFCD01TB();
                sfcd01TB.setCODE_ID(response.getBody().getComCodeHandlerOutputDtos()[i].getAgentHandlerCode());
                sfcd01TB.setCODE_NAME(response.getBody().getComCodeHandlerOutputDtos()[i].getAgentHandlerName());
                sfcd01TB.setOPTION01(response.getBody().getComCodeHandlerOutputDtos()[i].getAgreementno());

                lstSFCD01TB.add(sfcd01TB);
            }
        }
        return lstSFCD01TB;
    }

    @Transactional
    public SFMN00TB saveSFMN00TB(SFMN00TB sfmn00TB) throws Exception {

        String temporaryNo = "";
        if (sfmn00TB.getTEMPORARYNO() == null || "".equals(sfmn00TB.getTEMPORARYNO())) {
            temporaryNo = sqlSession.selectOne("SPCI.selectNewTemporaryNo", sfmn00TB);
        } else {
            temporaryNo = sfmn00TB.getTEMPORARYNO();
        }
        sfmn00TB.setTEMPORARYNO(temporaryNo);

        sqlSession.update("SPCI.saveSFMN00TB", sfmn00TB);
        return sfmn00TB;
    }

    public SFUS01TB selectSFUS01TBByPK(SFUS01TB sfus01TB) throws Exception {
        return sqlSession.selectOne("SPCI.selectSFUS01TBByPK", sfus01TB);
    }

    @Transactional
    public SFUS01TB saveSFUS01TB(SFUS01TB sfus01TB) throws Exception {
        try {
            sfus01TB.setERR_CD("00");
            sqlSession.update("SPCI.saveSFUS01TB", sfus01TB);
        } catch (Exception e) {
            sfus01TB.setERR_CD("99");
            sfus01TB.setERR_MSG(e.toString());
            e.printStackTrace();
        }
        return sfus01TB;
    }

    @Transactional
    public SFUS01TB deleteSFUS01TB(SFUS01TB sfus01TB) throws Exception {
        try {
            sfus01TB.setERR_CD("00");
            sqlSession.delete("SPCI.deleteSFUS01TB", sfus01TB);
        } catch (Exception e) {
            sfus01TB.setERR_CD("99");
            sfus01TB.setERR_MSG(e.toString());
            e.printStackTrace();
        }
        return sfus01TB;
    }

    public List<SFUS02TB> selectSFUS02TBByUserCode(SFUS02TB sfus02TB) throws Exception {
        return sqlSession.selectList("SPCI.selectSFUS02TBByUserCode", sfus02TB);
    }

    @Transactional
    public Map<String, Object> procSFUS02TB(Map<String, Object> mapReturn) throws Exception {
        SFUS02TB sfus02TB = (SFUS02TB) mapReturn.get("SFUS02TB");
        List<SFUS03TB> lstSFUS03TB = (List<SFUS03TB>) mapReturn.get("lstSFUS03TB");

        String quotationSeq = sfus02TB.getQUOTATION_SEQ();
        if ("UserDefinedAdd".equals(quotationSeq)) {
            quotationSeq = Integer.toString((Integer) sqlSession.selectOne("SPCI.sequenceSFUS02TB"));
            sfus02TB.setQUOTATION_SEQ(quotationSeq);

            for (int i = 0 ; lstSFUS03TB != null && i < lstSFUS03TB.size() ; i++) {
                lstSFUS03TB.get(i).setQUOTATION_SEQ(quotationSeq);
            }
        }

        sqlSession.update("SPCI.procSFUS02TB", sfus02TB);
        for (int i = 0 ; lstSFUS03TB != null && i < lstSFUS03TB.size() ; i++) {
            sqlSession.update("SPCI.procSFUS03TB", lstSFUS03TB.get(i));
        }

        mapReturn.put("SFUS02TB", sfus02TB);
        mapReturn.put("lstSFUS03TB", lstSFUS03TB);

        return mapReturn;
    }

    public List<SFUS03TB> selectSFUS03TBByPk(SFUS03TB sfus03TB) throws Exception {
        return sqlSession.selectList("SPCI.selectSFUS03TBByPk", sfus03TB);
    }

    public void deleteSFUS02TB(SFUS02TB sfus02TB) throws Exception {
        sqlSession.update("SPCI.deleteSFUS02TB", sfus02TB);
    }

    public void saveSFUS04TB(SFUS04TB sfus04TB) throws Exception {
        sqlSession.insert("SPCI.saveSFUS04TB", sfus04TB);
    }

    public SPCICommonService getSpciCommonService() {
        return spciCommonService;
    }

    public void setSpciCommonService(SPCICommonService spciCommonService) {
        this.spciCommonService = spciCommonService;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
}
