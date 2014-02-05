package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.SolicitudTutoria;
import modelo.Teg;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CAtenderDefensa;
import controlador.CGeneral;

import servicio.SPrograma;
import servicio.STeg;

@Controller
public class CCatalogoSolicitudDefensa extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	CAtenderDefensa vista = new CAtenderDefensa();
	@Wire
	private Textbox txtEstudianteDefensa;
	@Wire
	private Textbox txtFechaSolicitudDefensa;
	@Wire
	private Textbox txtAreaSolicitudDefensa;
	@Wire
	private Textbox txtTematicaSolicitudDefensa;
	@Wire
	private Textbox txtTituloSolicitudDefensa;
	@Wire
	private Textbox txtNombreTutorSolicitudDefensa;
	@Wire
	private Textbox txtApellidoTutorSolicitudDefensa;
	@Wire
	private Listbox ltbSolicitudesDefensa;
	private List<Teg> tegsDefensa1 = new ArrayList<Teg>();
	private List<Teg> tegsDefensa = new ArrayList<Teg>();
	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if(map != null || map==null){
			//Permite llenar la lista del teg que tienen por estatus Solicitando Defensa
			tegsDefensa1 = servicioTeg.buscarTegPorProgramaParaDefensa2(servicioPrograma.buscarProgramaDeDirector(ObtenerUsuarioProfesor()));
			if(tegsDefensa1.isEmpty()){
				ltbSolicitudesDefensa.setEmptyMessage("No hay solicitudes registradas");
			}else{
			tegsDefensa.add(tegsDefensa1.get(0));
			for(int i =1; i<tegsDefensa1.size();i++){
				long temp = tegsDefensa1.get(i-1).getId();
				if(temp !=tegsDefensa1.get(i).getId()){
					tegsDefensa.add(tegsDefensa1.get(i));
				}
				
			}
			for (int i = 0; i < tegsDefensa.size(); i++) {
				List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(tegsDefensa.get(i));
				String nombre = es.get(0).getNombre();
				String apellido = es.get(0).getApellido();
				tegsDefensa.get(i).setEstatus(nombre+" "+apellido);
			}
			System.out.println(tegsDefensa.toString());
			ltbSolicitudesDefensa.setModel(new ListModelList<Teg>(tegsDefensa));
		}
		}
	}
	//Permite filtrar los datos del catalogo
	@Listen("onChange = #txtAreaSolicitudDefensa,#txtTematicaSolicitudDefensa,#txtTituloSolicitudDefensa,#txtNombreTutorSolicitudDefensa,#txtApellidoTutorSolicitudDefensa")
	public void filtrarCatalogo(){
		
		List<Teg> tegs = new ArrayList<Teg>();
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			tegs.get(i).setEstatus(nombre+" "+apellido);
		}
		for (Teg teg : tegsDefensa) {
			if (servicioEstudiante.buscarEstudiantePorTeg(teg)
					.get(0)
					.getNombre()
					.toLowerCase()
					.contains(
							txtEstudianteDefensa.getValue()
									.toLowerCase())
					&&teg.getTematica().getareaInvestigacion().getNombre()
					.toLowerCase()
					.contains(txtAreaSolicitudDefensa.getValue().toLowerCase())
					&& teg
							.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaSolicitudDefensa.getValue()
											.toLowerCase())
					&& teg
							.getTitulo()
							.toLowerCase()
							.contains(txtTituloSolicitudDefensa.getValue())
					&& teg
							.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(txtNombreTutorSolicitudDefensa.getValue())
					&& teg
							.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(txtApellidoTutorSolicitudDefensa.getValue())) {
				tegs.add(teg);
			}
		}

		ltbSolicitudesDefensa.setModel(new ListModelList<Teg>(tegs));
	}
	
	//permite enviar el dato seleccionado del catalogo a la vista recibida
	@Listen("onDoubleClick = #ltbSolicitudesDefensa")
	public void seleccionarLista(){
		
		if(ltbSolicitudesDefensa.getItemCount()!= 0){
		Listitem listItem = ltbSolicitudesDefensa.getSelectedItem();
		if(listItem!=null){
		Teg tegSeleccionado = (Teg)listItem.getValue();
		long id = tegSeleccionado.getId();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id",id);
		Sessions.getCurrent().setAttribute("catalogoSolicitudDefensa", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VAtenderDefensa.zul", null, null);	 				
		window.doModal();
		vista.recibir("catalogos/VCatalogoSolicitudDefensa");
	}
	}
	}
}
