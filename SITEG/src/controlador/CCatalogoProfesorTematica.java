package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.CondicionPrograma;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Tematica;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SAreaInvestigacion;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SSolicitudTutoria;
import servicio.STematica;
import configuracion.GeneradorBeans;

@Controller
public class CCatalogoProfesorTematica extends CGeneral {

	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SAreaInvestigacion servicioAreaInvestigacion = GeneradorBeans
			.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	SPrograma serviciop =GeneradorBeans.getServicioPrograma();
	SSolicitudTutoria serviciost = GeneradorBeans.getServicioTutoria();

	@Wire
	private Listbox ltbProfesor;

	@Wire
	private Combobox cmbProgramaSolicitud;
	@Wire
	private Window wdwCatalogoProfesorArea;

	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;

	private static String vistaRecibida;
	
	private static long p;
	private static long t;

	void inicializar(Component comp) {


		List<Profesor> profe = llenarprofesores();
		ltbProfesor.setModel(new ListModelList<Profesor>(profe));
				

		// TODO Auto-generated method stub
		List<Profesor> profesores = servicioProfesor
				.buscarProfesoresPorTematica(servicioTematica
						.buscarTematica(t));
		ltbProfesor.setModel(new ListModelList<Profesor>(profesores));


		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

	}



public void recibir(String vista,long programa, long tematica) {
	vistaRecibida = vista;

	p = programa;
	t = tematica;
	

	

}
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCorreoMostrarProfesor")
	public void filtrarDatosCatalogo() {
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Profesor> profesores2 = new ArrayList<Profesor>();

		for (Profesor profesor : profesores) {
			if (profesor
					.getCedula()
					.toLowerCase()
					.contains(txtCedulaMostrarProfesor.getValue().toLowerCase())
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
											.toLowerCase())) {
				profesores2.add(profesor);
			}

		}

		ltbProfesor.setModel(new ListModelList<Profesor>(profesores2));

	}

	@Listen("onDoubleClick = #ltbProfesor")
	public void mostrarDatosCatalogo() {

		if (vistaRecibida == null) {

			vistaRecibida = "/vistas/transacciones/VSolicitarTutor";

		} else {
			Listitem listItem = ltbProfesor.getSelectedItem();
			Profesor profesorDatosCatalogo = (Profesor) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("cedula", profesorDatosCatalogo.getCedula());
			String vista = vistaRecibida;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			// Executions.sendRedirect("/vistas/transacciones/VSolicitarTutor.zul");

			Window window = (Window) Executions.createComponents(
					"/vistas/transacciones/VSolicitarTutor.zul", null, null);
			window.doModal();
			wdwCatalogoProfesorArea.onClose();
		}
	}
	
	public List<Profesor> llenarprofesores()
	{
	Tematica tema = servicioTematica.buscarTematica(t);
			
			
			
			List<Profesor> profesores = servicioProfesor.buscarProfesoresPorTematica(tema);
			String variable = "Numero de tutorias por profesor";
			Programa progra = serviciop.buscarPorId(p);
			CondicionPrograma cm = buscarCondicionVigenteEspecifica(variable, progra);
			List<SolicitudTutoria> st = serviciost.buscarAceptadas();
			int valor = cm.getValor();
			
			
			
			
			List<Profesor> profe = new  ArrayList <Profesor> ();
			for (int i = 0; i <profesores.size(); i++) {
				Profesor pf = profesores.get(i);
				int contar = 0;
				for (int j = 0 ; i< st.size(); j++)
				{
					if (pf == st.get(j).getProfesor())
					{
						++contar;
					}
				}
				if (contar <valor)
				{
					profe.add(pf);
				}
				
			}
			return profe;
			
		}
		

}
