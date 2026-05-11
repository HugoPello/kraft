/**
 * Motor de mensajería de bajo nivel encargado de la comunicación con el servidor SMTP.
 * Utiliza el protocolo TLS y la librería Jakarta Mail para el envío de correos electrónicos.
 * Esta clase es el núcleo técnico que permite la funcionalidad de Doble Factor de Autenticación (2FA).
 * 
 * @author Hugo
 */

package Servicios;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private final String username;
    private final String password;
    private final Session session;

    /**
     * Configura la sesión de correo estableciendo las propiedades del servidor de Google.
     * Implementa un objeto Authenticator para gestionar las credenciales de forma segura.
     * 
     * @param username Cuenta de Gmail emisora (ej: krafutad@gmail.com)
     * @param password App Password de 16 caracteres generada en la cuenta de Google
     */
    public EmailService(final String username, final String password) {
        this.username = username;
        this.password = password;

        // Configuración de propiedades del protocolo SMTP para Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Configuración para Gmail
        props.put("mail.smtp.port", "587");

        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Construye y envía un mensaje MIME de forma síncrona.
     * 
     * @param to Dirección de correo del destinatario.
     * @param subject Asunto del mensaje (ej: Código de Verificación).
     * @param messageText Cuerpo del mensaje que contiene el código OTP.
     * @throws MessagingException Si ocurre un error en la conexión o las credenciales son rechazadas.
     */
    public void sendEmail(String to, String subject, String messageText) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(messageText);

        Transport.send(message);
    }
}