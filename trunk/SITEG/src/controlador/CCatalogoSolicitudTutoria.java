package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.SolicitudTutoria;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import servicio.SSolicitudTutoria;
import configuracion.GeneradorBeans;

@Controller
public class CCatalogoSolicitudTutoria extends CGeneral {

	SSolicitudTutoria servicioTutoria = GeneradorBeans.getServicioTutoria();
	CSolicitudTutoria vista = new CSolicitudTutoria();
	
	@Wire
	private Listbox ltbSolicitud;
	@Wire
	private Textbox txtAreaSolicitud;
	@Wire
	private Textbox txtTematicaSolicitud;
	@Wire
	private Textbox txtDescripcionSolicitud;
	@Wire
	private Textbox txtFechaSolicitud;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if(map != null || map==null){
		List<SolicitudTutoria> solicitudes = servicioTutoria
				.buscarSolicitudPorRevisar(ObtenerUsuarioProfesor());
		ltbSolicitud.setModel(new ListModelList<SolicitudTutoria>(solicitudes));
		}
	}

	@Listen("onChange = #txtAreaSolicitud,#txtTematicaSolicitud,#txtDescripcionSolicitud")
	public void filtrarDatosCatalogo() {
		List<SolicitudTutoria> solicitudes = servicioTutoria
				.buscarSolicitudPorRevisar(ObtenerUsuarioProfesor());
		List<SolicitudTutoria> solicitud2 = new ArrayList<SolicitudTutoria>();

		for (SolicitudTutoria solicitud : solicitudes) {
			if (solicitud.getTematica().getareaInvestigacion().getNombre()
					.toLowerCase()
					.contains(txtAreaSolicitud.getValue().toLowerCase())
					&& solicitud
							.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaSolicitud.getValue()
											.toLowerCase())
					&& solicitud
							.getDescripcion()
							.toLowerCase()
							.contains(txtDescripcionSolicitud.getValue())) {
				solicitud2.add(solicitud);
			}
		}

		ltbSolicitud.setModel(new ListModelList<SolicitudTutoria>(solicitud2));

	}
	
	@Listen("onDoubleClick = #ltbSolicitud")
	public void seleccionarSolicitud(){
		Listitem listItem = ltbSolicitud.getSelectedItem();
		SolicitudTutoria solicitudSeleccionada = (SolicitudTutoria)listItem.getValue();
		long id = solicitudSeleccionada.getId();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id",id);
		Sessions.getCurrent().setAttribute("catalogoSolicitud", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VEvaluarTutorias.zul", null, null);	 				
		window.doModal();
		vista.recibir("catalogos/VCatalogoSolicitudTutorias");
	}
}
