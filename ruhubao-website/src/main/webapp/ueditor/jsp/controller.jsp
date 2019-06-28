<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE HTML>
<html lang="UTF-8">
<head>
    <meta charset="UTF-8">
    <title>ueditor demo</title>
   <!--  <script type="text/javascript">
   	 	window.UEDITOR_HOME_URL = "${basePath }/"
    </script> -->
</head>
<body>
    <!-- 加载编辑器的容器 -->
    <script id="container" name="content" type="text/plain">
        这里写你的初始化内容
    </script>
    <!-- 配置文件 -->
    <script type="text/javascript" src="${basePath }/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${basePath }/ueditor/ueditor.all.min.js"></script>

    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('container');
      //根据不同action上传图片和附件
       UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
            UE.Editor.prototype.getActionUrl = function(action) {
                if (action == 'uploadimage') {
                    return '${pageContext.request.contextPath}/pic/uploadimage';
                } else if (action == 'uploadfile') {
                    return '${pageContext.request.contextPath}/upload/uploadfile';
                } else {
                    return this._bkGetActionUrl.call(this, action);
                }
            } 
    </script>
</body>
</html>
