package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;
import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Categoria;
import modelo.Programa;
import modelo.Usuario;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Messagebox;
import servicio.SCategoria;
import servicio.SProfesor;
import servicio.SUsuario;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

@Controller
public class CCargarProfesor extends CGeneral {
	SProfesor servicioProfesor = GeneradorBeans
			.getServicioProfesor();
	SCategoria servicioCategoria = GeneradorBeans
			.getServicioCategoria();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	
	private File f;

	@Listen("onClick = #btnCargarListaProfesores")
	public void cargarProfesores() {

		// variables locales
		String cedula, nombre, apellido, sexo, direccion, telefonomovil, telefonofijo, correo, usuario;
		boolean estatus;
		long idcategoria, idprograma;

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
					idcategoria = Long.parseLong(linea);
					
					linea = br.readLine();
					idprograma = Long.parseLong(linea);
					
					linea = br.readLine();
					usuario = linea;

					// busco el programa con el id que tengo en el txt para
					// registrar
					Categoria categoria = new Categoria();
					categoria = servicioCategoria.buscarPorId(idcategoria);
					
					// busco el programa con el id que tengo en el txt para
					// registrar
					
					Programa programa = new Programa();
					programa = servicioPrograma.buscar(idprograma);
					
					// creo el estudiante y lo guardo
					Profesor profesor;
					Usuario user = servicioUsuario.buscarUsuarioPorNombre(usuario);
					Set<AreaInvestigacion> areasProfesor = new HashSet<AreaInvestigacion>();
					profesor = new Profesor(cedula, nombre, apellido, correo, sexo,
							direccion, telefonomovil, telefonofijo,
							estatus, categoria, programa, areasProfesor, user);

					servicioProfesor.guardarProfesor(profesor);
				}
				br.close();
				fr.close();
				Messagebox.show("Profesores registrados exitosamente",
						"Información", Messagebox.OK, Messagebox.INFORMATION);

			} catch (Exception ex) {

				ex.printStackTrace();

				Messagebox
						.show("Debe elegir un archivo con el formato .txt para que los datos sean guardados correctamente",
								"Error", Messagebox.OK, Messagebox.ERROR);
			}
		}

		else {

			Messagebox.show("Búsqueda de la lista de profesores cancelada", "Warning",
					Messagebox.OK, Messagebox.EXCLAMATION);

		}
	}

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
	}
}

