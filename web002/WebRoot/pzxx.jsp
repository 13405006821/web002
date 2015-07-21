<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>配置信息</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<style type="text/css">
			a {
				text-decoration: none;
			}
		</style>
		<style type="text/css">
    #cover
    {
        position:absolute;
        right:0px;
        bottom:0px;
        width:100%;
        height:100%;
        background-color:#D6E6FF;
        filter:alpha(opacity=70);
        -moz-opacity: 0.7;
        opacity: 0.7;       
        display:none;
        z-index:5;
    }
    #tb_window
    {
        width:200px;
        height:120px;
        border:0px;
        z-index:2;
        margin:200px auto;
    }
    </style>
	<script type="text/javascript" src="../skin/js/cart.source.js"></script>
	<script language="javascript">
var xmlHttp;
function createXMLHttpRequest() {
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
}
function createQueryString() {
	var cart_url = document.getElementById("cart_url").value;
	var cart_start = document.getElementById("cart_start").value;
	var cart_end = document.getElementById("cart_end").value;
	var source_url = document.getElementById("source_url").value;
	var source_start = document.getElementById("source_start").value;
	var source_end = document.getElementById("source_end").value;
	var id = document.getElementById("id").value;
	var queryString="cart_url=" + cart_url + "&cart_start=" + cart_start + "&cart_end=" + cart_end
		+ "&source_url=" + source_url+ "&source_start=" + source_start+ "&source_end=" + source_end+ "&id=" + id;
	return queryString;
}
function doRequestUsingGET() {
	createXMLHttpRequest();
	var queryString = "/makehtml/servlet/UpdatePzServlet?";
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >数据处理中,请稍等...</font>');
	queryString = queryString + createQueryString() + "&timeStamp="
			+ new Date().getTime();
	xmlHttp.onreadystatechange = handleStateChange;
	xmlHttp.open("GET", queryString, true);
	xmlHttp.send(null);
}
function doRequestUsingPost() {
	createXMLHttpRequest();
	var url = "/makehtml/servlet/UpdatePzServlet?timeStamp=" + new Date().getTime();
	var queryString = createQueryString();
	xmlHttp.open("POST", url, true);
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >数据处理中,请稍等...</font>');
	xmlHttp.onreadystatechange = handleStateChange;
	xmlHttp.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	xmlHttp.send(queryString);
}
function handleStateChange() {
	if (xmlHttp.readyState == 4) {
		if (xmlHttp.status == 200) {
			var responseText = document.createTextNode(xmlHttp.responseText);
			alert(responseText.data);
			hidden();
		}else{
			hidden();
			window.alert("所请求的页面有异常");
		}
	}
}
	</script>
	</head>
	<body bgcolor="#ffffff" style="font-size: 12px;">
		<table width="98%" cellspacing="1" cellpadding="0" border="0"
			bgcolor="#D1DDAA" align="center">
			<tbody>
				<tr>
					<td height="26">
						<table width="98%" cellspacing="0" cellpadding="0" border="0">
							<tbody>
								<tr>
									<td align="center">
										<input type="button" value="首页" onclick="location='/makehtml/servlet/ControlServlet';" class="coolbg np">
										<input type="button" value="配置信息" onclick="location='/makehtml/servlet/PzxxServlet';" class="coolbg np" style="color: #3996f4;">
										<input type="button" value="车源信息" onclick="location='/makehtml/servlet/CartListServlet?p=1';" class="coolbg np">
										<input type="button" value="货源信息" onclick="location='/makehtml/servlet/SourceListServlet?p=1';" class="coolbg np">
	    <input type="button" value="安全退出" onclick="location='/makehtml/servlet/LogoutServlet';" class="coolbg np">
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<form name="form2" action="/makehtml/servlet/UpdatePzServlet" method="get">
			<table width="98%" cellspacing="1" cellpadding="2" border="0"
				bgcolor="#D1DDAA" align="center"
				style="margin-top: 8px; font-size: 12px;">
				<tbody>
					<tr bgcolor="#E7E7E7" >
						<td height="24" colspan="10">
							&nbsp;配置信息&nbsp;
						</td>
					</tr>
					<tr bgcolor="#FAFAF1" align="center" height="22">
 						<td height="24" colspan="4">
							<input type="hidden" name="id" id="id" value="${pzxx.id}"/>
							车源URL：
						</td>
						<td height="24" colspan="6">
							<input style="width: 100%;" name="cart_url" id="cart_url" value="${pzxx.url_che }" readonly="readonly"/>
						</td>
					</tr>
					<tr bgcolor="#FAFAF1" align="center" height="22">
						<td height="24" colspan="4">
							车源开始页码：
						</td>
						<td height="24" colspan="6">
							<input style="width: 100%;" name="cart_start" id="cart_start" value="${pzxx.start_page_che }"/>
						</td>
					</tr>
					<tr bgcolor="#FAFAF1" align="center" height="22">
						<td height="24" colspan="4">
							车源结束页码：
						</td>
						<td height="24" colspan="6" style="text-align: left;">
							<input style="width: 400px;" value="${pzxx.end_page_che }" id="cart_end" name="cart_end"/>&nbsp;&nbsp;
						</td>
					</tr>
					<tr bgcolor="#FAFAF1" align="center" height="22">
						<td height="24" colspan="4">
							货源URL：
						</td>
						<td height="24" colspan="6">
							<input style="width: 100%;" id="source_url" name="source_url" value="${pzxx.url_huo }" readonly="readonly"/>
						</td>
					</tr>
					<tr bgcolor="#FAFAF1" align="center" height="22">
						<td height="24" colspan="4">
							货源开始页码：
						</td>
						<td height="24" colspan="6">
							<input style="width: 100%;" id="source_start" name="source_start" value="${pzxx.start_page_huo }"/>
						</td>
					</tr>
					<tr bgcolor="#FAFAF1" align="center" height="22">
						<td height="24" colspan="4">
							货源结束页码：
						</td>
						<td height="24" colspan="6" style="text-align: left;">
							<input style="width: 400px;" id="source_end" name="source_end" value="${pzxx.end_page_huo }"/>&nbsp;&nbsp;
						</td>
					</tr>
					<tr bgcolor="#FAFAF1" height="22">
						<td height="24" colspan="10">
							<input style="margin-left: 260px;" type="button" name="Submit2" value="提交"  onclick="doRequestUsingPost();"/>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="cover" style="left: 0px; top: 0px;">
            <div id="tb_window" style="margin-left:42%;">
                <div style="padding-left:6px;text-align: center;padding-top: 20px;">
                    <div id="messageId" style="text-align: center;font-size: 16px;"></div>
                </div>
            </div>
        </div>
	</body>
</html>
