package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Jurado;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;

import org.springframework.stereotype.Controller;
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

import servicio.SJurado;
import servicio.SProfesor;
import servicio.STeg;
import configuracion.GeneradorBeans;

@Controller
public class CCatalogoCalificarDefensa extends CGeneral {

	@Wire
	private Listbox ltbCalificarDefensa;

	@Wire
	private Textbox txtMostrarFechaCalificar;
	@Wire
	private Textbox txtMostrarTematicaCalificar;
	@Wire
	private Textbox txtMostrarAreaCalificar;
	@Wire
	private Textbox txtMostrarTituloCalificar;
	@Wire
	private Textbox txtMostrarNombreTutorCalificar;
	@Wire
	private Textbox txtMostrarApellidoTutorCalificar;
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SJurado servicioj = GeneradorBeans.getServicioJurado();
	CCalificarDefensa ventanarecibida = new CCalificarDefensa();

	@Override
	void inicializar(Component comp) {

		List<Teg> t = buscar();

		ltbCalificarDefensa.setModel(new ListModelList<Teg>(t));
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

	}

	public List<Teg> buscar() {
		List<Jurado> j = servicioj
				.buscarTegDeProfesor(ObtenerUsuarioProfesor());

		List<Teg> t = new ArrayList<Teg>();
		for (int i = 0; i < j.size(); i++) {
			Teg teg = j.get(i).getTeg();

			if (teg.getEstatus().equals("Defensa Asignada")) {

				t.add(teg);
			}
		}
		return t;
	}

	@Listen("onChange = #txtMostrarFechaCalificar,#txtMostrarTematicaCalificar,#txtMostrarAreaCalificar,#txtMostrarTituloCalificar,#txtMostrarNombreTutorCalificar,# txtMostrarApellidoTutorCalificar")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscar();
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (teg.getFecha()
					.toString()
					.toLowerCase()
					.contains(txtMostrarFechaCalificar.getValue().toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematicaCalificar.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarAreaCalificar.getValue()
											.toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(
									txtMostrarTituloCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutorCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutorCalificar.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbCalificarDefensa.setModel(new ListModelList<Teg>(teg2));

	}

	@Listen("onDoubleClick = #ltbCalificarDefensa")
	public void mostrarDatosCatalogo() {
		Listitem listItem = ltbCalificarDefensa.getSelectedItem();
		Teg tegDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tegDatosCatalogo.getId());
		String vista = "transacciones/VCalificarDefensa";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("tegCatalogo", map);

		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VCalificarDefensa.zul", null, null);
		window.doModal();
		ventanarecibida.recibir("catalogos/VCatalogoCalificarDefensa");

	}

}
