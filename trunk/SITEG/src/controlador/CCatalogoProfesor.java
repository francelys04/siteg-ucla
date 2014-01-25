package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Profesor;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SProfesor;
import configuracion.GeneradorBeans;

public class CCatalogoProfesor extends CGeneral {
	
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	
	@Wire
	private Listbox ltbProfesor;
	
	@Wire
	private Combobox cmbCategoriaProfesor;
	@Wire
	private Window wdwCatalogoProfesor;
	@Wire
	private Window wdwProfesor;
	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;
	@Wire
	private Textbox txtCategoriaMostrarProfesor;
	 private static String vistaRecibida;
	 public static List<Profesor> profesores;
	 public static boolean variable2 = false;
	
	void inicializar(Component comp) {
		
		
		// TODO Auto-generated method stub
				
			
				if (cmbCategoriaProfesor == null) {
					List<Profesor> profesores = servicioProfesor.buscarActivos();
					ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
				}

				HashMap<String, Object> map = (HashMap<String, Object>) Sessions
						.getCurrent().getAttribute("itemsCatalogo");

				if (map != null) {
					if (map.get("usuario") != null) {
						List<Profesor> profesores = servicioProfesor
								.buscarProfesorSinUsuario();
						ltbProfesor.setModel(new ListModelList<Profesor>(
								profesores));
						variable2= true;

					} else {
						List<Profesor> profesores = servicioProfesor
								.buscarActivos();
						ltbProfesor.setModel(new ListModelList<Profesor>(
								profesores));

					}

					Selectors.wireComponents(comp, this, false);
				}
				
	}
	
	public void recibir (String vista)
	{
		vistaRecibida = vista;
	}
	
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCategoriaMostrarProfesor,#txtCorreoMostrarProfesor")
	public void filtrarDatosCatalogo() {
		if (variable2 == true)
		{
			 profesores = servicioProfesor
			.buscarProfesorSinUsuario();
		}
		else
			{
			profesores = servicioProfesor.buscarActivos();
			}
		List<Profesor> profesores2 = new ArrayList<Profesor>();

		for (Profesor profesor : profesores) {
			if (profesor
					.getCedula()
					.toLowerCase()
					.contains(
							txtCedulaMostrarProfesor.getValue().toLowerCase())
					&& profesor
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCorreoElectronico()
							.toLowerCase()
							.contains(
									txtCorreoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCategoria()
							.getNombre()
							.toLowerCase()
							.contains(
									txtCategoriaMostrarProfesor.getValue()
											.toLowerCase())) {
				profesores2.add(profesor);
			}

		}

		ltbProfesor.setModel(new ListModelList<Profesor>(profesores2));

	}

	@Listen("onDoubleClick = #ltbProfesor")
	public void mostrarDatosCatalogo() {

		try {
		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VProfesor";

		} else {
		
		Listitem listItem = ltbProfesor.getSelectedItem();
		Profesor profesorDatosCatalogo = (Profesor) listItem.getValue();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
	
		if(map2!=null)
			map=map2;
		map.put("cedula", profesorDatosCatalogo.getCedula());
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoProfesor.onClose();
		}
		} catch (NullPointerException e) {

			System.out.println("NullPointerException");
		}
		
	}
	
	
	
	
	

}
