<%@ page import="com.lgeds.jdf.servlet.Box" %>
<%@ page import="com.lgeds.jdf.servlet.HttpUtility" %>
<%@ page import="com.sdsc.core.util.Util" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp"%>

<%
response.setHeader("Progma", "no-cache");
response.setHeader("Cache-Control", "must-revalidate");
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache-Control", "no-stroe");
response.setHeader("Expires", "0");

String flag = Util.replaceSpecialCharater(request.getParameter("param"));
String keyId = Util.replaceSpecialCharater(request.getParameter("keyId"));
String ipuid = Util.replaceSpecialCharater(request.getParameter("ipuid"));
String veriNo = Util.replaceSpecialCharater(request.getParameter("veriNo"));
String source = Util.replaceSpecialCharater(request.getParameter("source"));
String weiboCid = Util.replaceSpecialCharater(request.getParameter("cid"));
String weiboViewer = Util.replaceSpecialCharater(request.getParameter("viewer"));
String weiboSubAppkey = Util.replaceSpecialCharater(request.getParameter("sub_appkey"));
String weiboAccessToken = Util.replaceSpecialCharater(request.getParameter("access_token"));
String area = Util.replaceSpecialCharater(request.getParameter("area"));
if (area == null || "".equals(area)) {
    area = "021";
}
%>
<html>
<head>
	<title>三星直销车险网上投保系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <script type="text/javascript">
        if (top != self) top.location = self.location;  //XFS

        function createRequestObject() {
            var req;
            if (window.XMLHttpRequest){
                req = new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } else {
                var url = contextRootPath + '/browser/checkbrowser.do?windowsXPOr2003=' + isWindowsXPOr2003();
                location.href = url;
                return;
            }
            return req;
        }
        var http = createRequestObject();

        function isMobileBrowser() {
        	if (navigator.appVersion.toLowerCase().indexOf("mobile") >= 0
            || navigator.appVersion.toLowerCase().indexOf("android") >= 0){
            	return true;
            } else {
            	return false;
            }
        }
        function sendRequest(page, params, type) {
            http.open('POST', page, true);
            http.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            http.setRequestHeader("Pragma", "no-cache");
            if (type == "baseInitStatic") {
                http.onreadystatechange = handleResponse;
            } else if (type == "keyIdForward") {
                http.onreadystatechange = handleResponseForword;
            } 
            http.send(params);
        }
        function handleResponse() {
            if(http.readyState == 4 && http.status == 200){
                var anyForm = document.getElementById('anyForm');
                anyForm.method = "POST";

                if (isMobileBrowser()) {
                    //anyForm.action = "http://m.samsunganycar.com/web/m/init.do";   // 正式
                    anyForm.action = "/web/m/init.do";  // 测试
                    anyForm.submit();
                } else {
	                if ("<%=veriNo%>" != "") {
	                    anyForm.action = contextRootPath + "/basicinfo/initTM.do";
	                    anyForm.submit();
	                } else {
	                    anyForm.action = contextRootPath + "/basicinfo/init.do";
	                    anyForm.submit();
	                }
                }
            }
        }
        function handleResponseForword() {
            if(http.readyState == 4 && http.status == 200){
                var forwardURL = http.responseText;
                if (forwardURL != "") {
                    var anyForm = document.getElementById('anyForm');
                    anyForm.method = "POST";
                    if (isMobileBrowser()) {
                    	if(forwardURL.indexOf('Event') >= 0){
                    		anyForm.action = "http://m.samsunganycar.com" + forwardURL + "&keyId=<%=keyId%>";  // 正式
		                    //location.href = forwardURL + "&keyId=<%=keyId%>";  // 测试
                        } else if ("<%=keyId%>" == "A144PE00002") {
                            location.href = "http://m.samsunganycar.com/servlets/SPCI?keyId=A144MI00003";
                            return;
                    	} else {
                    		anyForm.action = "http://m.samsunganycar.com/web/m/init.do?keyId=<%=keyId%>";   // 正式
                    		//anyForm.action = forwardURL;   // 测试
                    	}
                    } else {
                    	anyForm.action = forwardURL;
                    }
                    anyForm.submit();
                }
            }
        }
    </script>
	<script language='javascript'>
        var isWindowsXPOr2003 = function() {
            var userAgent = navigator.userAgent.toLowerCase();
            return userAgent.indexOf('windows nt 5.1') >= 0 ||
                    userAgent.indexOf('windowsnt 5.2') >= 0;
        };
		function lfn_Init() {

		    var flag = "<%=flag%>";
		    var keyId = "<%=keyId%>";
		    var ipuid = "<%=ipuid%>";
            var params = "";
            params += "&PERFORM_ORDER=" + "010";
            params += "&keyId=" + keyId;
            params += "&ipuid=" + ipuid;
            params += "&app=" + "main";

		    if (keyId != "") {
                sendRequest(contextRootPath + '/home/keyIdForward.do?v=' + new Date().getTime(), params, "keyIdForward");
		    } else {
                sendRequest(contextRootPath + '/information/baseInitStatic.do?v=' + new Date().getTime(), params, "baseInitStatic");
            }
		}
	</script>
	</head>
<body onLoad="lfn_Init()">

<form name='anyForm' method='post' id="anyForm">
  <%--<input type='hidden' name='keyId' value='<%=keyId%>'>--%>
<%
    if (area != null && ! "".equals(area)) {
        out.println("<input type='hidden' name='city.cityId' value='" + area + "'>");
    }
%>
  <input type='hidden' name='token' value='<%=source%>'>						<!-- +hilton -->
  <input type='hidden' name='weiboCid' value='<%=weiboCid%>'>					<!-- +hilton -->
  <input type='hidden' name='weiboViewer' value='<%=weiboViewer%>'>				<!-- +hilton -->
  <input type='hidden' name='weiboSubAppkey' value='<%=weiboSubAppkey%>'>		<!-- +hilton -->
  <input type='hidden' name='weiboAccessToken' value='<%=weiboAccessToken%>'>	<!-- +hilton -->
  <input type='hidden' name='veriNo' value='<%=veriNo%>'>
</form>

</body>
</html>
