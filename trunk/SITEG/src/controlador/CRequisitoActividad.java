package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import modelo.Actividad;
import modelo.ActividadRequisito;
import modelo.Lapso;
import modelo.Requisito;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SActividad;
import servicio.SRequisito;
import configuracion.GeneradorBeans;

@Controller
public class CRequisitoActividad extends CGeneral {

	SActividad servicioActividad = GeneradorBeans.getServicioActividad();
	SRequisito servicioRequisito = GeneradorBeans.getServicioRequisito();
	CCatalogoActividad catalogo = new CCatalogoActividad();

	@Wire
	private Textbox txtNombreActividad;
	@Wire
	private Textbox txtDescripcionActividad;
	@Wire
	private Listbox ltbActividad;
	@Wire
	private Listbox lsbRequistoActividadDisponibles;
	@Wire
	private Listbox lsbRequisitoActividadSeleccionados;
	@Wire
	private Window wdwCatalogoActividad;
	private List<Requisito> requisitos;
	private long id = 0;
	private String descripcion;

	private Actividad actividadSeleccionada;

	/**
	 * Muestra el Catalogo con todas las Actividades guardadas
	 * 
	 * @author adriana
	 * @date 15-12-2013
	 */
	@Listen("onClick = #btnCatalogoRequisitoActividad")
	public void buscarActividad() {
		final Window win = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoActividad.zul", null, null);
		win.setClosable(true);
		catalogo.recibir("maestros/VRequisitoActividad");
		win.doModal();
	}

	/**
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VRequisitoActividad como VCatalogoActividad
	 * 
	 * @author adriana
	 * @date 15-12-2013
	 */
	@Override
	void inicializar(Component comp) {

		Selectors.wireComponents(comp, this, false);

		/**
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		/**
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Actividad actividad2 = servicioActividad
						.buscarActividad(codigo);
				id = actividad2.getId();
				txtNombreActividad.setValue(actividad2.getNombre());
				this.actividadSeleccionada = actividad2;
				descripcion = (actividad2.getDescripcion());
				map.clear();
				map = null;
			}
		}

		/**
		 * Listado de todos las actividades y requisitos que se encuentran
		 * guardados a demas de mostrar los requisitos relacionados con una
		 * actividad al momento de cargar
		 * 
		 **/

		List<Actividad> actividades = servicioActividad.buscarActivos();
		requisitos = servicioRequisito.buscarRequisito();
		List<Requisito> requisitosSeleccionados = new ArrayList<Requisito>();
		if (actividadSeleccionada != null) {
			Iterator it = actividadSeleccionada.getActividadesRequisitos()
					.iterator();
			while (it.hasNext()) {
				Requisito req = ((ActividadRequisito) it.next()).getRequisito();
				requisitos.remove(req);
				requisitosSeleccionados.add(req);
			}
		}
		lsbRequistoActividadDisponibles.setModel(new ListModelList(requisitos));
		lsbRequisitoActividadSeleccionados.setModel(new ListModelList(
				requisitosSeleccionados));
		/*
		 * Validacion para mostrar el listado de actividades mediante el
		 * componente ltbActividad dependiendo si se encuentra ejecutando la
		 * vista VCatalogoActividad
		 */
		if (txtNombreActividad == null) {
			ltbActividad.setModel(new ListModelList<Actividad>(actividades));
		}

	}

	/**
	 * Metodo que permite mover un Item hacia a la otra lista de los Requisitos
	 * Seleccionados
	 * 
	 * @date 09-12-2013
	 */
	@Listen("onClick = #btnPasar1")
	public void moverDerecha() {
		Listitem list1 = lsbRequistoActividadDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(lsbRequisitoActividadSeleccionados);
	}

	/**
	 * Metodo que permite remover un Item de la lista de los Requisitos
	 * Seleccionados
	 * 
	 * @date 09-12-2013
	 */
	@Listen("onClick = #btnPasar2")
	public void moverIzquierda() {
		Listitem list2 = lsbRequisitoActividadSeleccionados.getSelectedItem();
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(lsbRequistoActividadDisponibles);
	}

	/**
	 * Metodo que permite guardar una actividad con sus requisitos
	 * 
	 * @date 09-12-2013
	 */
	@Listen("onClick = #btnGuardarRequisitoActividad")
	public void guardarActividadRequisito() {
		List<Listitem> items = this.lsbRequisitoActividadSeleccionados
				.getItems();
		Lapso lapso = new Lapso(1, "test", new Date(), new Date(), true);

		Iterator it = actividadSeleccionada.getActividadesRequisitos()
				.iterator();
		List<Requisito> requisitosSeleccionados = new ArrayList<Requisito>();
		while (it.hasNext()) {
			requisitosSeleccionados.add(((ActividadRequisito) it.next())
					.getRequisito());
		}

		for (int i = 0; i < items.size(); i++) {
			Requisito req = (Requisito) items.get(i).getValue();
			if (!requisitosSeleccionados.contains(req)) {
				System.out.println("------------------------------->");
				ActividadRequisito actividadRequisito = new ActividadRequisito(
						actividadSeleccionada, req, lapso);
				actividadSeleccionada.getActividadesRequisitos().add(
						actividadRequisito);
			}
		}
		servicioActividad.guardar(actividadSeleccionada);
	}

	/**
	 * Metodo para limpiar los componentes de la vista
	 * 
	 * @date 09-12-2013
	 */
	@Listen("onClick = #btnCancelarRequisitoActividad")
	public void cancelarActividades() {
		txtNombreActividad.setValue("");
		descripcion = null;
		requisitos = servicioRequisito.buscarRequisito();
		lsbRequistoActividadDisponibles.setModel(new ListModelList(requisitos));
		lsbRequisitoActividadSeleccionados.getItems().clear();
		;
	}

}
