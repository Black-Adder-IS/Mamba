/*
 * Controlado de Estudiante
 */

package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ConexionBD;
import modelo.CursoBD;
import modelo.EstudianteBD;

/**
 *
 * @author Marco Aurelio Nila Fonseca
 * @version 1.0
 */
@WebServlet(name = "Estudiante", urlPatterns = {"/Estudiante"})
public class Estudiante extends HttpServlet {
    
    private final String CONFIRMACION_REGISTRO = "0";
    private final String RECHAZO_REGISTRO = "1";
    private final String CONFIRMACION_EDICION = "2";
    private final String RECHAZO_EDICION = "3";
    private final String CONFIRMACION_BORRADO = "4";
    private final String RECHAZO_BORRADO = "5";

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
        String operacion = request.getParameter("operacion");
        
        if (operacion == null) {
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
        
        PrintWriter out = response.getWriter();
        if (operacion.equals("registrar_Estudiante")) {
            String nombre = request.getParameter("nombre_Estudiante");
            String correo = request.getParameter("correo_Estudiante");
            String contrasenia = request.getParameter("contrasenia_Estudiante");

            if (new EstudianteBD().crear_estudiante(nombre, correo, contrasenia)) {
                out.println(CONFIRMACION_REGISTRO);
                Date date = new Date();
                DateFormat hora = new SimpleDateFormat("HH:mm:ss");
                DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                Correo enviar_correo = new Correo();
                enviar_correo.cuerpo_Correo(correo, "Registro Escuela Inglés", "Tu cuenta ha sido creada en \"Elementors\"\n"
                        + "Tus datos son:\n"
                        + "Nombre: " + nombre
                        + "\nContraseña: " + contrasenia
                        + "\nHora de registro: " + hora.format(date)
                        + "\nFecha de registro: " + fecha.format(date));
                enviar_correo.enviar_Correo();
            } else {
               out.println(RECHAZO_REGISTRO);
            }

        } else if (operacion.equals("editar_Estudiante")) {
            String correoA = request.getParameter("correo_Estudiante");
            String nombre = request.getParameter("nuevo_nombre_Estudiante");
            String correo = request.getParameter("nuevo_correo_Estudiante");
            String contrasenia = request.getParameter("nuevo_contrasenia_Estudiante");
            
            if (new EstudianteBD().editar_estudiante(correoA, nombre, correo, contrasenia) == 1) {
                out.println(CONFIRMACION_EDICION);
            } else {
                out.println(RECHAZO_EDICION);
            }
        
        } else if (operacion.equals("eliminar_Estudiante")) {
            String correo = request.getParameter("correo_Estudiante");
            if (new EstudianteBD().eliminar_estudiante(correo) == 1) {
                out.println(CONFIRMACION_BORRADO);
            } else {
                out.println(RECHAZO_BORRADO);
            }

        } else if (operacion.equals("obtener_Cursos_Actuales")) {
            String correo = request.getParameter("correo_Estudiante");
            ConexionBD conexion_bd = new ConexionBD();
            Connection conexion = conexion_bd.conectarBD();
            ResultSet rs = conexion_bd.consulta(conexion, "SELECT ALL `profesor_correo`, `curso_tipo`, `curso_inicio`, `curso_final`" +
                                                  "FROM `Escuela`.`Curso` WHERE `curso_estado`='Cursando' AND `estudiante_correo`='" + correo + "';");
            
            ResultSet r;
            Object [] fila;
            Object nombre_Profesor;
            Object url_Pagina;
            try {
                while (rs.next()) {
                    fila = new Object[4];

                    for (int i = 1; i <= 4; i++)
                        fila[i-1] = rs.getObject(i);

                    r = conexion_bd.consulta(conexion, "SELECT `profesor_nombre`, `profesor_id` FROM `Escuela`.`Profesor` WHERE `profesor_correo`='" + 
                                                         fila[0].toString() + "';");
                    r.next();
                    nombre_Profesor = r.getObject(1);
                    url_Pagina = r.getObject(2);
                    
                    out.println("<tr>");
                    out.println("<td><a href=\"profesor?id=" + url_Pagina + "\">" + nombre_Profesor + "</a></td>");
                    out.println("<td>" + fila[1] + "</d>");
                    out.println("<td>" + fila[2].toString().substring(0, 5) + " - " + fila[3].toString().substring(0, 5) + "</td>");
                    out.println("</tr>");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

        } else if (operacion.equals("obtener_Cursos_Finalizados")) {
            String correo = request.getParameter("correo_Estudiante");
            ConexionBD conexion_bd = new ConexionBD();
            Connection conexion = conexion_bd.conectarBD();
            ResultSet rs = conexion_bd.consulta(conexion, "SELECT ALL `profesor_correo`, `curso_tipo`, `curso_calificacion`, `curso_nota`, `curso_url_certificado`" +
                                                  "FROM `Escuela`.`Curso` WHERE `curso_estado`='Terminado' AND `estudiante_correo`='" + correo + "';");
            
            ResultSet r;
            Object [] fila;
            Object nombre_Profesor;
            Object url_Pagina;
            try {
                while (rs.next()) {
                    fila = new Object[4];

                    for (int i = 1; i <= 4; i++)
                        fila[i-1] = rs.getObject(i);

                    r = conexion_bd.consulta(conexion, "SELECT `profesor_nombre`, `profesor_id` FROM `Escuela`.`Profesor` WHERE `profesor_correo`='" + 
                                                         fila[0].toString() + "';");
                    r.next();
                    nombre_Profesor = r.getObject(1);
                    url_Pagina = r.getObject(2);
            
                    out.println("<tr>");
                    out.println("<td><a href=\"profesor?id=" + url_Pagina + "\">" + nombre_Profesor + "</a></td>");
                    out.println("<td>" + fila[1] + "</td>");
                    if (Integer.parseInt(fila[2].toString()) <= 5) {
                        out.println("<td>" + fila[2] + "</td>");
                        out.println("<td>");
                        out.println("<a href=\"#\" data-dropdown=\"drop1\" class=\"button alert radius tiny\">");
                        out.println("Nota");
                        out.println("</a>");
                        out.println("<div id=\"drop1\" data-dropdown-content class=\"f-dropdown content\">");
                        out.println("<p>" + fila[3] + "</p>");
                        out.println("</div>");
                        out.println("</td>");
                    } else {
                        out.println("<td>" + fila[2] + "</td>");
                        out.println("<td>");
                        out.println("<a target='_blank' href='" + rs.getString("curso_url_certificado") + "' class='button success radius tiny'>");
                        out.println("Descargar");
                        out.println("</a></td>");
                    }
                    out.println("</tr>");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

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
