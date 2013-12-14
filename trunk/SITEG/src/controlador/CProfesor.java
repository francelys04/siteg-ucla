package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;

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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SCategoria;
import servicio.SProfesor;
import configuracion.GeneradorBeans;


public class CProfesor extends CGeneral {

	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SCategoria servicioCategoria = GeneradorBeans.getServicioCategoria();
	
	@Wire
	private Combobox cmbCategoriaProfesor;
	@Wire
	private Textbox txtCedulaProfesor;
	@Wire
	private Textbox txtNombreProfesor;
	@Wire
	private Textbox txtApellidoProfesor;
	@Wire
	private Textbox txtDireccionProfesor;
	@Wire
	private Textbox txtTelefonoMovilProfesor;
	@Wire
	private Textbox txtTelefonoFijoProfesor;
	@Wire
	private Textbox txtCorreoProfesor;
	@Wire
	private Radiogroup rdgSexoProfesor;
	@Wire
	private Radio rdoSexoFProfesor;
	@Wire
	private Radio rdoSexoMProfesor;
	@Wire
	private Listbox ltbProfesor;
	@Wire
	private Window wdwCatalogoProfesor;
	@Wire
	private Window wdwProfesor;
	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;
	@Wire
	private Textbox txtCategoriaMostrarProfesor;
	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Categoria> categorias = servicioCategoria.buscarCategoria();
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		if (cmbCategoriaProfesor == null) {
			ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
		} else {
			cmbCategoriaProfesor.setModel(new ListModelList<Categoria>(
					categorias));
		}

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesor.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor.buscarProfesorPorCedula(txtCedulaProfesor.getValue());
				txtNombreProfesor.setValue(profesor.getNombre());
				txtApellidoProfesor.setValue(profesor.getApellido());

				if (profesor.getSexo() == "Masculino") {
					rdoSexoMProfesor.setChecked(true);
				} else
					rdoSexoFProfesor.setChecked(true);
				txtDireccionProfesor.setValue(profesor.getDireccion());
				txtTelefonoMovilProfesor.setValue(profesor.getTelefono_movil());
				txtTelefonoFijoProfesor
						.setValue(profesor.getTelefono_fijo());
				txtCorreoProfesor.setValue(profesor.getCorreoElectronico());
				cmbCategoriaProfesor.setValue(profesor.getCategoria().getNombre());
				map.clear();
				map = null;
			}
		}
	}
	
	@Listen("onClick = #btnCatalogoProfesor")
	public void buscarProfesor() {

		Window window = (Window) Executions.createComponents(
				"/vistas/VCatalogoProfesor.zul", null, null);
		window.doModal();

	}

	@Listen("onClick = #btnGuardarProfesor")
	public void guardarProfesor() {

		String cedula = txtCedulaProfesor.getValue();
		String nombre = txtNombreProfesor.getValue();
		String apellido = txtApellidoProfesor.getValue();
		String sexo = rdgSexoProfesor.getSelectedItem().getLabel();
		String direccion = txtDireccionProfesor.getValue();
		String telefonoMovil = txtTelefonoMovilProfesor.getValue();
		String telefonoFijo = txtTelefonoFijoProfesor.getValue();
		String correo = txtCorreoProfesor.getValue();
		String categorias = cmbCategoriaProfesor.getValue();
		Boolean estatus = true;
		Categoria categoria = servicioCategoria.buscarCategoriaPorNombre(categorias);
		Set<AreaInvestigacion> areasProfesor = new HashSet<AreaInvestigacion>();
		Profesor profesor = new Profesor(cedula, nombre, apellido, correo, sexo,
				direccion, telefonoMovil, telefonoFijo, estatus,
				categoria, areasProfesor);
		servicioProfesor.guardarProfesor(profesor);
		cancelarProfesor();
		System.out.println("Estudiante Guardado");
	}
	
	@Listen("onClick = #btnCancelarProfesor")
	public void cancelarProfesor() {

		txtCedulaProfesor.setValue("");
		txtNombreProfesor.setValue("");
		txtApellidoProfesor.setValue("");
		rdgSexoProfesor.setSelectedItem(null);
		txtDireccionProfesor.setValue("");
		txtTelefonoMovilProfesor.setValue("");
		txtTelefonoFijoProfesor.setValue("");
		txtCorreoProfesor.setValue("");
		cmbCategoriaProfesor.setValue("");

	}
	
	@Listen("onClick = #btnEliminarProfesor")
	public void eliminarProfesor() {
		System.out.println("Estudiante Eliminado");
		String cedula = txtCedulaProfesor.getValue();
		Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedula);
		profesor.setEstatus(false);
		servicioProfesor.guardarProfesor(profesor);
		cancelarProfesor();
		System.out.println("Estudiante Eliminado");
	}
	
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCorreoMostrarProfesor,#txtProgramaMostrarProfesor")
	public void filtrarDatosCatalogo() {
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Profesor> profesores2 = new ArrayList<Profesor>();

		for (Profesor profesor : profesores) {
			if (profesor
					.getCedula()
					.toLowerCase()
					.contains(
							txtCedulaMostrarProfesor.getValue().toLowerCase())
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
											.toLowerCase())
					&& profesor
							.getCategoria()
							.getNombre()
							.toLowerCase()
							.contains(
									txtCategoriaMostrarProfesor.getValue()
											.toLowerCase())) {
				profesores2.add(profesor);
			}

		}

		ltbProfesor.setModel(new ListModelList<Profesor>(profesores2));

	}

	@Listen("onDoubleClick = #ltbProfesor")
	public void mostrarDatosCatalogo() {

		Listitem listItem = ltbProfesor.getSelectedItem();
		Profesor profesorDatosCatalogo = (Profesor) listItem.getValue();
		if(profesorDatosCatalogo==null)
		{
			alert("no hay gente");
		}
		else{
		String vista="VProfesor";
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cedula", profesorDatosCatalogo.getCedula());
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoProfesor.onClose();
		}
	}
}
