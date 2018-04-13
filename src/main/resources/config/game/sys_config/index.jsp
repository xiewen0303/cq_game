<%@ page language="java"  import="java.util.*,java.io.File" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jat file list</title>
</head>

 <%
	ArrayList<String> filelist = new ArrayList<String>();
	String filePath="/data/client/publish/config/nizhuan/zhcn/java/data";
	//String filePath="E:\apache-tomcat-7.0.64\webapps\web\zhcn\java\nightly";
	File root = new File(filePath);
	File[] files = root.listFiles();
	for(File file : files){
		if(file.getName().contains(".jat")||file.getName().equals("maplist.dat")){
			filelist.add(file.getName());	
		}
	}
%>

<body>

<%
 for (String fileName : filelist) { 
		
%>

<a href="#"><%= fileName %></a><br/>
<%
	}
%>
 
</body>
</html>