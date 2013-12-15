package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Categoria;
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
	
	void inicializar(Component comp) {
		
		
		// TODO Auto-generated method stub
				
				List<Profesor> profesores = servicioProfesor.buscarActivos();
				if (cmbCategoriaProfesor == null) {
					ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
				}

				Selectors.wireComponents(comp, this, false);

				HashMap<String, Object> map = (HashMap<String, Object>) Sessions
						.getCurrent().getAttribute("itemsCatalogo");

				
	}
	
	public void recibir (String vista)
	{
		vistaRecibida = vista;
		//System.out.println("imprimo");
		//System.out.println(vistaRecibida);
	}
	
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCorreoMostrarProfesor,#txtProgramaMostrarProfesor")
	public void filtrarDatosCatalogo() {
		List<Profesor> profesores = servicioProfesor.buscarActivos();
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

		Listitem listItem = ltbProfesor.getSelectedItem();
		Profesor profesorDatosCatalogo = (Profesor) listItem.getValue();
		if(profesorDatosCatalogo==null)
		{
			alert("no hay gente");
		}
		else{
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cedula", profesorDatosCatalogo.getCedula());
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoProfesor.onClose();
		}
	}

}
