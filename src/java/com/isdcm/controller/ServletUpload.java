package com.isdcm.controller;

import com.isdcm.security.VideoSecurityHelper;
import com.isdcm.security.XMLSecurityHelper;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.IOException;
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
    private final String UPLOAD_DIRECTORY = "/input";
    private final String OUTPUT_DIRECTORY = "/output";
    private final String SECRET_KEY = "t0L64tGfVBU11zuhvM7ksA==";
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String webInfPath = getServletConfig().getServletContext().getRealPath("/WEB-INF");
        FileUtils.cleanDirectory(new File(webInfPath + UPLOAD_DIRECTORY));
        FileUtils.cleanDirectory(new File(webInfPath + OUTPUT_DIRECTORY)); 
        
        //process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                                         new DiskFileItemFactory()).parseRequest(request);
               
                String type = null;
                String inputPath = null;
                String outputPath = null;
                
                // Este for lee cada uno de los parametros que se le suben (radioButton y el fichero)
                for(FileItem item : multiparts){
                    // Para los ficheros del Upload
                    if(!item.isFormField()){
                        // Si el item es un fichero mp4 ejecuta esto
                        if (item.getContentType().equals("video/mp4")){
                            inputPath = webInfPath + UPLOAD_DIRECTORY + "/inputVideo.mp4";
                            outputPath = webInfPath + OUTPUT_DIRECTORY + "/outputVideo.mp4";
                        } else { // Si el video es otro tipo de fichero como un XML ejecuta esto
                            inputPath = webInfPath + UPLOAD_DIRECTORY + "/inputXml.xml";
                            outputPath = webInfPath + OUTPUT_DIRECTORY + "/outputXml.xml";
                        }
                        item.write( new File(inputPath));
                    } else { // Si es un radioButton, obtenemos el valor
                        type = item.getString();
                    }
                }
             
                if (null == type){
                    request.setAttribute("message",
                                 "The user has not select a radioButton. This means that the user has modify the HTML");
                } else switch (type) {
                    case "0": // Encriptar XML
                        XMLSecurityHelper.encrypt(inputPath, outputPath, SECRET_KEY);
                        break;
                    case "1": // Desencriptar XML
                        XMLSecurityHelper.decrypt(inputPath, outputPath, SECRET_KEY);
                        break;
                    case "2": // Encriptar Video
                        VideoSecurityHelper.encryptVideo(inputPath, outputPath, SECRET_KEY);
                        break;
                    default: // Desencriptar Video
                        VideoSecurityHelper.decryptVideo(inputPath, outputPath, SECRET_KEY);
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
