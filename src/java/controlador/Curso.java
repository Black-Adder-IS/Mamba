/*
 * Controlador del curso
 */
package controlador;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ConexionBD;
import modelo.CursoBD;

/**
 *
 * @author Marco Aurelio Nila Fonseca
 * @version 1.0
 */
@WebServlet(name = "Curso", urlPatterns = {"/Curso"})
public class Curso extends HttpServlet {

    private final String VALIDACION_TIEMPO = "-1";
    private final String CONFIRMACION_CREACION = "0";
    private final String RECHAZO_CREACION = "1";
    private final String CONFIRMACION_CALIFICACION = "2";
    private final String RECHAZO_CALIFICACION = "3";
    private final String CONFIRMACION_BORRADO = "4";
    private final String RECHAZO_BORRADO = "5";
    private final String CONFIRMACION_ASIGNADO = "6";
    private final String RECHAZO_ASIGNADO = "7";
    private final String CONFIRMACION_SOLICITUD = "8";
    private final String NO_PUEDE_CREAR = "9";

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
        String operacion = request.getParameter("operacion");

        if (operacion == null) {
            int tipo;
            try {
                tipo = Integer.parseInt(request.getParameter("tipo"));
            } catch (NumberFormatException e) {
                return;
            }
            CursoBD cursoBD;
            String filtro;
            switch (tipo) {
                //Cuenta número de cursos para poder paginar
                case 1:
                    filtro = request.getParameter("filtro");
                    int cuantos;
                    filtro = filtro == null ? "" : filtro.trim();
                    cursoBD = new CursoBD();
                    try {
                        cuantos = cursoBD.cuenta_cursos(filtro);
                    } catch (SQLException ex) {
                        System.out.println(ex);
                        try (PrintWriter out = response.getWriter()) {
                            out.println("error");
                        }
                        return;
                    }
                    try (PrintWriter out = response.getWriter()) {
                        out.println(cuantos);
                    }
                    return;
                //Cursos a mostrar
                case 2:
                    ArrayList<String> cursos;
                    filtro = request.getParameter("filtro");
                    String cantidad_str = request.getParameter("cantidad");
                    String pagina_str = request.getParameter("pagina");
                    int cantidad,
                     pagina;
                    try {
                        cantidad = Integer.parseInt(cantidad_str);
                        pagina = Integer.parseInt(pagina_str);
                    } catch (Exception e) {
                        return;
                    }
                    filtro = filtro == null ? "" : filtro.trim();
                    System.out.println("Parámetro: " + filtro);
                    cursoBD = new CursoBD();
                    try {
                        cursos = cursoBD.obten_cursos(filtro, pagina, cantidad);
                    } catch (SQLException ex) {
                        System.out.println(ex);
                        try (PrintWriter out = response.getWriter()) {
                            out.println("error");
                        }
                        return;
                    }
                    try (PrintWriter out = response.getWriter()) {
                        String json = new Gson().toJson(cursos);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(json);
                    }
                    break;
                //Solicitar curso
                case 3:
                    String estudiante = request.getParameter("estudiante");
                    String curso_str = request.getParameter("curso");
                    int curso = Integer.parseInt(curso_str);
                    boolean exito = false;
                    cursoBD = new CursoBD();
                    try {
                        exito = cursoBD.solicitar_curso(estudiante, curso);
                        if (exito) {
                            try (PrintWriter out = response.getWriter()) {
                                out.println(CONFIRMACION_SOLICITUD);
                            }
                            Date date = new Date();
                            DateFormat hora = new SimpleDateFormat("HH:mm:ss");
                            DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                            Correo enviar_correo = new Correo();
                            ConexionBD conexion_bd = new ConexionBD();
                            Connection conexion = conexion_bd.conectarBD();
                            ResultSet rs = conexion_bd.consulta(conexion, "SELECT `estudiante_correo`, `profesor_correo`, `curso_tipo`, `curso_inicio`, `curso_final` "
                                    + "FROM `Escuela`.`Curso` WHERE `curso_estado`='Confirmando' AND `curso_id`=" + curso + ";");

                            ResultSet r;
                            String nombre_Estudiante;
                            String nombre_Profesor;
                            Object[] fila;
                            try {
                                rs.next();
                                fila = new Object[5];

                                for (int i = 1; i <= 5; i++) {
                                    fila[i - 1] = rs.getObject(i);
                                }

                                r = conexion_bd.consulta(conexion, "SELECT `estudiante_nombre` FROM `Escuela`.`Estudiante` WHERE `estudiante_correo`='"
                                        + fila[0].toString() + "';");
                                r.next();
                                nombre_Estudiante = r.getObject(1).toString();

                                r = conexion_bd.consulta(conexion, "SELECT `profesor_nombre` FROM `Escuela`.`Profesor` WHERE `profesor_correo`='"
                                        + fila[1].toString() + "';");
                                r.next();
                                nombre_Profesor = r.getObject(1).toString();

                                enviar_correo.cuerpo_Correo(fila[1].toString(), "Solicitud de Curso ", "Estimado Profesor " + nombre_Profesor
                                        + ", por este medio le informamos que el estudiante " + nombre_Estudiante
                                        + " ha solicitado su curso."
                                        + "\nTipo de curso: " + fila[2].toString()
                                        + "\nHorario: " + fila[3].toString().substring(0, 5) + " - " + fila[4].toString().substring(0, 5) + " horas"
                                        + "\nContacto estudiante: " + fila[0].toString()
                                        + ".\n\n\n"
                                        + "Hora de envío: " + hora.format(date)
                                        + "\nFecha de envío: " + fecha.format(date));
                                enviar_correo.enviar_Correo();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                    }
            }
        } else {
            PrintWriter out = response.getWriter();
            if (operacion.equals("crear_Curso")) {
                String correo = request.getParameter("correo_Profesor");
                String tinicio = request.getParameter("tiempo_Inicio");
                String tfinal = request.getParameter("tiempo_Final");
                String tipo = request.getParameter("tipo_Curso");

                if (tinicio.compareTo(tfinal) >= 0) {
                    out.println(VALIDACION_TIEMPO);
                } else {
                    if (new CursoBD().crear_curso(correo, tinicio, tfinal, tipo) == 2) {
                        out.println(CONFIRMACION_CREACION);
                    } else if (new CursoBD().crear_curso(correo, tinicio, tfinal, tipo) == 1) {
                        out.println(NO_PUEDE_CREAR);
                    } else {
                        out.println(RECHAZO_CREACION);
                    }
                }

            } else if (operacion.equals("asignar_Curso")) {
                String id = request.getParameter("id_Curso");
                String aceptado = request.getParameter("aceptado_Curso");
                boolean asignado = false;
                if (aceptado.equals("true")) {
                    asignado = true;
                }

                if (new CursoBD().asignar_curso(id, asignado)) {
                    out.println(CONFIRMACION_ASIGNADO);

                    Date date = new Date();
                    DateFormat hora = new SimpleDateFormat("HH:mm:ss");
                    DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                    Correo enviar_correo = new Correo();
                    ConexionBD conexion_bd = new ConexionBD();
                    Connection conexion = conexion_bd.conectarBD();
                    ResultSet rs = conexion_bd.consulta(conexion, "SELECT `estudiante_correo`, `profesor_correo`, `curso_tipo`, `curso_inicio`, `curso_final` "
                            + "FROM `Escuela`.`Curso` WHERE `curso_estado`='Cursando' AND `curso_id`=" + id + ";");
                    if (rs == null)
                        return;
                    ResultSet r;
                    String nombre_Estudiante;
                    String nombre_Profesor;
                    Object[] fila;
                    try {
                        rs.next();
                        fila = new Object[5];

                        for (int i = 1; i <= 5; i++) {
                            fila[i - 1] = rs.getObject(i);
                        }

                        r = conexion_bd.consulta(conexion, "SELECT `estudiante_nombre` FROM `Escuela`.`Estudiante` WHERE `estudiante_correo`='"
                                + fila[0].toString() + "';");
                        r.next();
                        nombre_Estudiante = r.getObject(1).toString();

                        r = conexion_bd.consulta(conexion, "SELECT `profesor_nombre` FROM `Escuela`.`Profesor` WHERE `profesor_correo`='"
                                + fila[1].toString() + "';");
                        r.next();
                        nombre_Profesor = r.getObject(1).toString();

                        String mensaje_1 = "";
                        String mensaje_2 = "";
                        if (asignado) {
                            mensaje_1 = "Fuiste aceptado al curso del Profesor " + nombre_Profesor;
                            mensaje_2 = "aceptado";
                        } else {
                            mensaje_1 = "Fuiste rechazado al curso del Profesor " + nombre_Profesor;
                            mensaje_2 = "rechazado";
                        }
                        
                        enviar_correo.cuerpo_Correo(fila[0].toString(), mensaje_1, "Estimado " + nombre_Estudiante
                                + ", por este medio te informamos que el Profesor " + nombre_Profesor + " (" + fila[1].toString() + ")"
                                + " ha " + mensaje_2 + " tu solicitud"
                                + "\nTipo de curso: " + fila[2].toString()
                                + "\nHorario: " + fila[3].toString().substring(0, 5) + " - " + fila[4].toString().substring(0, 5) + " horas"
                                + ".\n\n\n"
                                + "Hora de envío: " + hora.format(date)
                                + "\nFecha de envío: " + fecha.format(date));
                        enviar_correo.enviar_Correo();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    out.println(RECHAZO_ASIGNADO);
                }

            } else if (operacion.equals("eliminar_Curso")) {
                String id = request.getParameter("id_Curso");

                if (new CursoBD().eliminar_curso(id) == 1) {
                    out.println(CONFIRMACION_BORRADO);
                } else {
                    out.println(RECHAZO_BORRADO);
                }

            } else if (operacion.equals("calificar_Curso")) {
                String id = request.getParameter("id");
                String calificacion_C = request.getParameter("calificacion");
                String nota_C = request.getParameter("nota");

                if (new CursoBD().calificar_curso(id, calificacion_C, nota_C)) {
                    out.println(CONFIRMACION_CALIFICACION);
                } else {
                    out.println(RECHAZO_CALIFICACION);
                }
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
