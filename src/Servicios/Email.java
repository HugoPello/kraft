/**
 * Clase de servicio de alto nivel para la gestión de comunicaciones por correo.
 * Actúa como un envoltorio (wrapper) de EmailService para facilitar el envío 
 * de códigos de verificación OTP con un formato predefinido.
 * 
 * @author Hugo
 */
package Servicios;

public class Email {
	
	/**
     * Configura y dispara el envío de un correo electrónico con el código de seguridad.
     * Define las credenciales de acceso, el asunto y el cuerpo del mensaje antes
     * de delegar el envío físico a la clase EmailService.
     * 
     * @param emailDestino Dirección de correo del usuario que se está registrando.
     * @param codigoOTP Código alfanumérico generado por el Trigger de la base de datos.
     */
	public void enviarCorreoOTP(String emailDestino, String codigoOTP) {
	    try {
	    	// Instanciamos el servicio utilizando las credenciales de la App Password de Google.
	        EmailService mail = new EmailService("kraftutad@gmail.com", "REDACTED");
	        
	        // Configuración del contenido del mensaje para el sistema 2FA
	        String asunto = "Código de Verificación - Proyecto 2FA";
	        String mensaje = "Tu código de seguridad es: " + codigoOTP + "\nExpira en 5 minutos.";
	        
	        // Envia el mensaje con los parametros que hemos obtenido anteriormente
	        mail.sendEmail(emailDestino, asunto, mensaje);
	        System.out.println("Correo enviado con éxito a: " + emailDestino);
	        
	    } catch (Exception e) {
	        System.err.println("Error al enviar el correo: " + e.getMessage());
	    }
	}
}

