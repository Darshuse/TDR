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
	   Watchdog Message User Notification
	</h1>
	
	<p> A watchdog alert has been generated for the user request description <b>${description}</b></p>

<table style="width:80%" >
  <tr>
    <td>Message UUMID</td>
    <td><b>${mesgUumid}</b></td>
    <td>Suffix</td>
    <td><b>${mesgUumidSuffix}</b></td>
  </tr>
  
    <tr>
    <td>Format</td>
    <td><b>Swift</b></td>
    <td>Sub-Format</td>
    <td><b>${mesgSubFormat}</b></td>
  </tr>
  
  
    <tr>
    <td>Msg type</td>
    <td><b>${messageType}</b></td>
    <td>Nature</td>
    <td><b>${mesgNature}</b></td>
  </tr>

</table>
<br>


<#function optinalParamFun optionalParam="" >
    <#if (optionalParam?has_content)>
          <#return optionalParam />
    <#else>
         <#return '' />

    </#if>
</#function>

<table style="width:80%" >
<tr><td colspan=4>&nbsp;</td></tr>
<tr><td>Sender</td><td><b>${mesgSenderX1}</b></td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(senderCorrBranchInfo)}</td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(senderCorrCountryCode)}</td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(senderCorrCountryName)}</td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(senderCorrGeoLocation)}</td><td></td><td></td></tr>

<tr><td colspan=4>&nbsp;</td></tr>

<tr><td>Receiver</td><td><b>${xReceiverX1}</b></td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(reciverCorrBranchInfo)}</td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(reciverCorrCountryCode)}</td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(reciverCorrCountryName)}</td><td></td><td></td></tr>
<tr><td></td><td>${optinalParamFun(reciverCorrGeoLocation)}</td><td></td><td></td></tr>

</table>

<br>


<table style="width:80%" >
   <tr>
    <td>Transaction Ref</td>
    <td><b>${mesgTrnRef}</b></td>
    <td>Related ref.</td>
    <td><b>${mesgRelTrnRef}</b></td>
  </tr>
  
  <tr>
    <td>Amount</td>
    <td><b>${xFinAmount} ${xFinCcy}</b></td>
    <td>Value date</td>
    <td><b>${xFinValueDate}</b></td>
  </tr>
</table>

<br>

<table style="display:${displayText}">
<tr><td colspan=4>&nbsp;</td></tr><tr><td>Text</td><td>=</td><td></td><td></b></td></tr>
<tr><td colspan=4>&nbsp;</td></tr><tr><td>${mesgText}</td><td></td><td></td><td></b></td></tr>
</table>

 </body>
</html>