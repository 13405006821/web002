<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>欢迎使用信息收集系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link type="text/css" rel="stylesheet" href="skin/css/base.css">
  </head>
  <body bgcolor="#ffffff" style="font-size: 12px;">
 <table width="98%" cellspacing="1" cellpadding="0" border="0" bgcolor="#D1DDAA" align="center">
	<tbody><tr>
	 <td background="skin/images/newlinebg3.gif" height="26">
	  <table width="98%" cellspacing="0" cellpadding="0" border="0">
	  <tbody><tr>
	  <td align="center">
	    <input type="button" value="首页" onclick="location='/makehtml/servlet/ControlServlet';" class="coolbg np" style="color: #3996f4;">
	    <input type="button" value="配置信息" onclick="location='/makehtml/servlet/PzxxServlet';" class="coolbg np">
	    <input type="button" value="车源信息" onclick="location='/makehtml/servlet/CartListServlet?p=1';" class="coolbg np">
	    <input type="button" value="货源信息" onclick="location='/makehtml/servlet/SourceListServlet?p=1';" class="coolbg np">
	    <input type="button" value="安全退出" onclick="location='/makehtml/servlet/LogoutServlet';" class="coolbg np">
	 </td>
	 </tr>
	</tbody></table>
	</td>
	</tr>
	</tbody>
</table>
  <form name="form2">
	<table width="98%" cellspacing="1" cellpadding="2" border="0" bgcolor="#D1DDAA" align="center" style="margin-top:8px;font-size: 12px;">
		<tbody>
		<tr bgcolor="#E7E7E7">
			<td background="skin/images/tbg.gif" height="24" colspan="10">&nbsp;欢迎使用&nbsp;</td>
		</tr>
		<tr bgcolor="#FAFAF1">
			<td colspan="10">
				<div style="margin-top:8px;margin-bottom:3px;text-align: center;font-size: 16px;">
					<b>欢迎使用本系统</b>
				</div>
				<div style="margin-left: 40px;margin-bottom:3px;margin-top:8px;">
					<b>一.运行环境</b>
				</div>
				<div style="margin-left: 60px;margin-bottom:3px;margin-top:8px;line-height: 20px;">
					1.windows操作系统.<br/>
					2.mysql5.1及以上.<br/>
					3.tomcat6.0及以上.<br/>
				</div>
				<div style="margin-left: 40px;margin-bottom:3px;margin-top:8px;">
					<b>二.注意事项</b>
				</div>
				<div style="margin-left: 60px;margin-bottom:3px;margin-top:8px;line-height: 20px;">
					1.使用过程中,严禁重复提交.<br/>
					2.信息提交过程中,请耐心等待.<br/>
					3.信息提交完成后,请及时查看物流平台的信息.<br/>
				</div><br/><br/>
			</td>
		</tr>
		<tr bgcolor="#EEF4EA" align="right">
			<td align="center" height="36" colspan="10"><!--翻页代码 --></td>
		</tr>
		</tbody>
	</table>
</form>
  </body>
</html>
