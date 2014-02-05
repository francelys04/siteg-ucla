package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
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
import controlador.CGeneral;
import controlador.CSolicitudTutoria;

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
	@Wire
	private Textbox txtNombreEstudianteSolicitud;
	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if (map != null || map == null) {
			List<SolicitudTutoria> solicitudes = servicioTutoria
					.buscarSolicitudPorRevisar(ObtenerUsuarioProfesor());
			for (int i = 0; i < solicitudes.size(); i++) {
				List<Estudiante> es = servicioEstudiante
						.buscarSolicitudesEstudiante(solicitudes.get(i));
				String nombre = es.get(0).getNombre();
				String apellido = es.get(0).getApellido();
				solicitudes.get(i).setEstatus(nombre+" "+apellido);
			}
			ltbSolicitud.setModel(new ListModelList<SolicitudTutoria>(
					solicitudes));
		}
	}

	// permite filtar los datos del catalogo
	@Listen("onChange = #txtFechaSolicitud,#txtAreaSolicitud,#txtTematicaSolicitud,#txtDescripcionSolicitud")
	public void filtrarDatosCatalogo() {
		List<SolicitudTutoria> solicitudes = servicioTutoria
				.buscarSolicitudPorRevisar(ObtenerUsuarioProfesor());
		for (int i = 0; i < solicitudes.size(); i++) {
			List<Estudiante> es = servicioEstudiante
					.buscarSolicitudesEstudiante(solicitudes.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			solicitudes.get(i).setEstatus(nombre+" "+apellido);
		}
		List<SolicitudTutoria> solicitud2 = new ArrayList<SolicitudTutoria>();

		for (SolicitudTutoria solicitud : solicitudes) {
			if (solicitud.getFecha().toString().toLowerCase()
					.contains(txtFechaSolicitud.getValue().toLowerCase())
					&& servicioEstudiante
							.buscarSolicitudesEstudiante(solicitud)
							.get(0)
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreEstudianteSolicitud.getValue()
											.toLowerCase())
					&& solicitud
							.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(txtAreaSolicitud.getValue().toLowerCase())
					&& solicitud
							.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaSolicitud.getValue()
											.toLowerCase())
					&& solicitud.getDescripcion().toLowerCase()
							.contains(txtDescripcionSolicitud.getValue())) {
				solicitud2.add(solicitud);
			}
		}

		ltbSolicitud.setModel(new ListModelList<SolicitudTutoria>(solicitud2));

	}

	// permite llebas los datos del teg a la vista Evaluar Tutorias
	@Listen("onDoubleClick = #ltbSolicitud")
	public void seleccionarSolicitud() {
		if (ltbSolicitud.getItemCount() != 0) {
			Listitem listItem = ltbSolicitud.getSelectedItem();
			if(listItem!=null){
			SolicitudTutoria solicitudSeleccionada = (SolicitudTutoria) listItem
					.getValue();
			long id = solicitudSeleccionada.getId();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			Sessions.getCurrent().setAttribute("catalogoSolicitud", map);
			Window window = (Window) Executions.createComponents(
					"/vistas/transacciones/VEvaluarTutorias.zul", null, null);
			window.doModal();
			vista.recibir("catalogos/VCatalogoSolicitudTutorias");
			}
		}
	}
}
