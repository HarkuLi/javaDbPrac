<%@ page import = "java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="breadcrumbs">
	<c:set var = "pathList" value = "${requestScope.pathList}" />
	<c:set var = "currentPath" value = "" />
	<c:forEach var = "path" items = "${pathList}">
		<c:set var = "currentPath" value = "${currentPath}/${path}" />
		<span>></span>
		<a href="${currentPath}">
			<c:out value = "${path}" />
		</a>
	</c:forEach>
</div>