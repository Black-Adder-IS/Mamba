/*
 * Clase para atender todas las consultas que tengan que ver con la tabla de estudiantes
 */

package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Marco Aurelio Nila Fonseca
 * @version 1.0
 */
public class EstudianteBD extends ConexionBD{
    
    public EstudianteBD(){
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
        String consulta = "select * from Estudiante where estudiante_correo = '" + id + "' AND estudiante_contrasena = '" + contrasena +"';";
        System.out.println(consulta);
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
    public boolean crear_estudiante(String nombre, String correo, String contrasenia) {
        String consulta = "SELECT * FROM `Escuela`.`Estudiante` WHERE `estudiante_correo`='" + correo  +"';";
        String query = "INSERT INTO `Escuela`.`Estudiante` (`estudiante_correo`, `estudiante_nombre`, `estudiante_contrasena`) VALUES ('" +
                        correo + "', '" + nombre + "', '" + contrasenia + "');";
        boolean encontrado = false;
                
        Connection conexion = super.conectarBD();
        if (conexion == null) {
        }
        ResultSet resultado = super.consulta(conexion, consulta);
            
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
    
    public int editar_estudiante(String correoA, String nombre, String correo, String contrasenia) {
        int ex = -1;
        String query = "UPDATE `Escuela`.`Estudiante` SET";
        if (!nombre.equals("")) {
            query += " `estudiante_nombre`='" + nombre + "',";
        }
        if (!correo.equals("")) {
            query += " `estudiante_correo`='" + correo + "',";
        }
        if (!contrasenia.equals("")) {
            query += " `estudiante_contrasena`='" + contrasenia + "',";
        }
        
        if (query.length() != 0) {
            query = "UPDATE `Escuela`.`Estudiante` SET" + query;
            int temp = query.length()-1;
            if (query.charAt(temp) == ',') {
                query = query.substring(0, temp);
            }
            query += " WHERE `estudiante_correo`='" + correoA + "';";
        } else {
            return ex;
        }
        query += " WHERE `estudiante_correo`='" + correoA + "';";
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
    
    public int eliminar_estudiante(String correo) {
        int ex = -1;
        String query = "DELETE FROM `Escuela`.`Estudiante` WHERE `estudiante_correo`='" + correo + "';";
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