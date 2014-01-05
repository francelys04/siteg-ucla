package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import servicio.STeg;

@Controller
public class CCatalogoSolicitudDefensa extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	CAtenderDefensa vista = new CAtenderDefensa();
	
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
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if(map != null || map==null){
			tegsDefensa1 = servicioTeg.buscarTegPorProgramaParaDefensa(ObtenerUsuarioProfesor().getPrograma());
			tegsDefensa.add(tegsDefensa1.get(0));
			for(int i =1; i<tegsDefensa1.size();i++){
				System.out.println("id"+tegsDefensa1.get(i).getId());
				long temp = tegsDefensa1.get(i-1).getId();
				System.out.println("temporal"+temp);
				if(temp !=tegsDefensa1.get(i).getId()){
					tegsDefensa.add(tegsDefensa1.get(i));
				}
				
			}
			System.out.println(tegsDefensa.toString());
			ltbSolicitudesDefensa.setModel(new ListModelList<Teg>(tegsDefensa));
		}
	}
	
	@Listen("onChange = #txtAreaSolicitudDefensa,#txtTematicaSolicitudDefensa,#txtTituloSolicitudDefensa,#txtNombreTutorSolicitudDefensa,#txtApellidoTutorSolicitudDefensa")
	public void filtrarCatalogo(){
		
		List<Teg> tegs = new ArrayList<Teg>();

		for (Teg teg : tegsDefensa) {
			if (teg.getTematica().getareaInvestigacion().getNombre()
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
	
	@Listen("onDoubleClick = #ltbSolicitudesDefensa")
	public void seleccionarLista(){
		Listitem listItem = ltbSolicitudesDefensa.getSelectedItem();
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
