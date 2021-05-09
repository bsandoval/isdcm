<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
if(session.getAttribute("user")== null) //check for existing session
{
	response.sendRedirect("login.jsp");
}
%>
<!DOCTYPE html>
<html>
   <head>
      <title>File Uploading Form</title>
   </head>
   
   <body>
        <h3> Something </h3>
        Video Encrypt: <br />
        <form action="upload" method="post" enctype="multipart/form-data">
            
            <input type="radio" value="0" name="group1" checked> XML Encrypt
            <input type="radio" value="1" name="group1"> XML Decrypt
            <input type="radio" value="2" name="group1"> Video Encrypt
            <input type="radio" value="3" name="group1"> Video Decrypt
            
            <input type="file" name="file" />
            <input type="submit" value="upload" />
        </form>
   </body>
   
</html>
