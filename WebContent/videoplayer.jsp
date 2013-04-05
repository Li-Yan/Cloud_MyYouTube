<%@page import="cloud.Video"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type='text/javascript' src='https://d290w3p5o590mj.cloudfront.net/jwplayer.js'></script>
<title>Video Player</title>
<style type="text/css">
body {
	background-image: url(images/videoplay_background.png);
	background-repeat: repeat;
}
.video_name {
	font-family: "Palatino Linotype", "Book Antiqua", Palatino, serif;
	font-size: 24px;
	color: #CCC;
	font-weight: bolder;
}
</style>

<script src="./plugin/jquery-1.9.1.min.js"></script>
<link rel="Stylesheet" type="text/css" href="./plugin/wTooltip.css" />
<script type="text/javascript" src="./plugin/wTooltip.js"></script>

</head>
<body>
<div align="center">
<img src="images/speaker.png" width="97" height="97" alt="speaker">
<%for(int i = 0; i < 77; i++) {
	out.print("&nbsp;");
}%>
<img src="images/speaker-rv.png" width="97" height="97" alt="speaker"><br>
<span class="video_name">
<%out.print(Video.selectedVideo);%></span><br /><br />
<div id='mediaplayer'></div>
<script type="text/javascript">
   jwplayer('mediaplayer').setup({
      file: "rtmp://s13yeoa46ugv4e.cloudfront.net/cfx/st/<%out.print(Video.selectedVideo);%>",
      width: "640",
      height: "360"
   });
</script><br />
</div>
&nbsp;&nbsp;&nbsp;&nbsp;<button id="back" type="button" onclick="location='list.jsp'"><img src="images/back.png" width="37" height="37" alt="back"></button>

<script type="text/javascript">
$('.mooTest').wTooltip();
console.log($('.mooTest').wTooltip('opacity'));
$('.mooTest :first').wTooltip('opacity', 0.2)
console.log($('.mooTest').wTooltip('opacity'));

$("#back").wTooltip({
	title: "Back to List Page",
	theme: "blue"
});

</script>
</body>
</html>