package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.STeg;
import servicio.SUsuario;
import servicio.SProfesor;

import configuracion.GeneradorBeans;

@Controller
public class CCatalogoAsignarComision extends CGeneral {
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	
	CAsignarComision vistaComision = new CAsignarComision();

	@Wire
	private Listbox ltbProyectosRegistrados;
	@Wire
	private Window wdwCatalogoAsignarComision;
	@Wire
	private Textbox txtMostrarFecha;
	@Wire
	private Textbox txtMostrarTematica;
	@Wire
	private Textbox txtMostrarArea;
	@Wire
	private Textbox txtMostrarTitulo;
	@Wire
	private Textbox txtMostrarNombreTutor;
	@Wire
	private Textbox txtMostrarApellidoTutor;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Teg> tegs = buscarDatos();
		
		
		
     
		

		ltbProyectosRegistrados.setModel(new ListModelList<Teg>(tegs));
		
       
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
	
	}

	// Metodo que permite obtener una lista de los teg de acuerdo al
	// programa del profesor que se encuentra loggeado
	public List<Teg> buscarDatos() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Teg> tegs = servicioTeg.BuscarProyectoRegistrado();

		Profesor profesor1 = new Profesor();
		List<Teg> t = new ArrayList<Teg>();

		for (int i = 0; i < tegs.size(); i++) {

			profesor1 = tegs.get(i).getTutor();

			boolean encontre = false;

			for (int j = 0; j < profesores.size(); j++) {

				if (profesores.get(j).getCedula().equals(profesor1.getCedula())) {
					System.out.print(profesores.get(j).getCedula());
					encontre = true;
				}
			}
			if (encontre == true) {
				t.add(tegs.get(i));

			}

		}

		ltbProyectosRegistrados.setModel(new ListModelList<Teg>(t));
		return t;
	}

	// Metodo que permite filtrar un teg de acuerdo a la fecha, tematica, area,
	// titulo, nombre y apellido del tutor
	@Listen("onChange = #txtMostrarFecha, #txtMostrarTematica,#txtMostrarArea,#txtMostrarTitulo,#txtMostrarNombreTutor,# txtMostrarApellidoTutor")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscarDatos();
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (teg.getFecha().toString().toLowerCase()
					.contains(txtMostrarFecha.getValue().toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematica.getValue().toLowerCase())

					&& teg.getTematica().getareaInvestigacion().getNombre()
							.toLowerCase()
							.contains(txtMostrarArea.getValue().toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(txtMostrarTitulo.getValue().toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutor.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutor.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbProyectosRegistrados.setModel(new ListModelList<Teg>(teg2));

	}

	//Metodo que permite mostrar los datos del catalogo
	@Listen("onDoubleClick = #ltbProyectosRegistrados")
	public void mostrarDatosCatalogo() {
		if(ltbProyectosRegistrados.getItemCount()!=0){
		Listitem listItem = ltbProyectosRegistrados.getSelectedItem();
		Teg tegDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tegDatosCatalogo.getId());
		String vista = "transacciones/VAsignarComision";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("tegCatalogo", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VAsignarComision.zul", null, null);
		window.doModal();
		vistaComision.recibir("catalogos/VCatalogoAsignarComision");
		

	}
	}
	


	
	
	

}
