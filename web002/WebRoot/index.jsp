<%@ page language="java" pageEncoding="gbk"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>��½</title>
<head>
 <SCRIPT Language="JavaScript">
<!--
function  isnull(){
   
   if(document.form1.username.value==''&&document.form1.password.value==''){
    alert("�û��������벻��Ϊ�գ�")
    document.form1.username.focus();
    return false;
   }
   if(document.form1.password.value==''){
    alert("���벻��Ϊ�գ�");
    document.form1.password.focus();
    return false;
   }
   
   if(document.form1.username.value==''){
   alert("�û�������Ϊ�գ�");
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
      <td colspan="2"><div align="center" style="font-size: large;font-weight: bold;">��ӭ��½</div></td>
    </tr>
    <tr>
      <td width="86"><div align="center">�û���</div></td>
      <td width="199"><input name="username" type="text" maxlength="20" value="wxwl"></td>
    </tr>
    <tr>
      <td><div align="center">��&nbsp; ��</div></td>
      <td><input name="password" type="password" maxlength="20" value="wxwl20120410"></td>
    </tr>
    <tr>
      <td colspan="2"><div align="center">
        <font color="red">${tips}</font>
      </div></td>
    </tr>
    <tr>
      <td colspan="2"><div align="center">
        <input type="submit" name="Submit" value="��½">
      </div></td>
    </tr>
  </table>
</form>
   
</body>
</html>