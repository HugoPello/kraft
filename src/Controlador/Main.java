/**
 * Clase principal que actúa como el punto de entrada (Entry Point) de la aplicación.
 * Se encarga de instanciar las capas del patrón MVC, inicializar el array de vistas
 * y establecer las dependencias cruzadas entre el Modelo, el Controlador y las Vistas.
 * 
 * @author Hugo
 * @version 1.0
 */
package Controlador;

import javax.swing.JFrame;

import Modelo.Modelo;
import Vista.Vista;
import Vista._00_Login;
import Vista._01_Registro;
import Vista._02_Inicio;
import Vista._03_Actividades;
import Vista._04_DentroActividad;
import Vista._05_Logros;
import Vista._06_Ranking;
import Vista._07_Tienda;
import Vista._08_PerfilUsuario;
import Vista._09_OlvidoContraseña;
import Vista._10_CrearEventoAdmin;
import Vista._11_PanelAdmin;
import Vista._12_VistaDeEvento;
import Vista._13_AjustesUsuario;
import Vista._14_CrearActividad;
import Vista._15_Configuracion;
import Vista._16_Grupos;
import Vista._17_CrearGrupo;
import Vista._18_VerificarOTP;

public class Main {
	
		/**
	     * Método principal que arranca el hilo de ejecución de la interfaz gráfica.
	     * Gestiona la inicialización de las 19 ventanas del proyecto y define
	     * el estado inicial de visibilidad del sistema.
	     * 
	     */
		public static void main(String[] args) { 
			
		Controlador miControlador = new Controlador();
		Modelo miModelo = new Modelo();
		// Inicialización del contenedor de vistas
		Vista[] misVistas = new Vista[19];
		
		// Cada índice corresponde a una funcionalidad específica de la aplicación
		misVistas[0] = new _00_Login();
		misVistas[1] =new _01_Registro();
		misVistas[2] =new _02_Inicio();
		misVistas[3] =new _03_Actividades();
		misVistas[4] =new _04_DentroActividad();
		misVistas[5] =new _05_Logros();
		misVistas[6] =new _06_Ranking();
		misVistas[7] =new _07_Tienda();
		misVistas[8] =new _08_PerfilUsuario();
		misVistas[9] =new _09_OlvidoContraseña();
		misVistas[10] =new _10_CrearEventoAdmin();
		misVistas[11] =new _11_PanelAdmin();
		misVistas[12] =new _16_Grupos();
		misVistas[13] = new _12_VistaDeEvento();
		misVistas[14] = new _13_AjustesUsuario();
		misVistas[15] = new _14_CrearActividad();
		misVistas[16] = new _15_Configuracion();
		misVistas[17] = new _17_CrearGrupo();
		misVistas[18] = new _18_VerificarOTP();


		// Establecemos los vínculos necesarios para que las capas puedan comunicarse entre sí
		miModelo.setVistas(misVistas);
		miControlador.setVista(misVistas);
		miControlador.setModelo(miModelo);
		
		// Recorremos el array para asignar el Modelo y el Controlador a cada vista
        // y asegurar que todas inicien ocultas mediante un casting a JFrame
		for (Vista vista : misVistas) {
			vista.setModelo(miModelo);
			vista.setControlador(miControlador);
			((JFrame) vista).setVisible(false);
		}
		
		// Iniciamos el flujo de la aplicación mostrando únicamente el Login
		((JFrame) misVistas[0]).setVisible(true);
		
		
	}
}