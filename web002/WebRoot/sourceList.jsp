<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>货源列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<style type="text/css">
		a {text-decoration: none;}
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
  </head>
  <body bgcolor="#ffffff" style="font-size: 12px;">
 <table width="98%" cellspacing="1" cellpadding="0" border="0" bgcolor="#D1DDAA" align="center">
	<tbody><tr>
	 <td height="26">
	  <table width="98%" cellspacing="0" cellpadding="0" border="0">
	  <tbody><tr>
	  <td align="center">
	    <input type="button" value="首页" onclick="location='/makehtml/servlet/ControlServlet';" class="coolbg np">
	    <input type="button" value="配置信息" onclick="location='/makehtml/servlet/PzxxServlet';" class="coolbg np">
	    <input type="button" value="车源信息" onclick="location='/makehtml/servlet/CartListServlet?p=1';" class="coolbg np">
	    <input type="button" value="货源信息" onclick="location='/makehtml/servlet/SourceListServlet?p=1';" class="coolbg np" style="color: #3996f4;">
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
			<td height="24" colspan="10">&nbsp;货源列表&nbsp;</td>
		</tr>
		<tr bgcolor="#FAFAF1" align="center" height="22">
			<td width="4%">序列</td>
			<td width="3%">选择</td>
			<td width="31%">标题</td>
			<td width="5%">联系人</td>
			<td width="10%">联系电话</td>
			<td width="13%">出发地</td>
			<td width="17%">目的地</td>
			<td width="11%">发布时间</td>
			<td width="5%">操作</td>
		</tr>
		
		<c:forEach items="${sourceList}" varStatus="status" var="source">
			<tr bgcolor="#FFFFFF" align="center" height="22" onmouseout="javascript:this.bgColor='#FFFFFF';" onmousemove="javascript:this.bgColor='#FCFDEE';">
				<td>${status.count}</td>
				<td><input type="checkbox" class="np" value="${source.id}" id="id" name="id"></td>
				<td align="left"><a title="${source.title}">${fn:substring(source.title, 0, 28)}</a></td>
				<td>${source.link_man}</td>
				<td>${source.link_tel}</td>
				<td>${source.start_address}</td>
				<td>${source.dest_address}</td>
				<td>${source.modify_time}</td>
				<td><a href="#" onclick="addSourceAajax(${source.id})">添加</a></td>
			</tr>
		</c:forEach>
		<tr bgcolor="#FAFAF1">
			<td height="28" colspan="10">
				&nbsp;
				<a class="coolbg" href="javascript:selAll()">全选</a>
				<a class="coolbg" href="javascript:noSelAll()">取消</a>
				<a class="coolbg" href="javascript:batchSource(0,'add')">&nbsp;添加&nbsp;</a>
				<a class="coolbg" href="javascript:batchSource(0,'del')">&nbsp;删除&nbsp;</a>
			</td>
		</tr>
		<tr bgcolor="#EEF4EA" align="right">
			<td align="center" height="36" colspan="10">
				<c:if test="${p!=1}">
					<a href="/makehtml/servlet/SourceListServlet?p=1">首页</a>&nbsp;
					<a href="/makehtml/servlet/SourceListServlet?p=${p-1}">上页</a>&nbsp;
				</c:if>
				<c:if test="${p==1}">首页&nbsp;上页&nbsp;</c:if>
				<c:if test="${p==lastPage}">下页&nbsp;尾页</c:if>
				<c:if test="${p!=lastPage}">
					<a href="/makehtml/servlet/SourceListServlet?p=${p+1}">下页</a>&nbsp;
					<a href="/makehtml/servlet/SourceListServlet?p=${lastPage}">尾页</a>
				</c:if>
			</td>
		</tr>
		</tbody>
	</table>
</form>
		<div id="cover" style="left: 0px; top: 0px;">
            <div id="tb_window" style="margin-left:40%;">
                <div style="padding-left:6px;text-align: center;padding-top: 20px;">
                    <div id="messageId" style="text-align: center;font-size: 16px;"></div>
                </div>
            </div>
        </div>
</body>
</html>
