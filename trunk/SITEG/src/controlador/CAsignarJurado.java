package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.Condicion;

import modelo.Defensa;
import modelo.Estudiante;

import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TipoJurado;
import modelo.compuesta.Jurado;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SCondicionPrograma;
import servicio.SDefensa;
import servicio.SJurado;
import servicio.STeg;
import servicio.STipoJurado;
import servicio.seguridad.SGrupo;

@Controller
public class CAsignarJurado extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SDefensa servicioDefensa = GeneradorBeans.getServicioDefensa();
	SJurado servicioJurado = GeneradorBeans.getServicioJurado();
	STipoJurado servicioTipoJurado = GeneradorBeans.getServicioTipoJurado();
	SCondicionPrograma servicioCondicionPrograma = GeneradorBeans
			.getServicioCondicionPrograma();
	SGrupo serviciogrupo = GeneradorBeans.getServicioGrupo();

	@Wire
	private Textbox txtProgramaAtenderDefensa;
	@Wire
	private Textbox txtAreaAtenderDefensa;
	@Wire
	private Textbox txtTematicaAtenderDefensa;
	@Wire
	private Textbox txtTituloAtenderDefensa;
	@Wire
	private Label lblCondicionAtenderDefensa;
	@Wire
	private Listbox ltbJuradoDisponible;
	@Wire
	private Listbox ltbJuradoSeleccionado;
	@Wire
	private Listbox ltbEstudiantesAtenderDefensa;
	@Wire
	private Datebox dtbFechaAtenderDefensa;
	@Wire
	private Window wdwAsignarJurado;
	@Wire
	private Textbox txtTutorAsignarJurado;
	// @Wire
	// private Combobox cmbDefensaTipoJurado;
	private static String vistaRecibida;
	private static String cedula;
	private static String tipoJurado;
	long idTeg = 0;
	long idDefensa = 0;
	private static Programa programa;
	private static int numeroIntegrantes;
	@Wire
	private Image imagenx;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private List<Profesor> profesores;
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;
	public static int j;
	List<Profesor> juradoDisponible= new ArrayList();
	List<Jurado> juradoOcupado= new ArrayList();

	Jurado jurado = new Jurado();

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitudDefensa");
		if (map != null) {
			if (map.get("id") != null) {
				idTeg = (Long) map.get("id");
				Teg teg = servicioTeg.buscarTeg(idTeg);
				llenarListas(teg);

				
				txtTutorAsignarJurado.setValue(teg.getTutor().getNombre() + " " + teg.getTutor().getApellido());
				
				txtAreaAtenderDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaAtenderDefensa.setValue(teg.getTematica()
						.getNombre());
				// para que se guie por el programa del estudiante
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg);
				programa = est.get(0).getPrograma();
				txtProgramaAtenderDefensa.setValue(est.get(0).getPrograma()
						.getNombre());
				txtTituloAtenderDefensa.setValue(teg.getTitulo());
				lblCondicionAtenderDefensa
						.setValue("Recuerde que la cantidad de integrantes del juarado es de: "
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes del jurado",
										programa).getValor());

			}
		}
	}

	// metodo que permite mover un profesor de disponible a seleccionado
	@Listen("onClick = #btnAgregarJurado")
	public void moverDerechaJurado() {
		
		List<Integer> itemEliminar= new ArrayList();
	    List<Listitem> listitemEliminar= new ArrayList();
	    List<Listitem> listItem= ltbJuradoDisponible.getItems();
		if(listItem.size()!=0){
		for(int i=0;i<listItem.size();i++){
			
			if(listItem.get(i).isSelected()){
				
			Profesor profesor=listItem.get(i).getValue();
			System.out.println("profesor:"+profesor.getNombre());
			juradoDisponible.remove(profesor);
			Jurado jurado=new Jurado();
			jurado.setProfesor(profesor);
			juradoOcupado.add(jurado);
			ltbJuradoSeleccionado.setModel(new ListModelList<Jurado>(
					juradoOcupado));
			

			listitemEliminar.add(listItem.get(i));
			
			}
			
		}
		}
		for(int i=0;i<listitemEliminar.size();i++){
			ltbJuradoDisponible.removeItemAt(listitemEliminar.get(i).getIndex());
		}
	
		 
	//	if(!ltbActividadesSeleccionadas.isCheckmark() && !ltbActividadesSeleccionadas.isMultiple()){
			ltbJuradoSeleccionado.setMultiple(false);
			ltbJuradoSeleccionado.setCheckmark(false);
			ltbJuradoSeleccionado.setMultiple(true);
			ltbJuradoSeleccionado.setCheckmark(true);

	}

	// metodo que permite mover un profesor de seleccionado a disponible
	@Listen("onClick = #btnRemoverJurado")
	public void moverIzquierdaJurado() {
		
		List<Integer> itemEliminar= new ArrayList();
	    List<Listitem> listitemEliminar= new ArrayList();
	  
	    List<Listitem> listItem2= ltbJuradoSeleccionado.getItems();
		if(listItem2.size()!=0){
		for(int i=0;i<listItem2.size();i++){
			
			if(listItem2.get(i).isSelected()){
				
			Jurado jurado=listItem2.get(i).getValue();
			System.out.println("jurado:"+jurado.getProfesor().getNombre());
			juradoOcupado.remove(jurado);
			Profesor profesor=new Profesor();
			profesor.setNombre(jurado.getProfesor().getNombre());
			juradoDisponible.add(profesor);
			ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
					juradoDisponible));
			 

			listitemEliminar.add(listItem2.get(i));
			
//			ltbActividadesDisponibles.removeItemAt(i);
//			i=0;
			}
			
		}
		}	
		for(int i=0;i<listitemEliminar.size();i++){
			ltbJuradoSeleccionado.removeItemAt(listitemEliminar.get(i).getIndex());
		}
		//if(!ltbActividadesDisponibles.isCheckmark() && !ltbActividadesDisponibles.isMultiple()){
			ltbJuradoDisponible.setMultiple(false);
			ltbJuradoDisponible.setCheckmark(false);
			ltbJuradoDisponible.setMultiple(true);
			ltbJuradoDisponible.setCheckmark(true);
		//}

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// Metodo para llenar las listas de jurados tanto disponibles (para ser
	// seleccionado)
	// como los que ya han sido seleccionado en un teg (se los trae de la bd)
	public void llenarListas(Teg teg) {
		List<Estudiante> estudiantesTeg = servicioEstudiante
				.buscarEstudiantePorTeg(teg);
		ltbEstudiantesAtenderDefensa.setModel(new ListModelList<Estudiante>(
				estudiantesTeg));
		juradoDisponible = servicioProfesor
				.buscarProfesorJuradoDadoTeg(teg);
		ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
				juradoDisponible));
		juradoOcupado = servicioJurado.buscarJuradoDeTeg(teg);
		ltbJuradoSeleccionado
				.setModel(new ListModelList<Jurado>(juradoOcupado));
		ltbJuradoSeleccionado.setDisabled(true);
		numeroIntegrantes = juradoOcupado.size();
		ltbJuradoSeleccionado.setMultiple(false);
		ltbJuradoSeleccionado.setCheckmark(false);
		ltbJuradoSeleccionado.setMultiple(true);
		ltbJuradoSeleccionado.setCheckmark(true);
		ltbJuradoDisponible.setMultiple(false);
		ltbJuradoDisponible.setCheckmark(false);
		ltbJuradoDisponible.setMultiple(true);
		ltbJuradoDisponible.setCheckmark(true);

	}

	// Metodo que permite finalizar la asignacion del jurado
	@Listen("onClick = #btnAceptarDefensa")
	public void aceptarDefensaDefinitiva() {
		if (validarJurado() && guardardatos()) {
			Teg teg = servicioTeg.buscarTeg(idTeg);
			teg.setEstatus("Jurado Asignado");
			servicioTeg.guardar(teg);
			Messagebox.show("Solicitud de Defensa finalizada exitosamente",
					"Información", Messagebox.OK, Messagebox.INFORMATION);
			salir();
		} else {
			Messagebox.show(
					"El numero de profesores en el jurado no es el correcto, debe ser: "
							+ buscarCondicionVigenteEspecifica(
									"Numero de integrantes del jurado", programa)
									.getValor(), "Error", Messagebox.OK,
					Messagebox.ERROR);
		}
//		int valorcondicion = buscarCondicionVigenteEspecifica(
//				"Numero de integrantes del jurado", programa).getValor();
//		int valorItem = ltbJuradoSeleccionado.getItemCount();
//		if (valorItem != valorcondicion) {
//
//			Messagebox.show("El numero de profesores no es correcto",
//					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
//
//		} else {
//			boolean tipoBlanco = false;
//			for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
//				Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
//				tipoJurado = ((Combobox) ((listItem.getChildren().get(3)))
//						.getFirstChild()).getValue();
//
//				if (tipoJurado == "") {
//					tipoBlanco = true;
//				}
//			}
//			if (tipoBlanco == true) {
//				Messagebox.show("Debe seleccionar un tipo para cada jurado",
//						"Error", Messagebox.OK, Messagebox.ERROR);
//			} else {
//				for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
//					Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
//					tipoJurado = ((Combobox) ((listItem.getChildren().get(3)))
//							.getFirstChild()).getValue();
//					cedula = ((Label) ((listItem.getChildren().get(0)))
//							.getFirstChild()).getValue();
//
//					Set<Grupo> gruposUsuario = new HashSet<Grupo>();
//					Grupo grupo = servicioGrupo.BuscarPorNombre("ROLE_JURADO");
//					gruposUsuario.add(grupo);
//					byte[] imagenUsuario = null;
//					URL url = getClass().getResource(
//							"/configuracion/usuario.png");
//					try {
//						imagenx.setContent(new AImage(url));
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					imagenUsuario = imagenx.getContent().getByteData();
//
//					Set<Profesor> profesorSeleccionado = new HashSet<Profesor>();
//					for (int k = 0; k < ltbJuradoSeleccionado.getItemCount(); k++) {
//						Profesor profesor = ltbJuradoSeleccionado.getItems()
//								.get(k).getValue();
//						profesorSeleccionado.add(profesor);
//						Usuario user = servicioUsuario
//								.buscarUsuarioPorNombre(profesor.getCedula());
//						if (user == null) {
//							Usuario usuario = new Usuario(
//									0,
//									profesor.getCedula(),
//									passwordEncoder.encode(profesor.getCedula()),
//									true, gruposUsuario, imagenUsuario);
//							servicioUsuario.guardar(usuario);
//							user = servicioUsuario
//									.buscarUsuarioPorNombre(profesor
//											.getCedula());
//							profesor.setUsuario(user);
//							String mensaje = "Usted foma parte del Jurado su usuario: "
//									+ usuario.getNombre()
//									+ " y contraseÃ±a: "
//									+ usuario.getPassword();
//							enviarEmailNotificacion(
//									profesor.getCorreoElectronico(), mensaje);
//							servicioProfesor.guardarProfesor(profesor);
//						} else {
//
//							List<Grupo> grupino = new ArrayList<Grupo>();
//							grupino = serviciogrupo
//									.buscarGruposDelUsuario(user);
//							Grupo grupo2 = servicioGrupo
//									.BuscarPorNombre("ROLE_JURADO");
//							Set<Grupo> gruposU = new HashSet<Grupo>();
//							for (int f = 0; f < grupino.size(); ++f) {
//								Grupo g = grupino.get(f);
//								System.out.println(grupino.get(f).getNombre());
//								gruposU.add(g);
//							}
//							gruposU.add(grupo2);
//
//							user.setGrupos(gruposU);
//
//							servicioUsuario.guardar(user);
//						}
//					}
//
//					List<Jurado> jurados = new ArrayList<Jurado>();
//					Teg teg1 = servicioTeg.buscarTeg(idTeg);
//					long cedula1 = Long.parseLong(cedula);
//					Profesor profesorJurado = servicioProfesor
//							.buscarProfesorPorCedula(String.valueOf(cedula1));
//					TipoJurado tipo = servicioTipoJurado
//							.buscarPorNombre(tipoJurado);
//					Jurado jurado = new Jurado(teg1, profesorJurado, tipo);
//					jurados.add(jurado);
//					servicioJurado.guardar(jurados);
//					Teg teg = servicioTeg.buscarTeg(idTeg);
//					teg.setEstatus("Jurado Asignado");
//					servicioTeg.guardar(teg);
//					Messagebox
//							.show("Los datos del jurado fueron guardados exitosamente",
//									"Informacion", Messagebox.OK,
//									Messagebox.INFORMATION);
//
//					salir();
//
//				}
//			}
//
//		}

		
		
	}

	@Listen("onClick = #btnAceptarDefensaMientrasTanto")
	public void aceptarDefensa() {
		if (guardardatos()) {
			Messagebox
					.show("Solicitud de Defensa guardada para futuras modificaciones",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			salir();
		}
	}
	
	public boolean validarJurado() {
		if (ltbJuradoSeleccionado.getItemCount() == buscarCondicionVigenteEspecifica(
				"Numero de integrantes del jurado", programa).getValor()) {
			System.out.println("aqui");
			return true;
		} else
			System.out.println("alla");
		return false;
	}
	
	public boolean guardardatos() {
		Teg teg = servicioTeg.buscarTeg(idTeg);
		List<Jurado> jurados = new ArrayList<Jurado>();
		boolean error = false;
		for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
			Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
			String tipojurado = ((Combobox) ((listItem.getChildren().get(3)))
					.getFirstChild()).getValue();
			if (tipojurado == "") {
				error = true;
			}
			long cedula = ((Spinner) ((listItem.getChildren().get(0)))
					.getFirstChild()).getValue();
			Profesor profesorJurado = servicioProfesor
					.buscarProfesorPorCedula(String.valueOf(cedula));
			TipoJurado tipo = servicioTipoJurado.buscarPorNombre(tipojurado);
			Jurado jurado = new Jurado(teg, profesorJurado, tipo);
			jurados.add(jurado);
		}
		if (!error) {
			servicioJurado.guardar(jurados);
			return true;
		} else {
			Messagebox.show("Debe seleccionar un tipo para cada jurado",
					"Error", Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}
	
	@Listen("onClick = #btnCancelarDefensa")
	public void cancelarDefensa() {
		Teg teg = servicioTeg.buscarTeg(idTeg);
		List<Profesor> juradoDisponible = servicioProfesor
				.buscarProfesorJuradoDadoTeg(teg);
		ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
				juradoDisponible));
		ltbJuradoSeleccionado.getItems().clear();

	}

	public void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAsignarJurado.onClose();
	}

	@Listen("onClick = #btnSalirAsignarJurado")
	public void salirAsignarJurado() {
		salir();
	}

}
