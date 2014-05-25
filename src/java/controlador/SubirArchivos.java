/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ProfesorBD;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author sango
 */
@WebServlet(name = "SubirArchivos", urlPatterns = {"/SubirArchivos"})
public class SubirArchivos extends HttpServlet {

    String uploadDir = "../../../web/html/";
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String correo = request.getParameter("correo_profesor");
        
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(!isMultipart){
        } else {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = null;

            try{
                items = upload.parseRequest(request);
            } catch (Exception e){
                e.printStackTrace();
            }
            Iterator<FileItem> it = items.iterator();
            while(it.hasNext()){
                FileItem item = it.next();


                    String itemName = item.getName();
                    if(itemName == null || itemName.equals("")){
                        continue;
                    }

                    String fileName = FilenameUtils.getName(itemName);
                    File f = null;
                    
                    if (fileName.substring(fileName.length()-3).equals("pdf")) {
                        f = checkExist(correo + ".pdf");
                        System.out.println(new ProfesorBD().editar_profesor(correo, "", "", "", "", uploadDir + correo + ".pdf"));
                    }  if (fileName.substring(fileName.length()-3).equals("mp4")) {
                        f = checkExist(correo + ".mp4");
                        System.out.println(new ProfesorBD().editar_profesor(correo, "", "", "", uploadDir + correo + ".mp4", ""));
                    }
                    
                    try {
                        item.write(f);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Logger.getLogger(SubirArchivos.class.getName()).log(Level.SEVERE, null, ex);
                    }

            }


        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            processRequest(request, response);
        } finally {            
            out.close();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private File checkExist(String correo) {
        File f = new File(uploadDir + correo);
        if(f.exists()){
            f.delete();
        }
        f = new File(uploadDir + correo);
        return f;
    }
}
