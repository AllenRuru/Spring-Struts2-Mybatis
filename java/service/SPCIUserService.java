package com.samsunganycar.agency.common.service;

import com.samsunganycar.model.*;

import java.util.List;
import java.util.Map;

public interface SPCIUserService {
    public SFUSER userQuery(SFUSER sfuser) throws Exception;
    public SFUSER userOrAgentQuery(SFUSER sfuser) throws Exception;
    public List<SFCD01TB> comCodeHandler(SFUSER sfuser) throws Exception;
    public List<SFCD01TB> comCodeAgent(SFUSER sfuser) throws Exception;
    public List<SFCD01TB> selectAgentHandler(SFUSER sfuser) throws Exception;
    public SFMN00TB saveSFMN00TB(SFMN00TB sfmn00TB) throws Exception;
    public SFUS01TB selectSFUS01TBByPK(SFUS01TB sfus01TB) throws Exception;
    public SFUS01TB saveSFUS01TB(SFUS01TB sfus01TB) throws Exception;
    public SFUS01TB deleteSFUS01TB(SFUS01TB sfus01TB) throws Exception;
    public List<SFUS02TB> selectSFUS02TBByUserCode(SFUS02TB sfus02TB) throws Exception;
    public Map<String, Object> procSFUS02TB(Map<String, Object> mapReturn) throws Exception;
    public List<SFUS03TB> selectSFUS03TBByPk(SFUS03TB sfus03TB) throws Exception;
    public void deleteSFUS02TB(SFUS02TB sfus02TB) throws Exception;
    public void saveSFUS04TB(SFUS04TB sfus04TB) throws Exception;
}
