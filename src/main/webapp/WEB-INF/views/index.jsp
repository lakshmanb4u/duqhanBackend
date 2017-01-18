
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!--<img src="../resources/images/logo.png"/>-->
<h1>Hello Worlds!</h1>

<%--<c:forEach var="user" items="${users}" varStatus="loop" >--%>
<p> ${users.email}</p>
<%--</c:forEach>--%>