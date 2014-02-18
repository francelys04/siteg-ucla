package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import modelo.Estudiante;
import modelo.Programa;
import modelo.seguridad.Usuario;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/*
 * Controlador que permite almacenar en la base de datos un conjunto de
 * estudiantes desde un archivo plano
 */
@Controller
public class CCargarEstudiante extends CGeneral {

	@Wire
	private Listbox ltbEstudiantesCargados;
	@Wire
	private Window wdwCargarEstudiante;
	private File f;
	private static List<Estudiante> estudiantesCargados;

	/*
	 * Metodo que permite buscar el archivo plano de los estudiantes en la
	 * computadora y los carga en una lista
	 */
	@Listen("onClick = #btnCargarListaEstudiantes")
	public void cargarEstudiante() {

		String cedula, nombre, apellido, sexo, direccion, telefonomovil, telefonofijo, correo;
		boolean estatus;
		long idprograma;
		javax.swing.JFileChooser j = new javax.swing.JFileChooser();

		int opcion = j.showOpenDialog(j);
		if (opcion == JFileChooser.APPROVE_OPTION) {
			String path = j.getSelectedFile().getAbsolutePath();

			f = new File(path);

			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String linea = null;
				estudiantesCargados = new ArrayList<Estudiante>();
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
							.buscarUsuarioPorNombre(cedula);
					Programa programa = new Programa();
					programa = servicioPrograma.buscar(idprograma);
					Estudiante estudiante;

					estudiante = new Estudiante(cedula, nombre, apellido,
							correo, sexo, direccion, telefonomovil,
							telefonofijo, estatus, programa, usuario);

					estudiantesCargados.add(estudiante);

				}
				br.close();
				fr.close();

				ltbEstudiantesCargados.setModel(new ListModelList<Estudiante>(
						estudiantesCargados));

			} catch (Exception ex) {

				ex.printStackTrace();

				Messagebox
						.show("Debe elegir un archivo con el formato .txt para que los datos sean cargados correctamente",
								"Error", Messagebox.OK, Messagebox.ERROR);
			}
		}

		else {

			Messagebox.show("Busqueda de la lista de estudiantes cancelada",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);

		}
	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirCargarEstudiante")
	public void salirCargarEstudiante() {

		wdwCargarEstudiante.onClose();

	}

	/*
	 * Metodo que permite guardar a los estudiantes que se encuentran en la
	 * lista
	 */
	@Listen("onClick = #btnGuardarCargarEstudiante")
	public void CargarEstudiante() {

		Messagebox.show("¿Desea guardar los datos de los estudiantes?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							for (int i = 0; i < estudiantesCargados.size(); i++) {

								Estudiante estudiante = estudiantesCargados
										.get(i);
								servicioEstudiante.guardar(estudiante);

							}

							Messagebox
									.show("Datos de los estudiantes guardados esxitosamente",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
							ltbEstudiantesCargados.getItems().clear();

						}
					}
				});

	}

	/*
	 * Metodo heredado del Controlador CGeneral, en este caso no hay
	 * instrucciones dentro ya que no se cargan variables al iniciar esta vista
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

	}

}
