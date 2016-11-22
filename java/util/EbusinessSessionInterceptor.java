package com.samsunganycar.util;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EbusinessSessionInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	 private static Logger logger = LoggerFactory.getLogger(EbusinessSessionInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(true);
		String eBusinessToken = (String) session.getAttribute("eBusinessToken");
		if (eBusinessToken == null) {
			String header = request.getHeader("x-requested-with");
			if (header != null && "XMLHttpRequest".equalsIgnoreCase(header)) {
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setHeader("sessionStatus", "timeout");
                throw new Exception("操作超时");
			} else {
				return "eBusinessRedirect";
			}
		}
		return invocation.invoke();
	}

}

