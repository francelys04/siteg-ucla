package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Categoria;
import modelo.Profesor;
import modelo.AreaInvestigacion;
import modelo.Programa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zhtml.Messagebox;
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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SAreaInvestigacion;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SAreaInvestigacion;

@Controller
public class CTutorDisponible extends CGeneral {

	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SAreaInvestigacion servicioAreaInvestigacion = GeneradorBeans
			.getServicioArea();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();

	@Wire
	private Combobox cmbAreaTutorDisponible;
	@Wire
	private Listbox ltbTutores;
	@Wire
	private Textbox txtNombreTutorDisponible;
	@Wire
	private Textbox txtApellidoTutorDisponible;
	@Wire
	private Textbox txtCedulaTutorDisponible;
	@Wire
	private Textbox txtProgramaTutorDisponible;
	@Wire
	private Textbox txtCorreoTutorDisponible;
	@Wire
	private Textbox txtTelefonoTutorDisponible;

	// Metodo heredado del controlador CGeneral que permite inicializar los
	// componentes de zk
	// asi como tambien permite settear los atributos a los campos luego de
	// seleccionar desde el catalogo
	void inicializar(Component comp) {

		List<AreaInvestigacion> areas = servicioArea.buscarActivos();
		cmbAreaTutorDisponible.setModel(new ListModelList<AreaInvestigacion>(
				areas));

		if (cmbAreaTutorDisponible.getValue() != null) {

			AreaInvestigacion areasProfesor = servicioArea.buscarAreaPorNombre(cmbAreaTutorDisponible.getValue());

		}

		Selectors.wireComponents(comp, this, false);

	}
}
