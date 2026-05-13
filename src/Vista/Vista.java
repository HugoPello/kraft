/**
 * Interfaz fundamental del patrón Modelo-Vista-Controlador (MVC).
 * Define el contrato de comunicación que deben implementar todas las ventanas 
 * de la aplicación. Su objetivo es permitir la inyección de dependencias de 
 * las capas de lógica y control de forma estandarizada.
 * 
 * @author Hugo
 * @version 1.0
 */
package Vista;

import Controlador.Controlador;
import Modelo.Modelo;

public interface Vista {
	
	/**
     * Vincula la vista con la capa de datos.
     * Permite que la interfaz gráfica pueda consultar el estado de la sesión,
     * las estadísticas del usuario o los resultados de las consultas SQL.
     * 
     * @param miModelo Instancia única de la lógica de negocio.
     */
	public void setModelo (Modelo miModelo);
	
	/**
     * Vincula la vista con la capa de control.
     * Permite que la interfaz notifique las acciones del usuario (clics en botones,
     * envío de formularios) para que el controlador ejecute la lógica correspondiente.
     * 
     * @param miControlador Instancia del orquestador de eventos y navegación.
     */
	public void setControlador (Controlador miControlador);

}
