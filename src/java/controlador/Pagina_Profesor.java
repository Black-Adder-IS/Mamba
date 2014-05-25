/*
 * Página individual para cada profesor
 */

package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ProfesorBD;

/**
 *
 * @author Marco Aurelio Nila Fonseca
 * @version 1.0
 */
@WebServlet(name = "Pagina_Profesor", urlPatterns = {"/profesor"})
public class Pagina_Profesor extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String id_str = request.getParameter("id");
            ProfesorBD profesorBD = new ProfesorBD();
            int id;
            Profesor_contenedor profesor;
            try {
                id = Integer.parseInt(id_str.trim());
                profesor = profesorBD.obten_profesor(id);
                if (profesor == null) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
            
            
out.println("<!doctype html>");
out.println("<html class='no-js' lang='en'>");
out.println("<head>");
out.println("  <meta charset='utf-8' />");
out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0' />");
out.println("  <title>Escuela de Inglés | Bienvenido</title>");
out.println("  <link rel='stylesheet' href='css/foundation.css' />");
out.println("  <link rel='stylesheet' href='css/index.css' />");
out.println("  <link rel='stylesheet' href='css/html.css' />");
out.println("  <link href='http://fonts.googleapis.com/css?family=Maven+Pro' rel='stylesheet' type='text/css'>");
out.println("  <script src='js/vendor/modernizr.js'></script>");
out.println("  <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->");
out.println("  <script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js'></script>");
out.println("  <script src='js/solicitar_curso.js'></script>");
out.println("  <script src='js/generador_menu_index.js'></script> ");
out.println("  <script src='js/inicio_sesion.js'></script></script> ");
out.println("  <script src='js/mensajes_alerta.js'></script> ");
out.println("  <script src='//cdn.sublimevideo.net/js/x9bc50rn.js' type='text/javascript'></script>");
out.println("</head>");
out.println("<body>");
out.println("<div id='iniciarModal' class='reveal-modal' data-reveal>");
out.println("  <h2>Inicio de sesión</h2>");
out.println("  <form data-abide='ajax'>");
out.println("    <div class='row'>");
out.println("      <div class='large-4 columns'>");
out.println("        <div class='input-wrapper'>");
out.println("          <label>Correo <small>requerido</small>");
out.println("            <input id='id_text' type='email' required>");
out.println("          </label>");
out.println("          <small class='error'>Correo inválido</small>");
out.println("        </div>");
out.println("      </div>");
out.println("      <div class='large-4 columns'>");
out.println("        <div class='input-wrapper'>");
out.println("          <label>Constraseña <small>requerida</small>");
out.println("              <input id='contrasena_text' type='password' required pattern='_*'>");
out.println("        </label>");
out.println("          <small class='error'>Contraseña inválida</small>");
out.println("        </div>");
out.println("      </div>");
out.println("      <div class='large-4 columns'>");
out.println("        <label>Iniciar sesión como:</label>");
out.println("          <input type='radio' name='sesion_tipo' id='estudiante_radio' checked='checked'><label for='estudiante_radio'>Estudian</label>");
out.println("        <input type='radio' name='sesion_tipo' id='profesor_radio'><label for='profesor_radio'>Profesor</label>");
out.println("      </div>");
out.println("    </div>");
out.println("    <div class='text-center'>");
out.println("        <a id='iniciar_sesion_btn' href='#' class='button small'>Entrar</a>");
out.println("    </div>");
out.println("  </form>");
out.println("  <a class='close-reveal-modal'>&#215;</a>");
out.println("</div>");
out.println("  <div id='myModal' class='reveal-modal' data-reveal>");
out.println("    <h2>Elige el tipo de registro</h2>");
out.println("       <p class='lead'>Puedes elegir entre maestro y alumno</p>");
out.println("    <div class='panel'>");
out.println("      <h5>Aprende</h5>");
out.println("      <p>Selecciona el nivel de curso que quieras tomar con el maestro que te agrade.</p>");
out.println("      <a href='#' class='small button'  data-reveal-id='registroAlumnoModal' data-reveal>Aprender</a>");
out.println("    </div>");
out.println("    <div class='panel'>");
out.println("      <h5>Enseña</h5>");
out.println("      <p>Ofrece cursos eligiendo los horarios que quieras y a alumnos de tu agrado.</p>");
out.println("      <a href='#' class='small button' data-reveal-id='registroMaestroModal' data-reveal>Enseñar</a>");
out.println("    </div>");
out.println("    <a class='close-reveal-modal'>&#215;</a>");
out.println("  </div>");
out.println("");
out.println("  <div id='registroAlumnoModal' class='reveal-modal' data-reveal>");
out.println("    <h2>Registro de Alumno</h2>");
out.println("    <form>");
out.println("      <div class='row'>");
out.println("        <div class='large-12 columns'>");
out.println("          <label>Nombre");
out.println("            <input type='text' placeholder='large-12.columns' />");
out.println("          </label>");
out.println("        </div>");
out.println("      </div>");
out.println("      <div class='row'>");
out.println("        <div class='large-4 columns'>");
out.println("          <label>Correo");
out.println("            <input type='text' placeholder='large-4.columns' />");
out.println("          </label>");
out.println("        </div>");
out.println("        <div class='large-4 columns'>");
out.println("          <label>Constraseña");
out.println("            <input type='text' placeholder='min. 6 caracteres' />");
out.println("          </label>");
out.println("        </div>");
out.println("        <div class='large-4 columns'>");
out.println("          <label>Nivel de Inglés");
out.println("            <select>");
out.println("              <option value='husker'>Principiante</option>");
out.println("              <option value='starbuck'>Intermedio</option>");
out.println("              <option value='hotdog'>Avanzado</option>");
out.println("            </select>");
out.println("          </label>");
out.println("        </div>");
out.println("      </div>");
out.println("      <div class='row'>");
out.println("        <div class='large-12 columns text-center'>");
out.println("          <a href='#' class='button'>Registrar</a>");
out.println("        </div>");
out.println("      </div>");
out.println("    </form>");
out.println("");
out.println("    <a class='close-reveal-modal'>&#215;</a>");
out.println("  </div>");
out.println("  <div id='registroMaestroModal' class='reveal-modal' data-reveal>");
out.println("    <h2>Registro de Maestro</h2>");
out.println("    <form>");
out.println("      <div class='row'>");
out.println("        <div class='large-12 columns'>");
out.println("          <label>Nombre");
out.println("            <input type='text' placeholder='large-12.columns' />");
out.println("          </label>");
out.println("        </div>");
out.println("      </div>");
out.println("      <div class='row'>");
out.println("        <div class='large-6 columns'>");
out.println("          <label>Correo");
out.println("            <input type='text' placeholder='large-4.columns' />");
out.println("          </label>");
out.println("        </div>");
out.println("        <div class='large-6 columns'>");
out.println("          <label>Constraseña");
out.println("            <input type='text' placeholder='min. 6 caracteres' />");
out.println("          </label>");
out.println("        </div>");
out.println("      </div>");
out.println("      <div class='row'>");
out.println("        <div class='large-12 columns text-center'>");
out.println("          <div class='panel'>");
out.println("            <p>Despúes del registro debes subir un video de 1 minuto presentandote y una constancia antes de poder ofrecer cursos.</p>");
out.println("          </div>");
out.println("        </div>");
out.println("      </div>");
out.println("      <div class='row'>");
out.println("        <div class='large-12 columns text-center'>");
out.println("          <a href='#' class='button'>Registrar</a>");
out.println("        </div>");
out.println("      </div>");
out.println("    </form>");
out.println("    <a class='close-reveal-modal'>&#215;</a>");
out.println("  </div>");
out.println("        <div class='row'>");
out.println("            <div class='small-4 large-4 columns'>");
out.println("                <img src='images/logo2.png' alt='Logotipo'/>");
out.println("            </div>");
out.println("            <div class='small-6 large-6 columns divmenu'>");
out.println("                <ul id ='menu-ul' class='button-group'>");
out.println("                    <li><a href='#' class='button alert tiny' data-reveal-id='iniciarModal' data-reveal>Ingresar</a></li>");
out.println("                    <li><a href='html/horarios.html' class='button alert tiny'>Horarios</a></li>");
out.println("                    <li><a href='#' class='button alert tiny' data-reveal-id='myModal' data-reveal>Registro</a></li>");
out.println("                </ul>");
out.println("            </div>");
out.println("        </div>");
out.println("  <div class=\"separador\"></div>");
out.println("  <div class='row'>");
out.println("    <div class='small-12 large-12 columns text-center'>");
out.println("      <h1>" + profesor.nombre + "</h1>");
out.println("      <div>");
out.println("        <span class='secondary label'>Cursos finalizados: " + profesor.cursos_terminado + "</span>");
out.println("        <span class='secondary label'>Cursos en progreso: " + profesor.cursos_cursando + "</span>");
out.println("        <span class='secondary label'>Cursos disponilbes: " + profesor.cursos_espera + "</span>");
out.println("      </div>");
out.println("<video id='a240e92d' class='sublime' poster='https://cdn.sublimevideo.net/vpa/ms_800.jpg' width='640' height='360' title='Midnight Sun' data-uid='a240e92d' data-autoresize='fit' preload='none'>");
out.println("  <source src='https://cdn.sublimevideo.net/vpa/ms_360p.mp4' />");
out.println("  <source src='https://cdn.sublimevideo.net/vpa/ms_720p.mp4' data-quality='hd' />");
out.println("  <source src='https://cdn.sublimevideo.net/vpa/ms_360p.webm' />");
out.println("  <source src='https://cdn.sublimevideo.net/vpa/ms_720p.webm' data-quality='hd' />");
out.println("</video>");
out.println("    </div>");
out.println("  </div>");
out.println("  <br>");
out.println("  <div class='row'>");
out.println("    <div class='small-12 large-12 columns text-center'>");
out.println("      <span class='primary label'>Cursos disponibles</span>");
out.println("      <table max-width='600'>");
out.println("        <thead>");
out.println("          <tr>");
out.println("            <th class='text-center' width='40%'>Tipo</th>");
out.println("            <th class='text-center' width='40%'>Horarios</th>");
out.println("            <th class='text-center' width='20%'>Solicitar</th>");
out.println("          </tr>");
out.println("        </thead>");
out.println("        <tbody>");
            for (String curso : profesor.cursos) {
                out.println(curso);
            }
out.println("        </tbody>");
out.println("      </table>");
out.println("    </div>");
out.println("  </div>");
out.println("  <div class='row'>");
out.println("    <div class='small-12 large-12 columns text-center'>");
out.println("      <div class='panel radius'>");
            if (profesor.certificado_url == null || profesor.certificado_url.trim().equals("")) {
out.println("        El profesor todavía no cuenta con una constancia por lo que no puede ofrecer cursos");                
            }else{
out.println("        Si te interesa puedes descargas la constancia de acreditación del maestro: <a href='" + profesor.certificado_url +"'>constacia.pdf</a>");
            }
out.println("      </div>");
out.println("    </div>");
out.println("  </div>");
out.println("");
out.println("  <script src='js/vendor/jquery.js'></script>");
out.println("  <script src='js/foundation.min.js'></script>");
out.println("  <script>");
out.println("    $(document).foundation();");
out.println("  </script>");
out.println("</body>");
out.println("</html>");
        
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
