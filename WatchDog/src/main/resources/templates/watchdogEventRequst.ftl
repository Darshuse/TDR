<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Java Techie Mail</title>
</head>

<body><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

 <body>
	<h1>
	   Watchdog Event User Notification
	</h1>
	
	<p> A watchdog alert has been generated for the user request description <b>${description}</b></p>

<table style="width:80%" >
  <tr>
    <td>Event Class</td>
    <td><b>${jrnlEventClass}</b></td>
    <td>Event Number</td>
    <td><b>${jrnlEventNum}</b></td>
  </tr>
  
    <tr>
    <td>Event Name</td>
    <td><b>${jrnlEventName}</b></td>
    <td>Component Name</td>
    <td><b>${jrnlCompName}</b></td>
  </tr>
  
  
    <tr>
    <td>Event Severity</td>
    <td><b>${jrnlEventServerity}</b></td>
    <td>Application Name</td>
    <td><b>${jrnlApplServName}</b></td>
  </tr>

</table>


<table >
<tr><td colspan=4>&nbsp;</td></tr><tr><td>Event Text</td><td>=</td><td></td><td></b></td></tr>
<tr><td colspan=4>&nbsp;</td></tr><tr><td>${jrnlMergedText}</td><td></td><td></td><td></b></td></tr>
</table>

 </body>
</html>