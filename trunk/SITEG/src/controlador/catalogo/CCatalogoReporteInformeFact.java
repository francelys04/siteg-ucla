package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.STeg;
import configuracion.GeneradorBeans;
import controlador.CGeneral;


public class CCatalogoReporteInformeFact extends CGeneral {

	private static String vistaReporte;

	@Wire
	private Listbox ltbListaInformeFactibilidad;
	@Wire
	private Window wdwCatalogoReporteInformeFact;
	@Wire
	private Textbox txtEstudianteInformeFactibilidad;
	@Wire
	private Textbox txtTituloInformeFactibilidad;
	
	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los tegs
	 * disponibles mediante el metodo "buscarDatos()", recorriendolo uno a uno
	 * para luego cargar una lista de estudiantes por teg donde mediante la
	 * implementacion del servicio de busqueda se va obteniendo su nombre y su
	 * apellido y se va seteando temporalmente en la variable estatus del teg
	 * para poder visualizarlo en el componente lista de teg de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Teg> tegs = buscarDatos();
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> estudiantes = servicioEstudiante.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = estudiantes.get(0).getNombre();
			String apellido = estudiantes.get(0).getApellido();
			tegs.get(i).setEstatus(nombre+" "+apellido);
		}
		ltbListaInformeFactibilidad.setModel(new ListModelList<Teg>(tegs));

	}

	/*
	 * Metodo que permite retornar un lista de los tegs, donde se recorre tanto
	 * la lista del teg como los profesores activos, donde se compara si coincide las
	 * cedulas de cada uno de los profesores para cargar la lista de tegs.
	 */
	public List<Teg> buscarDatos() {

		List<Profesor> profesores = servicioProfesor
				.buscarActivos();
		List<Teg> tegs = servicioTeg.buscarProyectoFactible();

		Profesor profesor1 = new Profesor();
		List<Teg> tegs1 = new ArrayList<Teg>();

		for (int i = 0; i < tegs.size(); i++) {

			profesor1 = tegs.get(i).getTutor();
			boolean encontre = false;
			for (int j = 0; j < profesores.size(); j++) {
				if (profesores.get(j).getCedula().equals(profesor1.getCedula())) {
					encontre = true;
				}
			}
			if (encontre == true) {
				tegs1.add(tegs.get(i));
			}
		}

		ltbListaInformeFactibilidad.setModel(new ListModelList<Teg>(tegs1));
		return tegs1;
	}
	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista) {
		vistaReporte = vista;
		
	}
	

	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar el nombre y apellido del estudiante y el titulo de estos.
	 */
	@Listen("onChange = #txtEstudianteInformeFactibilidad, #txtTituloInformeFactibilidad")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscarDatos();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			teg1.get(i).setEstatus(nombre+" "+apellido);
		}
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (servicioEstudiante.buscarEstudiantePorTeg(teg)
					.get(0).getNombre().toLowerCase()
					.contains(txtEstudianteInformeFactibilidad.getValue().toLowerCase())
					&& 
					teg.getTitulo().toLowerCase()
					.contains(txtTituloInformeFactibilidad.getValue().toLowerCase()))
			{
				teg2.add(teg);
			}
		}

		ltbListaInformeFactibilidad.setModel(new ListModelList<Teg>(teg2));

	}
	
	
	//Metodo que permite mostrar los datos del catalogo
	/*@Listen("onDoubleClick = #ltbListaInformeFactibilidad")
	public void mostrarDatosCatalogo() {
		if(ltbListaInformeFactibilidad.getItemCount()!=0){
		Listitem listItem = ltbListaInformeFactibilidad.getSelectedItem();
		if(listItem!=null){
		Teg tegDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tegDatosCatalogo.getId());
		String vista = "reportes/estructurados/VReporteInformeFactibilidad";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("tegCatalogo", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/reportes/estructurados/VRegistrarAvance.zul", null, null);
		window.doModal();
		vistaReporte.recibir("catalogos/VCatalogoReporteInformeFactibilidad");
		//vistaRegistrarAvance.recibir("catalogos/VCatalogoAsignarComision");
		}
		}
	}*/
	
	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento doble clic
	 * sobre un item en especifico en la lista, extrayendo asi su id, para luego
	 * poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbListaInformeFactibilidad")
	public void mostrarDatosCatalogo() {
		if (vistaReporte == null) {
			vistaReporte = "reportes/estructurados/VReporteInformeFactibilidad";
		} else {
			// Programa seleccionado en el catalogo VCatalogoPrograma
			Listitem listItem = ltbListaInformeFactibilidad.getSelectedItem();
			Teg tegDatosCatalogo = (Teg) listItem.getValue();
			/*Programa programaDatos = servicioPrograma
					.buscarPorNombrePrograma(((Programa) listItem.getValue())
							.getNombre());*/
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", tegDatosCatalogo.getId());
			String vista = vistaReporte;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			// Permite cerrar la vista VCatalogo
			wdwCatalogoReporteInformeFact.onClose();
		}

	}

	
}
