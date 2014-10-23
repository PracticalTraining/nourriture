<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<body>
	<h2>jenkins,Hello World!!</h2>
	<form action="/nourriture-web/testAction.action">
		<input type="submit" value="测试ssh配置成功了没" />
	</form>
	<h1><s:property value="str"/></h1>
</body>
</html>
