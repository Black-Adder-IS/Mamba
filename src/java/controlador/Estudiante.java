/*
 * Controlado de Estudiante
 */

package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.EstudianteBD;

/**
 *
 * @author Marco Aurelio Nila Fonseca
 * @version 1.0
 */
@WebServlet(name = "Estudiante", urlPatterns = {"/Estudiante"})
public class Estudiante extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        int tipo;
        try {
            tipo = Integer.parseInt(request.getParameter("tipo"));
        } catch (NumberFormatException e) {
            return;
        }
        switch(tipo){
            //Iniciar sesión
            case 0 :
                String id = request.getParameter("id");
                String contrasena = request.getParameter("contrasena");
                if (id == null || contrasena == null || id.trim().equals("") || contrasena.trim().equals("")) {
                    System.out.println("saliendo");
                    return;
                }
                id = id.trim();
                contrasena = contrasena.trim();
                EstudianteBD profesor = new EstudianteBD();
                boolean encontrado;
                try {
                    encontrado = profesor.iniciar_sesion(id, contrasena);
                } catch (SQLException ex) {
                    System.out.println(ex);
                    try (PrintWriter out = response.getWriter()) {
                        out.println("error");
                    }
                    return;
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(encontrado ? "true" : "false");
                }
                return;
            default:
                return;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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

}
