package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

import modelo.Categoria;
import modelo.Profesor;
import modelo.Tematica;
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
public class CCargarProfesor extends CGeneral {

	private static final long serialVersionUID = -3323071040844703888L;
	@Wire
	private Window wdwCargarProfesor;
	@Wire
	private Listbox ltbProfesoresCargados;
	private static List<Profesor> profesoresCargados;
	private File f;

	/*
	 * Metodo que permite buscar el archivo plano de los profesores en la
	 * computadora y los carga en una lista
	 */
	@Listen("onClick = #btnCargarListaProfesores")
	public void cargarProfesores() {

		String cedula, nombre, apellido, sexo, direccion, telefonomovil, telefonofijo, correo;
		boolean estatus;
		long idcategoria;

		javax.swing.JFileChooser j = new javax.swing.JFileChooser();

		int opcion = j.showOpenDialog(j);
		if (opcion == JFileChooser.APPROVE_OPTION) {
			String path = j.getSelectedFile().getAbsolutePath();

			f = new File(path);

			try {

				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String linea = null;
				profesoresCargados = new ArrayList<Profesor>();

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
					idcategoria = Long.parseLong(linea);

					Usuario usuario = servicioUsuario
							.buscarUsuarioPorNombre(cedula);

					Categoria categoria = new Categoria();
					categoria = servicioCategoria.buscarPorId(idcategoria);

					Profesor profesor;

					Set<Tematica> tematicasProfesor = new HashSet<Tematica>();
					profesor = new Profesor(cedula, nombre, apellido, correo,
							sexo, direccion, telefonomovil, telefonofijo,
							estatus, categoria, tematicasProfesor, usuario);

					profesoresCargados.add(profesor);
				}
				br.close();
				fr.close();

				ltbProfesoresCargados.setModel(new ListModelList<Profesor>(
						profesoresCargados));

			} catch (Exception ex) {

				ex.printStackTrace();

				Messagebox
						.show("Debe elegir un archivo con el formato .txt para que los datos sean guardados correctamente",
								"Error", Messagebox.OK, Messagebox.ERROR);
			}
		}

		else {

			Messagebox.show("Busqueda de la lista de profesores cancelada",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);

		}
	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirCargarProfesor")
	public void salirCargarProfesor() {

		wdwCargarProfesor.onClose();

	}

	/* Metodo que permite limpiar los campos de la vista */
	@Listen("onClick = #btnCancelarCargarProfesor")
	public void cancelarCargarProfesor() {

		ltbProfesoresCargados.getItems().clear();
		ltbProfesoresCargados.setModel(new ListModelList<Profesor>());

	}

	/*
	 * Metodo que permite guardar a los profesores que se encuentran en la lista
	 */
	@Listen("onClick = #btnGuardarCargarProfesor")
	public void CargarEstudiante() {

		if (ltbProfesoresCargados.getItemCount() != 0) {

			Messagebox.show("¿Desea guardar los datos de los profesores?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								for (int i = 0; i < profesoresCargados.size(); i++) {

									Profesor profesor = profesoresCargados
											.get(i);
									servicioProfesor.guardarProfesor(profesor);

								}

								Messagebox
										.show("Datos de los profesores guardados esxitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
								cancelarCargarProfesor();

							}
						}
					});

		} else {

			Messagebox
					.show("Debe seleccionar la lista de profesores que desea registrar",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

		}

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
