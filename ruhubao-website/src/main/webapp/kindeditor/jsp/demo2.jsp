<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String htmlData = request.getParameter("content") != null ? request.getParameter("content") : "";
%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8" />
	<title>KindEditor JSP</title>
	<script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="../js/ajaxfileupload.js"></script>
	<link rel="stylesheet" type="text/css"
		href="../js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
	href="../js/easyui/themes/icon.css">
	<!-- <link rel="stylesheet" type="text/css" href="../css/default.css"> -->
	<script type="text/javascript"
		src="../js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
	src="../js/date.js"></script>
	
	<link rel="stylesheet" href="../themes/default/default.css" />
	<link rel="stylesheet" href="../plugins/code/prettify.css" />
<!-- 	<script charset="utf-8" src="../kindeditor.js"></script> -->
	<script charset="utf-8" src="../kindeditor-all.js"></script>
	<script charset="utf-8" src="../lang/zh-CN.js"></script>
	<script charset="utf-8" src="../plugins/code/prettify.js"></script>
	
	
	<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="content"]', {
				//修改文件上传的地址
				uploadJson : '../../pic/uploadimage', 
				/* 	uploadJson : '../jsp/upload_json.jsp',  */
				//图片空间的查询地址
				fileManagerJson : '../../pic/manage',
				/* fileManagerJson : '../jsp/file_manager_json.jsp', */
				//开启图片空间
				allowFileManager : true,
				/* afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
				} */
			});
			prettyPrint();
		});
	</script>
</head>
<body>
	<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				<a id="back" icon="icon-back" href="#" class="easyui-linkbutton" plain="true">返回列表</a>
			</div>
	</div>
	
	<form id="content" enctype="multipart/form-data"
				action="../../content/savaContent" method="post">
				<%-- 携带id隐藏域 --%>
				<input type="hidden" name="id" id="id"/>
				
				<table class="table-edit" width="95%" align="center">
					
					<tr>
						<td>宣传概要(标题):</td>
						<td colspan="3">
							<input type="text" name="title" id="title" class="easyui-validatebox" required="true" />
						</td>
					</tr>			
					<!-- <tr>
						<td>发布时间: </td>
						<td>
							<input type="text" name="startDate" id="startDate" class="easyui-datebox" required="true" />
						</td>
						<td>失效时间: </td>
						<td>
							<input type="text" name="endDate" id="endDate" class="easyui-datebox" required="true" />
						</td>
					</tr> -->
					<tr>
						<td>宣传内容(活动描述信息):</td>
						<td colspan="3">
							<textarea id="content" name="content" style="width:100%" rows="20"></textarea>
						</td>
					</tr>
				</table>
			</form>
</body>
</html>
<%!
private String htmlspecialchars(String str) {
	str = str.replaceAll("&", "&amp;");
	str = str.replaceAll("<", "&lt;");
	str = str.replaceAll(">", "&gt;");
	str = str.replaceAll("\"", "&quot;");
	return str;
}
%>