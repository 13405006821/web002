//获得选中文件的文件名
function getCheckboxItem() {
	var allSel = "";
	if (document.form2.id.value)
		return document.form2.id.value;
	for (i = 0; i < document.form2.id.length; i++) {
		if (document.form2.id[i].checked) {
			if (allSel == "")
				allSel = document.form2.id[i].value;
			else
				allSel = allSel + "," + document.form2.id[i].value;
		}
	}
	return allSel;
}

// 获得选中其中一个的id
function getOneItem() {
	var allSel = "";
	if (document.form2.id.value)
		return document.form2.id.value;
	for (i = 0; i < document.form2.id.length; i++) {
		if (document.form2.id[i].checked) {
			allSel = document.form2.id[i].value;
			break;
		}
	}
	return allSel;
}
function selAll() {
	for (i = 0; i < document.form2.id.length; i++) {
		if (!document.form2.id[i].checked) {
			document.form2.id[i].checked = true;
		}
	}
}
function noSelAll() {
	for (i = 0; i < document.form2.id.length; i++) {
		if (document.form2.id[i].checked) {
			document.form2.id[i].checked = false;
		}
	}
}

// 设一个变量
var XMLHttpReq = false;
// 创建一个XMLHttpRequest对象
function createXMLHttpRequest() {
	if (window.XMLHttpRequest) { // Mozilla
		XMLHttpReq = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		try {
			XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {

			}
		}
	}
}

// 发送请求函数
function send(url) {
	createXMLHttpRequest();
	XMLHttpReq.open("get", url, true);
	XMLHttpReq.onreadystatechange = proce; // 指定响应的函数
	XMLHttpReq.send(null); // 发送请求
}
function createQueryString(){
	var cart_url = document.getElementById("cart_url").value;
	var cart_start = document.getElementById("cart_start").value;
	var cart_end = document.getElementById("cart_end").value;
	var source_url = document.getElementById("source_url").value;
	var source_start = document.getElementById("source_start").value;
	var source_end = document.getElementById("source_end").value;
	var queryString="cart_url=" + cart_url + "&cart_start=" + cart_start + "&cart_end=" + cart_end
		+ "&source_url=" + source_url+ "&source_start=" + source_start+ "&source_end=" + source_end;
	return queryString;
}
function doRequestUsingPost(aid,op){
	 createXMLHttpRequest();
	 var url="/makehtml/servlet/UpdatePzServlet?op="+ op ;
	 var queryString=createQueryString();
	 show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >数据处理中,请稍等...</font>');
	 xmlHttp.open("POST",url,true);
	 xmlHttp.onreadystatechange=proce;
	 xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	 xmlHttp.send(queryString);
}

function show(message) {
	document.getElementById("messageId").innerHTML = message;
	document.getElementById("cover").style.display = "block";
}
function hidden() {
	document.getElementById("cover").style.display = "none";
}

function proce() {
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >数据处理中,请稍等...</font>');
	if (XMLHttpReq.readyState == 4) { // 对象状态
		if (XMLHttpReq.status == 200) {// 信息已成功返回，开始处理信息
			var root = XMLHttpReq.responseXML;
			var res = root.getElementsByTagName("content")[0].firstChild.data;
			hidden();
			window.alert(res);
			window.location = window.location.href;
		} else {
			hidden();
			window.alert("所请求的页面有异常");
		}
	}
}

//车源身份验证
function addCartAajax(id){
	send('/makehtml/servlet/AddCartServlet?id='+id);
}
function batchCart(aid,op){
	var qstr=getCheckboxItem();
	if(aid==0) aid = getOneItem();
	send('/makehtml/servlet/BatchCartServlet?op='+op+'&qstr='+qstr);
}

// 货源身份验证
function addSourceAajax(id) {
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >数据处理中,请稍等...</font>');
	send('/makehtml/servlet/AddSourceServlet?id=' + id);
}
function batchSource(aid,op) {
	var qstr = getCheckboxItem();
	if (aid == 0)
		aid = getOneItem();
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >数据处理中,请稍等...</font>');
	send('/makehtml/servlet/BatchSourceServlet?op='+op+'&qstr=' + qstr);
}

