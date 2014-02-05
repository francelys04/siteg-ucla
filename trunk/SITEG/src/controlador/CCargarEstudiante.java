package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;

import modelo.Estudiante;
import modelo.Programa;
import modelo.seguridad.Usuario;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SEstudiante;
import servicio.SPrograma;
import servicio.seguridad.SUsuario;
import configuracion.GeneradorBeans;

@Controller
public class CCargarEstudiante extends CGeneral {
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();

	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

	@Wire
	private Window wdwCargarEstudiante;
	private File f;

	@Listen("onClick = #btnCargarListaEstudiantes")
	public void cargarEstudiante() {

		// variables locales
		String cedula, nombre, apellido, sexo, direccion, telefonomovil, telefonofijo, correo;
		boolean estatus;
		long idprograma;

		// abre el examinar para elegir el archivo
		javax.swing.JFileChooser j = new javax.swing.JFileChooser();

		// abre el txt
		int opcion = j.showOpenDialog(j);
		if (opcion == JFileChooser.APPROVE_OPTION) {
			String path = j.getSelectedFile().getAbsolutePath();

			f = new File(path);

			try {
				// empieza a leer el txt
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String linea = null;

				// si no esta vacio el txt empieza a leer hasta que no encuentre
				// linea
				while ((linea = br.readLine()) != null) {

					cedula = linea;

					linea = br.readLine();
					nombre = linea;

					linea = br.readLine();
					apellido = linea;

					linea = br.readLine();
					sexo = linea;

					linea = br.readLine();
					direccion = linea;

					linea = br.readLine();
					telefonomovil = linea;

					linea = br.readLine();
					telefonofijo = linea;

					linea = br.readLine();
					correo = linea;

					linea = br.readLine();
					estatus = Boolean.parseBoolean(linea);

					linea = br.readLine();
					idprograma = Long.parseLong(linea);

					Usuario usuario = servicioUsuario
							.buscarUsuarioPorNombre("");
					// busco el programa con el id que tengo en el txt para
					// registrar
					Programa p = new Programa();
					p = servicioPrograma.buscar(idprograma);
					// creo el estudiante y lo guardo
					Estudiante estudiante;

					estudiante = new Estudiante(cedula, nombre, apellido,
							correo, sexo, direccion, telefonomovil,
							telefonofijo, estatus, p, usuario);

					servicioEstudiante.guardar(estudiante);
				}
				br.close();
				fr.close();
				Messagebox.show("Estudiantes registrados exitosamente",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);

			} catch (Exception ex) {

				ex.printStackTrace();

				Messagebox
						.show("Debe elegir un archivo con el formato .txt para que los datos sean guardados correctamente",
								"Error", Messagebox.OK, Messagebox.ERROR);
			}
		}

		else {

			Messagebox.show("Busqueda de la lista de estudiantes cancelada",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);

		}
	}

	@Listen("onClick = #btnSalirCargarEstudiante")
	public void salirCargarEstudiante() {

		wdwCargarEstudiante.onClose();

	}

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
	}

	
}
