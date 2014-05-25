/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 *
 * @author Marco Aurelio Nila Fonseca
 */
public class CursoBD extends ConexionBD{

    /**
     * Constructor de la clase
     */
    public CursoBD() {
        super();
    }
    
    /**
     * Cuenta la cantidad de cursos que son de cierto tipo
     * @param filtro es el tipo de curso que se contará
     * @return el número de cursos que cumplen con el filtro
     * @throws SQLException 
     */
    public int cuenta_cursos(String filtro) throws SQLException{
        int cantidad = 0;
        String consulta ="SELECT count(curso_id) AS cantidad " + 
            "FROM `Escuela`.`Profesor` AS foo JOIN " +
            "`Escuela`.`Curso` AS bar ON foo.profesor_correo = bar.profesor_correo " +
            "WHERE curso_estado = \"Espera\" ";
    
        if (!filtro.trim().equals("")) {
            consulta += "AND curso_tipo = \"" + filtro.trim()+"\" ";
        }
        consulta += ";";
        System.out.println(consulta);
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado =  consulta(conexion, consulta);
        if (resultado == null) {
            return -1;
        }
        if (resultado.next()) {
            cantidad = resultado.getInt("cantidad");
        }
        cierraConexion(conexion);
        return cantidad;
    }
    
    /**
     * Obtiene cierto número de cursos que cumplan con cierto tipo
     * @param filtro es el tipo de cursos
     * @param pagina es el número de cursos que se solicita, sirve para paginar
     * @param cantidad es la cantidad maxima de cursos que se regresarán
     * @return
     * @throws SQLException 
     */
    public ArrayList<String> obten_cursos(String filtro, int pagina, int cantidad) throws SQLException{
        ArrayList<String> profesores = new ArrayList<String>();
        String consulta ="SELECT profesor_id, profesor_nombre, curso_id, curso_tipo, DATE_FORMAT(curso_inicio, '%H:%i') AS curso_inicio, DATE_FORMAT(curso_final, '%H:%i') AS curso_final " + 
            "FROM `Escuela`.`Profesor` AS foo JOIN " +
            "`Escuela`.`Curso` AS bar ON foo.profesor_correo = bar.profesor_correo " +
            "WHERE curso_estado = \"Espera\" ";
    
        if (!filtro.trim().equals("")) {
            consulta += "AND curso_tipo = \"" + filtro.trim() + "\" ";
        }
        consulta += "LIMIT " + pagina * cantidad + ", " + (pagina + 1) * cantidad +";";
        System.out.println(consulta);
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado =  consulta(conexion, consulta);
        if (resultado == null) {
            return null;
        }
        while (resultado.next()) {
//profesor_id, profesor_nombre, curso_id, curso_tipo, curso_inicio, curso_final          
            String cadena = "<tr>";
            cadena += "<td><a href=\"../profesor?id=" + resultado.getInt("profesor_id") +"\">" + resultado.getString("profesor_nombre") +"</a></td>";
            cadena += "<td>"+resultado.getString("curso_tipo")+"</td>";
            cadena += "<td>"+resultado.getString("curso_inicio")+" - "+ resultado.getString("curso_final") +"</td>";
            cadena += "<td><a href='#' class='button success radius tiny solicitar_curso' data-profesor='"+resultado.getInt("profesor_id")+"' data-curso='"+resultado.getInt("curso_id")+"'>Solicitar</a></td>";
            cadena +=  "</tr>";
            profesores.add(new String(cadena));
        }
        cierraConexion(conexion);
        return profesores;
    }
    
    /**
     * Obtiene los cursos de cierto profesor
     * @param id es el id del profesor
     * @param correo es el correo del profesor
     * @return la lista de los cursos que ofrece el profesor
     * @throws SQLException 
     */
    public ArrayList<String> obten_cursos_profesor(int id, String correo) throws SQLException{
        ArrayList<String> cursos = new ArrayList<String>();
        String consulta ="SELECT curso_id, curso_tipo, DATE_FORMAT(curso_inicio, '%H:%i') AS curso_inicio, DATE_FORMAT(curso_final, '%H:%i') AS curso_final " +
                "  FROM `Escuela`.`Curso` WHERE curso_estado = \"Espera\" AND profesor_correo = \"" + correo + "\";";
        System.out.println(consulta);
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado =  consulta(conexion, consulta);
        if (resultado == null) {
            return null;
        }
        while (resultado.next()) {
//profesor_id, profesor_nombre, curso_id, curso_tipo, curso_inicio, curso_final          
            String cadena = "<tr>";
            cadena += "<td>"+resultado.getString("curso_tipo")+"</td>";
            cadena += "<td>"+resultado.getString("curso_inicio")+" - "+ resultado.getString("curso_final") +"</td>";
            cadena += "<td><a href='#' class='button success radius tiny solicitar_curso' data-profesor='"+id+"' data-curso='"+resultado.getInt("curso_id")+"'>Solicitar</a></td>";
            cadena +=  "</tr>";
            cursos  .add(new String(cadena));
        }
        cierraConexion(conexion);
        return cursos;
    }
    
    /**
     * Relaciona un curso con cierto alumno y lo pone en modo "Confirmando"
     * @param alumno_correo es el correo del alumno
     * @param curso_id es el id del curso
     * @return indicador si se pudo o no hacer la actualización en la base de datos
     * @throws SQLException 
     */
    public boolean solicitar_curso(String alumno_correo, int curso_id) throws SQLException {
        String consulta = "UPDATE `Escuela`.`Curso` SET estudiante_correo='" + alumno_correo + "', curso_estado='Confirmando' " +
                "WHERE curso_id = " + curso_id + ";";
        System.out.println(consulta);
        Connection conexion;
        conexion = getConexion();
        int resultado =  actualiza(conexion, consulta);
        return resultado != 0;
    }
    
    public boolean crear_curso(String correo, String tinicio, String tfinal, String tipo) {
        System.out.println(correo);
        String consulta_1 = "SELECT * FROM `Escuela`.`Profesor` WHERE `profesor_correo`='" + correo + "' AND (`profesor_url_certificado` IS NULL OR `profesor_url_video` IS NULL)";
        String consulta_2 = "SELECT * FROM `Escuela`.`Curso` WHERE `profesor_correo`='" + correo  +"' AND `curso_inicio`='" + tinicio + "' AND (`curso_estado`='Cursando' OR `curso_tipo`='" + tipo + "');";

        String query = "INSERT INTO `Escuela`.`Curso` (`profesor_correo`, `estudiante_correo`, `curso_inicio`, `curso_final`, `curso_tipo`, "
                + "`curso_estado`, `curso_nota`, `curso_calificacion`) VALUES ('" + correo + "', NULL, '" + tinicio + "', '" + tfinal
                + "','" + tipo + "', 'Espera', NULL, NULL);";
        boolean encontrado = false;
               
        Connection conexion = super.conectarBD();
        ResultSet resultado_1 = super.consultar(conexion, consulta_1);
        ResultSet resultado_2 = super.consultar(conexion, consulta_2);
        
        if (resultado_1 == null || resultado_2 == null) {
            return false;
        }
        
        try {
            encontrado = resultado_1.next();
            encontrado = encontrado || resultado_2.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
                
        if (encontrado) {
            return false;
        }
        
        try {
            Statement st = conexion.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        super.desconectarBD(conexion);
        return true;
    }
    
    public boolean asignar_curso(String id, boolean asignar) {
        String query = "";
        if (asignar)
            query = "UPDATE `Escuela`.`Curso` SET `curso_estado`='Cursando' WHERE `curso_id`='" + id + "';";
        else
            query = "UPDATE `Escuela`.`Curso` SET `curso_estado`='Espera', `estudiante_correo`=NULL WHERE `curso_id`='" + id + "';";
        
        Connection conexion = super.conectarBD();

        try {
            Statement st = conexion.createStatement();
            st.execute(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.desconectarBD(conexion);
        return true;
    }

    public int eliminar_curso(String id) {
        int ex = -1;
        String query = "DELETE FROM `Escuela`.`Curso` WHERE `curso_id`='" + id + "';";
        Connection conexion = null;
        try {
            conexion = super.conectarBD();
            Statement st = conexion.createStatement();
            ex = st.executeUpdate(query);
            st.close();
            super.desconectarBD(conexion);
            return ex;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return ex;
    }
    
    public boolean calificar_curso(String id, String calificacion, String nota) {
        String query;
        if (!nota.equals("")) {
            query = "UPDATE `Escuela`.`Curso` SET `curso_calificacion`=" + Integer.parseInt(calificacion) + ", `curso_estado`='Terminado', `curso_nota`='" + nota + "'";
            query += " WHERE `curso_id`='" + id + "';";
        } else {
            query = "UPDATE `Escuela`.`Curso` SET `curso_calificacion`=" + Integer.parseInt(calificacion) + ", `curso_estado`='Terminado'";
            query += " WHERE `curso_id`='" + id + "';";
        }
        
        Connection conexion = super.conectarBD();
        try {
            Statement st = conexion.createStatement();
            st.execute(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        super.desconectarBD(conexion);
        return true;
    }
}
