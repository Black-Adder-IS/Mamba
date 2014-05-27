/*
 * Maneja todas las peticiones a la base datos relacionadas con la tabla de profesores
 */

package modelo;

import controlador.Profesor_contenedor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 *
 * @author Marco Aurelio Nila Fonseca
 * @version 1.0
 */
public class ProfesorBD extends ConexionBD{
    
    /**
     * Constructor de la Clase
     */
    public ProfesorBD(){
        super();
    }
    
    /**
     * Método para iniciar sesión
     * @param id para buscar en la base de datos
     * @param contrasena para buscar en la base de datos
     * @return indicador si el usuario existe en la base de datos
     * @throws java.sql.SQLException
     */
    public boolean iniciar_sesion(String id, String contrasena) throws SQLException{
        boolean encontrado;
        String consulta = "select * from Profesor where profesor_correo = '" + id + "' AND profesor_contrasena = '" + contrasena +"';";
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado =  consulta(conexion, consulta);
        if (resultado == null) {
            return false;
        }
        encontrado = resultado.next();
        cierraConexion(conexion);
        return encontrado;
    }
    
    /**
     * Cuenta el número de profesores que tienen cursos con cierto filtro
     * @param filtro es el tipo de curso
     * @return el número de profes
     * @throws SQLException 
     */
    public int cuenta_profesores(String filtro) throws SQLException{
        int cantidad = 0;
        String consulta ="SELECT count(profesor_id) AS cantidad FROM " +
        "(SELECT profesor_id, profesor_nombre, sum(cantidad_terminado) AS cantidad_terminado, sum(cantidad_espera) AS cantidad_espera, sum(cantidad_cursando) AS cantidad_cursando " +
        "FROM Profesores ";
    
        if (!filtro.trim().equals("")) {
            consulta += "WHERE curso_tipo = \"" + filtro.trim() + "\" ";
        }
        consulta += "GROUP BY profesor_id) AS foo; ";
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
     * Obtiene una lista de profesores que tienen cursos que cumpen con cierto filtro
     * @param filtro es el tipo de curso
     * @param pagina el numero de pagina, sirve para paginar los resultados
     * @param cantidad es la cantidad de profesores que se pide, sirve para paginar los resultados
     * @return
     * @throws SQLException 
     */
    public ArrayList<String> obten_profesores(String filtro, int pagina, int cantidad) throws SQLException{
        ArrayList<String> profesores = new ArrayList<String>();
        String consulta ="SELECT * FROM " +
        "(SELECT profesor_id, profesor_nombre, sum(cantidad_terminado) AS cantidad_terminado, sum(cantidad_espera) AS cantidad_espera, sum(cantidad_cursando) AS cantidad_cursando " +
        "FROM Profesores ";
    
        if (!filtro.trim().equals("")) {
            consulta += "WHERE curso_tipo = \"" + filtro.trim() + "\" ";
        }
        consulta += "GROUP BY profesor_id) AS foo ";
        consulta += "LIMIT " + pagina * cantidad + ", " + cantidad +";";
        System.out.println("@@@@@@@Consulta: " + consulta);
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado =  consulta(conexion, consulta);
        if (resultado == null) {
            return null;
        }
        while (resultado.next()) {            
            String cadena = "<tr>";
            cadena += "<td><a href=\"../profesor?id=" + resultado.getInt("profesor_id") +"\">" + resultado.getString("profesor_nombre") +"</a></td>";
            cadena += "<td>"+resultado.getInt("cantidad_terminado")+"</td>";
            cadena += "<td>"+resultado.getInt("cantidad_cursando")+"</td>";
            cadena += "<td>"+resultado.getInt("cantidad_espera")+"</td>";
            cadena +=  "</tr>";
            profesores.add(new String(cadena));
        }
        cierraConexion(conexion);
        return profesores;
    }
    
    /**
     * Obtienen los datos de un profesor
     * @param id es el identificado del profesor
     * @return una estructura con los datos del profesor
     * @throws SQLException 
     */
    public Profesor_contenedor obten_profesor(int id) throws SQLException{
        ArrayList<String> cursos = new ArrayList<String>();
        Profesor_contenedor profesor;
        String consulta ="SELECT * " +
        "FROM Profesor WHERE profesor_id = " + id +";";
        System.out.println(consulta);
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado =  consulta(conexion, consulta);
        if (resultado == null || !resultado.next()) {
            return null;
        }
        profesor = new Profesor_contenedor();
        profesor.nombre = resultado.getString("profesor_nombre");
        profesor.correo = resultado.getString("profesor_correo");
        profesor.id = id;
        profesor.certificado_url = resultado.getString("profesor_url_certificado");
        profesor.video_url = resultado.getString("profesor_url_video");
        consulta = "SELECT count(*) AS cursos_espera FROM `Escuela`.`Curso` " +
                "WHERE profesor_correo = \"" + profesor.correo + "\" AND curso_estado = \"Espera\";";
        resultado =  consulta(conexion, consulta);
        resultado.next();
        profesor.cursos_espera = resultado.getInt("cursos_espera");
        
        consulta = "SELECT count(*) AS cursos_terminado FROM `Escuela`.`Curso` " +
                "WHERE profesor_correo = \"" + profesor.correo + "\" AND curso_estado = \"Terminado\";";
        resultado =  consulta(conexion, consulta);
        resultado.next();
        profesor.cursos_terminado = resultado.getInt("cursos_terminado");
        
        consulta = "SELECT count(*) AS cursos_cursando FROM `Escuela`.`Curso` " +
                "WHERE profesor_correo = \"" + profesor.correo + "\" AND curso_estado = \"Cursando\";";
        resultado =  consulta(conexion, consulta);
        resultado.next();
        profesor.cursos_cursando = resultado.getInt("cursos_cursando");
        cierraConexion(conexion);
        CursoBD cursoBD = new CursoBD();
        profesor.cursos = cursoBD.obten_cursos_profesor(profesor.id, profesor.correo);
        return profesor;
    }
    public boolean crear_profesor(String nombre, String correo, String contrasenia) {
        String consulta = "SELECT * FROM `Escuela`.`Profesor` WHERE `profesor_correo`='" + correo  +"';";
        String query = "INSERT INTO `Escuela`.`Profesor` (" +
                "`profesor_correo`, `profesor_nombre`, `profesor_contrasena`) " +
                "VALUES ('" + correo + "', '" + nombre + "', '" + contrasenia + "');";

        boolean encontrado = false;
                
        Connection conexion = super.conectarBD();
        ResultSet resultado = super.consultar(conexion, consulta);
            
        if (resultado == null) {
            return false;
        }
        
        try {
            encontrado = resultado.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
                
        if (encontrado) {
            return false;
        }
        
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
        return !encontrado;
    }
    
    public int editar_profesor(String correoA, String nombre, String correo, String contrasenia, String url_video, String url_constancia) {
        String consulta = "SELECT * FROM `Escuela`.`Profesor`";
        int ex = -1;
        String query = "";
        
        if (!nombre.equals("")) {
            query += " `profesor_nombre`='" + nombre + "',";
        }
        
        if (!correo.equals("")) {
            query += " `profesor_correo`='" + correo + "',";
        }
        
        if (!contrasenia.equals("")) {
            query += " `profesor_contrasena`='" + contrasenia + "',";
        }
        
        if (!url_video.equals("")) {
             query += "`profesor_url_video`='" + url_video + "',";
        } else if (!url_constancia.equals("")) {
            query += "`profesor_url_certificado`='" + url_constancia + "',";
        }
        
        if (query.length() != 0) {
            query = "UPDATE `Escuela`.`Profesor` SET" + query;
            int temp = query.length() - 1;
            query = query.substring(0, temp);
            query += " WHERE `profesor_correo`='" + correoA + "';";

        } else {
            return ex;
        }
        try {
            Connection conexion = super.conectarBD();
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
    
    public int eliminar_profesor(String correo) {
        int ex = -1;
        String query = "DELETE FROM `Escuela`.`Profesor` WHERE `profesor_correo`='" + correo + "';";
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
}

