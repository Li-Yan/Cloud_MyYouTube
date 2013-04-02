<%@page import="cloud.UploadServlet"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<style type="text/css">
.label {
	font-family: "Palatino Linotype", "Book Antiqua", Palatino, serif;
	font-weight: bold;
	color: #39F;
	font-size: 17px;
}
body {
	background-image: url(images/videoplay_background.png);
	background-repeat: repeat;
}
</style>
</head>

<body>

<%if (UploadServlet.uploadsucceed) { %>
<script language="javascript">alert("Congratulations: Video upload succeed~")</script>
<%
	UploadServlet.uploadsucceed = false;
}
%>
<%if (UploadServlet.duplicate) { %>
<script language="javascript">alert("Error: Duplicate video!!!")</script>
<%
	UploadServlet.duplicate = false;
}
%>
<%if (UploadServlet.emptyfile) { %>
<script language="javascript">alert("Error: Please select your video!!!")</script>
<%
	UploadServlet.emptyfile = false;
}
%>
<%if (UploadServlet.wrongformat) { %>
<form id="alertform">
<input id="supported_format" type="hidden" name="<%
int length = UploadServlet.supportedFormat.length;
for (int i = 0; i < length - 1; i++) {
	out.print(UploadServlet.supportedFormat[i] + " ");
}
out.print(UploadServlet.supportedFormat[length - 1] + "!!!");
%>"></input>
<script language="javascript">
s = "Error: Wrong video type.\n Only support " + document.getElementById("supported_format").name;
alert(s);
</script>
</form>
<%
	UploadServlet.wrongformat = false;
}
%>

<div align="center">
<img src="images/upload2.png" width="277" height="97" alt="upload title"><br><br>

<form action="cloud/UploadServlet" method="post" enctype="multipart/form-data">
<span class="label">Select a video:</span>&nbsp;
<input type="file" name="Video" class="label" size="30" /><br /><br />

<button type="submit"><img src="images/Submit.png" width="120" height="34" alt="submit"></button>
</form><br />
</div>
&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="location='index.html'"><img src="images/back.png" width="37" height="37" alt="back"></button>
</body>
</html>