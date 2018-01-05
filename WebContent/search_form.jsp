<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<form action = "get_res.jsp" method = "post">
	<input id = "keyword" name = "keyword">
	<input id = "page" name = "page" type = "hidden" value = "1">
	<input id = "search_button" name = "search_button" style = "color:green" value = "搜索" type = "submit">
	<!-- <input id = "pattern" name = "pattern"> -->
	<select name = "pattern">
	<option value = "relevance" selected = "selected"> 相关度 </option>
	<option value = "time"> 时间 </option>
	<option value = "hot" > 热度 </option>
	</select>
	
	
</form>