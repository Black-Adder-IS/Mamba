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
public class CursoBD extends ConexionBD {

    /**
     * Constructor de la clase
     */
    public CursoBD() {
        super();
    }

    /**
     * Cuenta la cantidad de cursos que son de cierto tipo
     *
     * @param filtro es el tipo de curso que se contará
     * @return el número de cursos que cumplen con el filtro
     * @throws SQLException
     */
    public int cuenta_cursos(String filtro) throws SQLException {
        int cantidad = 0;
        String consulta = "SELECT count(curso_id) AS cantidad "
                + "FROM `Escuela`.`Profesor` AS foo JOIN "
                + "`Escuela`.`Curso` AS bar ON foo.profesor_correo = bar.profesor_correo "
                + "WHERE curso_estado = \"Espera\" ";

        if (!filtro.trim().equals("")) {
            consulta += "AND curso_tipo = \"" + filtro.trim() + "\" ";
        }
        consulta += ";";
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado = consulta(conexion, consulta);
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
     *
     * @param filtro es el tipo de cursos
     * @param pagina es el número de cursos que se solicita, sirve para paginar
     * @param cantidad es la cantidad maxima de cursos que se regresarán
     * @return
     * @throws SQLException
     */
    public ArrayList<String> obten_cursos(String filtro, int pagina, int cantidad) throws SQLException {
        ArrayList<String> profesores = new ArrayList<String>();
        String consulta = "SELECT profesor_id, profesor_nombre, curso_id, curso_tipo, DATE_FORMAT(curso_inicio, '%H:%i') AS curso_inicio, DATE_FORMAT(curso_final, '%H:%i') AS curso_final "
                + "FROM `Escuela`.`Profesor` AS foo JOIN "
                + "`Escuela`.`Curso` AS bar ON foo.profesor_correo = bar.profesor_correo "
                + "WHERE curso_estado = \"Espera\" ";

        if (!filtro.trim().equals("")) {
            consulta += "AND curso_tipo = \"" + filtro.trim() + "\" ";
        }
        consulta += "LIMIT " + pagina * cantidad + ", " + cantidad + ";";
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado = consulta(conexion, consulta);
        if (resultado == null) {
            return null;
        }
        while (resultado.next()) {
//profesor_id, profesor_nombre, curso_id, curso_tipo, curso_inicio, curso_final          
            String cadena = "<tr>";
            cadena += "<td><a href=\"../profesor?id=" + resultado.getInt("profesor_id") + "\">" + resultado.getString("profesor_nombre") + "</a></td>";
            cadena += "<td>" + resultado.getString("curso_tipo") + "</td>";
            cadena += "<td>" + resultado.getString("curso_inicio") + " - " + resultado.getString("curso_final") + "</td>";
            cadena += "<td><a href='#' class='button success radius tiny solicitar_curso' data-profesor='" + resultado.getInt("profesor_id") + "' data-curso='" + resultado.getInt("curso_id") + "'>Solicitar</a></td>";
            cadena += "</tr>";
            profesores.add(new String(cadena));
        }
        cierraConexion(conexion);
        return profesores;
    }

    /**
     * Obtiene los cursos de cierto profesor
     *
     * @param id es el id del profesor
     * @param correo es el correo del profesor
     * @return la lista de los cursos que ofrece el profesor
     * @throws SQLException
     */
    public ArrayList<String> obten_cursos_profesor(int id, String correo) throws SQLException {
        ArrayList<String> cursos = new ArrayList<String>();
        String consulta = "SELECT curso_id, curso_tipo, DATE_FORMAT(curso_inicio, '%H:%i') AS curso_inicio, DATE_FORMAT(curso_final, '%H:%i') AS curso_final "
                + "  FROM `Escuela`.`Curso` WHERE curso_estado = \"Espera\" AND profesor_correo = \"" + correo + "\";";
        Connection conexion;
        conexion = getConexion();
        ResultSet resultado = consulta(conexion, consulta);
        if (resultado == null) {
            return null;
        }
        while (resultado.next()) {
//profesor_id, profesor_nombre, curso_id, curso_tipo, curso_inicio, curso_final          
            String cadena = "<tr>";
            cadena += "<td>" + resultado.getString("curso_tipo") + "</td>";
            cadena += "<td>" + resultado.getString("curso_inicio") + " - " + resultado.getString("curso_final") + "</td>";
            cadena += "<td><a href='#' class='button success radius tiny solicitar_curso' data-profesor='" + id + "' data-curso='" + resultado.getInt("curso_id") + "'>Solicitar</a></td>";
            cadena += "</tr>";
            cursos.add(new String(cadena));
        }
        cierraConexion(conexion);
        return cursos;
    }

    /**
     * Relaciona un curso con cierto alumno y lo pone en modo "Confirmando"
     *
     * @param estudiante_correo Correo del estudiante
     * @param curso_id es el id del curso
     * @return indicador si se pudo o no hacer la actualización en la base de
     * datos
     * @throws SQLException
     */
    public boolean solicitar_curso(String estudiante_correo, int curso_id) throws SQLException {
        String consulta_1 = "SELECT `curso_inicio`, `curso_final` FROM `Escuela`.`Curso` WHERE `curso_id`='" + curso_id + "';";
        Connection conexion = super.conectarBD();
        ResultSet resultado_1 = super.consulta(conexion, consulta_1);

        if (resultado_1 == null || !resultado_1.next())
            return false;

        String consulta_2 = "SELECT * FROM `Escuela`.`Curso` "
                + "WHERE `estudiante_correo`='" + estudiante_correo + "' AND (('" + resultado_1.getString(1) + "' BETWEEN curso_inicio AND curso_final) OR ('" + resultado_1.getString(2) +"' BETWEEN curso_inicio AND curso_final));";
        ResultSet resultado_2 = super.consulta(conexion, consulta_2);
        if (resultado_2 == null) {
            return false;
        }

        if (resultado_2.next())
            return false;

        String consulta = "UPDATE `Escuela`.`Curso` SET estudiante_correo='" + estudiante_correo + "', curso_estado='Confirmando' "
                + "WHERE `curso_id`='" + curso_id + "';";

        int resultado = super.actualiza(conexion, consulta);
        System.out.println(resultado);
        return resultado != 0;
    }

    public int crear_curso(String correo, String tinicio, String tfinal, String tipo) {
        String consulta_1 = "SELECT * FROM `Escuela`.`Profesor` WHERE `profesor_correo`='" + correo + "' AND (`profesor_url_certificado` IS NULL OR `profesor_url_video` IS NULL)";
        String consulta_2 = "SELECT * FROM `Escuela`.`Curso` "
                + "WHERE `profesor_correo`='" + correo + "' AND (('" + tinicio + "' BETWEEN curso_inicio AND curso_final) OR ('" + tfinal +"' BETWEEN curso_inicio AND curso_final));";

        String query = "INSERT INTO `Escuela`.`Curso` (`profesor_correo`, `estudiante_correo`, `curso_inicio`, `curso_final`, `curso_tipo`, "
                + "`curso_estado`, `curso_nota`, `curso_calificacion`) VALUES ('" + correo + "', NULL, '" + tinicio + "', '" + tfinal
                + "','" + tipo + "', 'Espera', NULL, NULL);";
        boolean encontrado = false;

        Connection conexion = super.conectarBD();
        ResultSet resultado_1 = super.consulta(conexion, consulta_1);
        ResultSet resultado_2 = super.consulta(conexion, consulta_2);

        if (resultado_1 == null || resultado_2 == null) {
            return 0;
        }

        try {
            if ((encontrado = resultado_1.next()))
                return 1;
            encontrado = resultado_2.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (encontrado) {
            return 0;
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
        return 2;
    }

    public boolean asignar_curso(String id, boolean asignar) {
        String query;
        if (asignar) {
            query = "UPDATE `Escuela`.`Curso` SET `curso_estado`='Cursando' WHERE `curso_id`='" + id + "';";
        } else {
            query = "UPDATE `Escuela`.`Curso` SET `curso_estado`='Espera', `estudiante_correo`=NULL WHERE `curso_id`='" + id + "';";
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

    public int eliminar_curso(String id) {
        int ex = -1;
        String query = "DELETE FROM `Escuela`.`Curso` WHERE `curso_id`='" + id + "';";
        Connection conexion;
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
        if (nota.equals("")) {
            query = "UPDATE `Escuela`.`Curso` SET `curso_calificacion`=" + Integer.parseInt(calificacion) + ", `curso_estado`='Terminado'";
            query += " WHERE `curso_id`='" + id + "';";
        } else {
            query = "UPDATE `Escuela`.`Curso` SET `curso_calificacion`=" + Integer.parseInt(calificacion) + ", `curso_estado`='Terminado', `curso_nota`='" + nota + "'";
            query += " WHERE `curso_id`='" + id + "';";
        }

        Connection conexion = super.conectarBD();
        try {
            Statement st = conexion.createStatement();
            st.execute(query);
            st.close();
            //Agregado por Marco, esto es para generar el pdf
            String consulta = "SELECT curso_id, "
                    + "profesor_nombre, "
                    + "estudiante_nombre, "
                    + "curso_tipo "
                    + "FROM "
                    + "(SELECT curso_id, "
                    + "profesor_correo, "
                    + "bar.estudiante_correo AS estudiante_correo, "
                    + "foo.curso_tipo, "
                    + "estudiante_nombre "
                    + "FROM `Escuela`.`Curso` AS foo "
                    + "LEFT JOIN "
                    + "`Escuela`.`Estudiante` AS bar "
                    + "ON foo.estudiante_correo = bar.estudiante_correo) AS foo "
                    + "LEFT JOIN "
                    + "`Escuela`.`Profesor` AS bar "
                    + "ON foo.profesor_correo = bar.profesor_correo "
                    + "WHERE `curso_id`= " + id + ";";
            ResultSet resultado = consulta(conexion, consulta);
            if (resultado == null) {
                System.out.println("Hubo un error");
            }
            if (resultado.next()) {
                int curso_ = resultado.getInt("curso_id");
                String estudiante_ = resultado.getString("estudiante_nombre");
                int promedio_ = Integer.parseInt(calificacion);
                String tipo_curso_ = resultado.getString("curso_tipo");
                String profesor_ = resultado.getString("profesor_nombre");
                controlador.GeneradorPDF.genera_pdf(curso_, estudiante_, promedio_, tipo_curso_, profesor_);
                //Agregado por Marco, esto es para actualizar la base con la liga al pdf
                String ruta =   id + ".pdf";
                consulta = "UPDATE `Escuela`.`Curso` SET `curso_url_certificado`='" + ruta + "' WHERE `curso_id`=" + id + ";";
                actualiza(conexion, consulta);
            }
            //--Termina el código agregado por Marco

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.desconectarBD(conexion);
        return true;
    }
}
