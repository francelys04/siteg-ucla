package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Requisito;
import modelo.TipoJurado;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SActividad;
import servicio.STipoJurado;

@Controller
public class CCatalogoTipoJurado extends CGeneral {

	STipoJurado servicioTipoJurado = GeneradorBeans.getServicioTipoJurado();

	@Wire
	private Textbox txtNombreTipoJurado;
	@Wire
	private Textbox txtDescripcionTipoJurado;
	@Wire
	private Textbox txtNombreMostrarTipoJurado;
	@Wire
	private Textbox txtDescripcionMostrarTipoJurado;
	@Wire
	private Listbox ltbTipoJurado;
	@Wire
	private Window wdwCatalogoTipoJurado;
	private long id = 0;

	private static String vistaRecibida;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		//llena la lista con los tipos de jurados activos
		List<TipoJurado> tipoJurado = servicioTipoJurado.buscarActivos();

		if (txtNombreTipoJurado == null) {
			ltbTipoJurado.setModel(new ListModelList<TipoJurado>(tipoJurado));
		}

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	// descripcion
	@Listen("onChange = #txtNombreMostrarTipoJurado,#txtDescripcionMostrarTipoJurado")
	public void filtrarDatosCatalogo() {
		List<TipoJurado> tipoJurado1 = servicioTipoJurado.buscarActivos();
		List<TipoJurado> tipoJurado2 = new ArrayList<TipoJurado>();

		for (TipoJurado tipoJurado : tipoJurado1) {
			if (tipoJurado
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarTipoJurado.getValue().toLowerCase())
					&& tipoJurado
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTipoJurado.getValue()
											.toLowerCase())) {
				tipoJurado2.add(tipoJurado);
			}
		}

		ltbTipoJurado.setModel(new ListModelList<TipoJurado>(tipoJurado2));

	}
	//lleva los datos del item seleccionado a la vista recibida
	@Listen("onDoubleClick = #ltbTipoJurado")
	public void mostrarDatosCatalogo() {
		if(ltbTipoJurado.getItemCount()!=0){

		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VTipoJurado";

		} else {

			Listitem listItem = ltbTipoJurado.getSelectedItem();
			TipoJurado tipoJuradoDatosCatalogo = (TipoJurado) listItem
					.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", tipoJuradoDatosCatalogo.getId());
			String vista = vistaRecibida;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			wdwCatalogoTipoJurado.onClose();
		}
	}
	}
}