<%@ page language="java" pageEncoding="gbk"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>登陆</title>
<head>
 <SCRIPT Language="JavaScript">
<!--
function  isnull(){
   
   if(document.form1.username.value==''&&document.form1.password.value==''){
    alert("用户名与密码不能为空！")
    document.form1.username.focus();
    return false;
   }
   if(document.form1.password.value==''){
    alert("密码不能为空！");
    document.form1.password.focus();
    return false;
   }
   
   if(document.form1.username.value==''){
   alert("用户名不能为空！");
   document.form1.username.focus();
   return false;
   }
   
  }
//-->
</SCRIPT>
</head>

<body>
 <form name="form1" method="post" action="/makehtml/servlet/ControlServlet" onsubmit= "return isnull()">
  <table width="301" height="119" border="0" align="center" style="margin-top: 150px;line-height: 40px;">
    <tr>
      <td colspan="2"><div align="center" style="font-size: large;font-weight: bold;">欢迎登陆</div></td>
    </tr>
    <tr>
      <td width="86"><div align="center">用户名</div></td>
      <td width="199"><input name="username" type="text" maxlength="20" value="wxwl"></td>
    </tr>
    <tr>
      <td><div align="center">密&nbsp; 码</div></td>
      <td><input name="password" type="password" maxlength="20" value="wxwl20120410"></td>
    </tr>
    <tr>
      <td colspan="2"><div align="center">
        <font color="red">${tips}</font>
      </div></td>
    </tr>
    <tr>
      <td colspan="2"><div align="center">
        <input type="submit" name="Submit" value="登陆">
      </div></td>
    </tr>
  </table>
</form>
   
</body>
</html>