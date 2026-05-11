//@Autor: Hugo

package Servicios;

public class Email {
	public void enviarCorreoOTP(String emailDestino, String codigoOTP) {
	    try {
	        // Usa tu cuenta de Gmail y el código de 16 caracteres de Google
	        EmailService mail = new EmailService("kraftutad@gmail.com", "REDACTED");
	        
	        String asunto = "Código de Verificación - Proyecto 2FA";
	        String mensaje = "Tu código de seguridad es: " + codigoOTP + "\nExpira en 5 minutos.";
	        
	        mail.sendEmail(emailDestino, asunto, mensaje);
	        System.out.println("Correo enviado con éxito a: " + emailDestino);
	        
	    } catch (Exception e) {
	        System.err.println("Error al enviar el correo: " + e.getMessage());
	    }
	}
}

