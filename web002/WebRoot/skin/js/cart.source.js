//���ѡ���ļ����ļ���
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

// ���ѡ������һ����id
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

// ��һ������
var XMLHttpReq = false;
// ����һ��XMLHttpRequest����
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

// ����������
function send(url) {
	createXMLHttpRequest();
	XMLHttpReq.open("get", url, true);
	XMLHttpReq.onreadystatechange = proce; // ָ����Ӧ�ĺ���
	XMLHttpReq.send(null); // ��������
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
	 show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >���ݴ�����,���Ե�...</font>');
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
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >���ݴ�����,���Ե�...</font>');
	if (XMLHttpReq.readyState == 4) { // ����״̬
		if (XMLHttpReq.status == 200) {// ��Ϣ�ѳɹ����أ���ʼ������Ϣ
			var root = XMLHttpReq.responseXML;
			var res = root.getElementsByTagName("content")[0].firstChild.data;
			hidden();
			window.alert(res);
			window.location = window.location.href;
		} else {
			hidden();
			window.alert("�������ҳ�����쳣");
		}
	}
}

//��Դ�����֤
function addCartAajax(id){
	send('/makehtml/servlet/AddCartServlet?id='+id);
}
function batchCart(aid,op){
	var qstr=getCheckboxItem();
	if(aid==0) aid = getOneItem();
	send('/makehtml/servlet/BatchCartServlet?op='+op+'&qstr='+qstr);
}

// ��Դ�����֤
function addSourceAajax(id) {
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >���ݴ�����,���Ե�...</font>');
	send('/makehtml/servlet/AddSourceServlet?id=' + id);
}
function batchSource(aid,op) {
	var qstr = getCheckboxItem();
	if (aid == 0)
		aid = getOneItem();
	show('<img src = "../skin/images/loading.gif"/><br/><br/><font color="blue" >���ݴ�����,���Ե�...</font>');
	send('/makehtml/servlet/BatchSourceServlet?op='+op+'&qstr=' + qstr);
}

