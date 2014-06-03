package controlador;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Correo {
    
    private Properties propiedades;
    private Session sesion;
    private Message mensaje;
    private BodyPart cuerpo;
    private BodyPart [] archivos;
    
    public Correo() {
        this("smtp.gmail.com", "587", "elementorsschoolofenglish@gmail.com", "IngenieriaSoftware2014");
    }
    
    public Correo(String servidor, String puerto, final String usuario, final String contrasenia) {
        propiedades = new Properties();
        propiedades.setProperty("mail.smtp.host", servidor);
        propiedades.setProperty("mail.smtp.starttls.enable", "true");
        propiedades.setProperty("mail.smtp.port", puerto);
        propiedades.setProperty("mail.smtp.auth", "true");
        
        sesion = Session.getInstance(propiedades, new javax.mail.Authenticator() {
                                                    protected PasswordAuthentication getPasswordAuthentication() {
                                                        return new PasswordAuthentication(usuario, contrasenia);
                                                    }
                                                  });
        mensaje = new MimeMessage(sesion);
        cuerpo = new MimeBodyPart();
        archivos = null;
    }
    
    public void cuerpo_Correo(String remitente, String destinatario, String asunto, String cuerpo) {
        try {
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mensaje.setSubject(asunto);
            this.cuerpo.setText(cuerpo);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void cuerpo_Correo(String destinatario, String asunto, String cuerpo) {
        this.cuerpo_Correo("elementorsschoolofenglish@gmail.com", destinatario, asunto, cuerpo);
    }
    
    public void adjuntar_Archivo(String ... archivos) {
        this.archivos = new MimeBodyPart[archivos.length];
        for (int i = 0; i < archivos.length; i++) {
            this.archivos[i] = new MimeBodyPart();
            try {
                this.archivos[i].setDataHandler(new DataHandler(new FileDataSource(archivos[i])));
                this.archivos[i].setFileName(new File(archivos[i]).getName());
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void enviar_Correo() {
        MimeMultipart multiParte = new MimeMultipart();
        try {
            multiParte.addBodyPart(cuerpo);
            if (archivos != null) {
                for (int i = 0; i < archivos.length; i++)
                    multiParte.addBodyPart(archivos[i]);
            }
            
            mensaje.setContent(multiParte);
            Transport.send(mensaje);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}