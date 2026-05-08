// @Autor: Hugo
package Controlador;

import javax.swing.JFrame;

import Modelo.Modelo;
import Vista.*;

public class Main {
		public static void main(String[] args) { 
			
		Controlador miControlador = new Controlador();
		Modelo miModelo = new Modelo();
		Vista[] misVistas = new Vista[18];
		
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


		
		
		miModelo.setVistas(misVistas);
		miControlador.setVista(misVistas);
		miControlador.setModelo(miModelo);
		
		for (Vista vista : misVistas) {
			vista.setModelo(miModelo);
			vista.setControlador(miControlador);
			((JFrame) vista).setVisible(false);
		}
		
		((JFrame) misVistas[0]).setVisible(true);
		
		
	}
}
