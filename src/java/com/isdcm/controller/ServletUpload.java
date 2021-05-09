package com.isdcm.controller;

import com.isdcm.security.VideoSecurityHelper;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

@WebServlet(urlPatterns = "/upload")
public class ServletUpload extends HttpServlet {
    private final String UPLOAD_DIRECTORY = "C:\\Users\\micky\\Documents\\NetBeansProjects\\isdcm\\web\\WEB-INF\\video";
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        FileUtils.cleanDirectory(new File(UPLOAD_DIRECTORY)); 
        
        //process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                                         new DiskFileItemFactory()).parseRequest(request);
               
                String type = null;
                
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        String name = new File(item.getName()).getName();
                        item.write( new File(UPLOAD_DIRECTORY + "/inputVideo.mp4"));
                        System.out.println(UPLOAD_DIRECTORY + "/inputVideo.mp4");
                    }
                    else {
                        type = item.getString();
                    }
                }
             
                if (null == type){ // Desencriptar Video
                    System.out.println("Este caso no tendria que pasar nunca");
                }
                else switch (type) {
                    case "0": // Encriptar XML
                        break;
                    case "1": // Desencriptar XML
                        break;
                    case "2": // Encriptar Video
                        VideoSecurityHelper.encryptVideo();
                        break;
                    default: // Desencriptar Video
                        VideoSecurityHelper.decryptVideo();
                        break;
                }
                
               //File uploaded successfully
               request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
               request.setAttribute("message", "File Upload Failed due to " + ex);
            }          
          
        }else{
            request.setAttribute("message",
                                 "Sorry this Servlet only handles file upload request");
        }
     
        RequestDispatcher dispatcher = request.getRequestDispatcher("security.jsp");
        dispatcher.forward(request, response);
      
    }
   
}
