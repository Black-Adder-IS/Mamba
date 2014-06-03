/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;
import java.net.URL;

/**
 *
 * @author sainoba
 */
public class GeneradorPDF {

    static Font font1 = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
    static Font font2 = new Font(Font.FontFamily.HELVETICA, 18);
    static Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 16);

    /**
     *
     * @param curso_id
     * @param nombre_estudiante
     * @param promedio
     * @param tipo_curso
     * @param nombre_profesor
     * @return
     */
    public static boolean genera_pdf(int curso_id, String nombre_estudiante, int promedio, String tipo_curso, String nombre_profesor) {
        boolean exito = true;
        Document document = new Document();
        Paragraph titulo_ = new Paragraph("Certificado de acreditación", font1);
        Paragraph estudiante_ = new Paragraph("Estudiante: " + nombre_estudiante, font2);
        Paragraph promedio_ = new Paragraph("Calificación: " + promedio, font2);
        Paragraph profesor_ = new Paragraph("Curso de tipo " + tipo_curso
                + "\nImpartido por " + nombre_profesor);
        titulo_.setAlignment(Element.ALIGN_CENTER);
        estudiante_.setAlignment(Element.ALIGN_CENTER);
        promedio_.setAlignment(Element.ALIGN_CENTER);
        profesor_.setAlignment(Element.ALIGN_CENTER);

        try {
            PdfWriter.getInstance(document, new FileOutputStream("/home/jesus/Mamba/build/web/"+curso_id+".pdf"));
            document.open();
            document.add(titulo_);
            document.add(estudiante_);
            document.add(promedio_);
            document.add(profesor_);
            try {
                //Image image1 = Image.getInstance("../../../web/images/logo.png");
                URL url = GeneradorPDF.class.getResource("/WebApplication1/README.md");
                System.out.println(System.getProperty("user.dir"));
                //Image image1 = Image.getInstance(url);
                //document.add(image1);
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (FileNotFoundException | DocumentException e) {
            exito = false;
        } finally {
            document.close();
        }
        System.out.println("Generado del pdf: " + exito);
        return exito;
    }
}
