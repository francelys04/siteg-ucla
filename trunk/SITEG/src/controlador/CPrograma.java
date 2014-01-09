package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;
import modelo.Condicion;
import modelo.CondicionPrograma;
import modelo.Grupo;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SAreaInvestigacion;
import servicio.SCondicion;
import servicio.SCondicionPrograma;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

@Controller
public class CPrograma extends CGeneral {

	/*
	 * Inicializacion del servicioPrograma que permitira realizar las
	 * operaciones con las entidades relacionadas con base de datos
	 */
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	
	CCatalogoPrograma catalogo = new CCatalogoPrograma();
	
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	SCondicionPrograma servicioCondicionPrograma = GeneradorBeans.getServicioCondicionPrograma();
	SCondicion servicioCondicion = GeneradorBeans.getServicioCondicion();
	/*
	 * Declaracion de componentes dado a sus id, implementados en las vistas
	 * VPrograma y VCatalago
	 */
	@Wire
	private Textbox txtNombrePrograma;
	@Wire
	private Textbox txtDescripcionPrograma;
	@Wire
	private Textbox txtCorreoPrograma;
	@Wire
	private Listbox ltbPrograma;
	@Wire
	private Window wdwCatalogoPrograma;
	@Wire
	private Textbox txtNombreMostrarPrograma;
	@Wire
	private Textbox txtDescripcionMostrarPrograma;
	@Wire
	private Button btnEliminarPrograma;
	@Wire
	private Button btnGuardarPrograma;

	/*
	 * Inicializacion de la varible global id que sera asociado con el id del
	 * programa
	 */
	long id = 0;

	/*
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VPrograma como VCatalogoPrograma
	 */
	void inicializar(Component comp) {

		/*
		 * Listado de todos los programas que se encuentran activos, cuyo
		 * estatus=true con el servicioPrograma mediante el metodo buscarActivas
		 */
		List<Programa> programas = servicioPrograma.buscarActivas();
		
		/*
		 * Validacion para mostrar el listado de programas mediante el
		 * componente ltbPrograma dependiendo si se encuentra ejecutando la
		 * vista VCatalogoPrograma
		 */
		if (ltbPrograma != null) {
			ltbPrograma.setModel(new ListModelList<Programa>(programas));
		}

		Selectors.wireComponents(comp, this, false);
		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VCatalogo
		 */
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		/*
		 * Validacion para vaciar la informacion del VCatalogo a la vista
		 * VPrograma.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if ((Long) map.get("id") != null) {
				id = ((Long) map.get("id"));
				System.out.println(id);
				/*
				 * Creacion de objeto cargado con el servicioPrograma mediante
				 * el metodo buscar dado a su id y asi llenar los textbox de la
				 * vista VPrograma
				 */
				Programa programa = servicioPrograma.buscar(id);
				txtNombrePrograma.setValue(programa.getNombre());
				txtDescripcionPrograma.setValue(programa.getDescripcion());
				txtCorreoPrograma.setValue(programa.getCorreo());
				btnEliminarPrograma.setDisabled(false);
				map.clear();
				map = null;
			}
		}

	}

	// Metodo que permite abrir la vista VCatalago en forma modal //
	@Listen("onClick = #btnBuscarPrograma")
	public void buscarPrograma() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoPrograma.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VPrograma");

	}

	

	

	// Metodo para guardar un programa
		@Listen("onClick = #btnGuardarPrograma")
		public void guardarPrograma() {
			if ((txtNombrePrograma.getText().compareTo("") == 0)
					|| (txtDescripcionPrograma.getText().compareTo("") == 0)
					|| (txtCorreoPrograma.getText().compareTo("") == 0)) {
				Messagebox.show("Debe completar todos los campos", "Error",
						Messagebox.OK, Messagebox.ERROR);

			} else {
				Messagebox.show("¿Desea guardar los datos del programa?",
						"Dialogo de confirmación", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {
									String nombre = txtNombrePrograma.getValue(); 
									String descripcion = txtDescripcionPrograma.getValue(); 
									String correo = txtCorreoPrograma.getValue();
									Boolean estatus = true; 
									Programa programa = new Programa( id,nombre, descripcion,correo,estatus);
									servicioPrograma.guardar(programa);
									Programa p = servicioPrograma.buscarPorNombrePrograma(nombre);
									System.out.println("Programa Guardado");
									List<CondicionPrograma> condicionesPrograma = new ArrayList<CondicionPrograma>();
									List<Condicion> condiciones = servicioCondicion.buscarActivos();
									for(int i=0; i<condiciones.size(); i++){
										Condicion condicion = condiciones.get(i);
										CondicionPrograma condicionPrograma = new CondicionPrograma();
										condicionPrograma.setPrograma(p);
										condicionPrograma.setCondicion(condicion);
										condicionPrograma.setValor(0);
										condicionesPrograma.add(condicionPrograma);
									}
									
									servicioCondicionPrograma.guardar(condicionesPrograma);
									cancelarPrograma(); 
									Messagebox.show("Programa registrado exitosamente","Información", Messagebox.OK,Messagebox.INFORMATION); 
									id = 0;
									}
							}
						});

			}
		}
		
		// Metodo para eliminar un programa dado su id
		@Listen("onClick = #btnEliminarPrograma")
		public void eliminarPrograma() {
			Messagebox.show("¿Desea eliminar los datos del programa?",
					"Dialogo de confirmación", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								Programa programa = servicioPrograma.buscar(id);
								programa.setEstatus(false);
								servicioPrograma.guardar(programa);
								cancelarPrograma();
								Messagebox.show("Programa eliminado exitosamente", "Información", Messagebox.OK,Messagebox.INFORMATION);
								btnEliminarPrograma.setDisabled(true);
							}
						}
					});
		}
			// Metodo para limpiar los componentes textbox y variable id
		@Listen("onClick = #btnCancelarPrograma")
		public void cancelarPrograma() {
			id = 0;
			txtNombrePrograma.setValue("");
			txtDescripcionPrograma.setValue("");
			txtCorreoPrograma.setValue("");
			btnEliminarPrograma.setDisabled(true);

		}

		


}
