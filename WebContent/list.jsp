<%@page import="java.util.*"%>

<%@page import="cloud.AmazonS3Manager"%>
<%@page import="cloud.Video" %>
<%@page import="cloud.UpdateRate" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of all videos</title>
<style type="text/css">
body {
	background-image: url(images/videoplay_background.png);
	background-repeat: repeat;
}
.list {
	font-family: "Palatino Linotype", "Book Antiqua", Palatino, serif;
	font-size: 13px;
	background-image: url(images/textured_paper.png);
	background-repeat: repeat;
}
.rate {
	font-family: Tahoma, Geneva, sans-serif;
	font-size: 17px;
}
</style>
<script src="./plugin/jquery-1.9.1.min.js"></script>
<link rel="Stylesheet" type="text/css" href="./plugin/wTooltip.css" />
<script type="text/javascript" src="./plugin/wTooltip.js"></script>
</head>
<body>

<div align="center">
<img src="images/All Video.png" width="350" height="87" alt="title"><br /><br />
<jsp:useBean id="S3Manager" scope="session" class="cloud.AmazonS3Manager"/>
<jsp:setProperty name="S3Manager" property="*" />
<% 
ArrayList<Video> videos = S3Manager.readAllFiles();
%>
<form name="main_form">
<select id="list" name="videoList" size="13" multiple="multiple" class="list" style="width:700px">
<% for (int i = 0; i < videos.size(); i++) { 
	String selectString = "[Name]: " + videos.get(i).name + " | ";
	selectString += "[Rate]: " + String.valueOf(videos.get(i).rate) + " | ";
	selectString += "[Timestamp]: " + videos.get(i).timeStamp;
%>
	<option value="<%out.print(videos.get(i).name);%>"><%out.print(selectString);%></option>
<% } %>
</select><br /><br />

<script language= "javascript">
function watch_video() {
	choose = document.getElementById("list");
	if (choose.options[choose.selectedIndex] == null) {
		alert("Alarm: Please select a video!!!"); 
	}
	else {
		document.forms["main_form"].action = "cloud/WatchVideo";
		document.forms["main_form"].method = "get";
		document.forms["main_form"].submit();
	}
}
</script> 

<button id="watch" type="button" onclick="watch_video()" ><img src="images/watch.png" width="37" height="37" alt="watch"></button><br><br>
<img src="images/rate.png" width="13" height="13" alt="rate">
<img src="images/rate.png" width="13" height="13" alt="rate">
<img src="images/rate.png" width="13" height="13" alt="rate">
<img src="images/rate.png" width="13" height="13" alt="rate">
<img src="images/rate.png" width="13" height="13" alt="rate">&nbsp;&nbsp;
<select name="rate" id="rate">
  <option value="-">-</option>
<% for (int i = 0; i < 6; i++) { 
%>
	<option value="<%out.print(i);%>"><%out.print(i);%></option>
<% } %>
</select>&nbsp;&nbsp;

<script language="javascript">
function update_rate() {
	choose = document.getElementById("list");
	rate = document.getElementById("rate");
	if (choose.options[choose.selectedIndex] == null) {
		alert("Alarm: Please select a video!!!"); 
	}
	else if (rate.options[rate.selectedIndex].text == "-") {
		alert("Alarm: Please select a rate!!!"); 
	}
	else {
		document.forms["main_form"].action = "cloud/UpdateRate";
		document.forms["main_form"].method = "post";
		document.forms["main_form"].submit();
	}
}
</script>

<button id="ratebutton" type="button" onclick="update_rate()" ><img src="images/check.png" width="17" height="17" alt="check"></button><br />
</form>
</div>
&nbsp;&nbsp;&nbsp;&nbsp;<button id="back" type="button" onclick="location='index.html'"><img src="images/back.png" width="37" height="37" alt="back"></button>

<script type="text/javascript">
$('.mooTest').wTooltip();
console.log($('.mooTest').wTooltip('opacity'));
$('.mooTest :first').wTooltip('opacity', 0.2)
console.log($('.mooTest').wTooltip('opacity'));

$("#watch").wTooltip({
	title: "Watch",
	theme: "blue"
});
$("#rate").wTooltip({
	title: "Choose a rate",
	theme: "blue"
});
$("#ratebutton").wTooltip({
	title: "Submit Rate",
	theme: "blue"
});
$("#back").wTooltip({
	title: "Back to Main Page",
	theme: "blue"
});

</script>
</body>

</html>