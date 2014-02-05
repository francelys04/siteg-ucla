package controlador.catalogo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SJurado;
import servicio.SProfesor;
import servicio.STeg;
import configuracion.GeneradorBeans;
import controlador.CCalificarDefensa;
import controlador.CGeneral;

@Controller
public class CCatalogoReporteItemTeg extends CGeneral {

	@Wire
	private Listbox ltbReporteItemTeg;
	@Wire
	private Textbox txtEstudianteCalificarDefensa;
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
	@Wire
	private Combobox cmbEstatus;
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SJurado servicioj = GeneradorBeans.getServicioJurado();
	CCalificarDefensa ventanarecibida = new CCalificarDefensa();

	@Override
	public
	void inicializar(Component comp) {

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

	}
//busca los tegs 
	@Listen("onSelect= #cmbEstatus")
	public List<Teg> buscar() {
		List<Teg> t = servicioTeg.buscarTegs(cmbEstatus.getValue());
		for (int i = 0; i < t.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(t.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			t.get(i).setEstatus(nombre+" "+apellido);
		}
		
		ltbReporteItemTeg.setModel(new ListModelList<Teg>(t));
		

		return t;
	}
//Permite hacer el filtrado en el catalogo
	@Listen("onChange = #txtMostrarFechaCalificar,#txtMostrarTematicaCalificar,#txtMostrarAreaCalificar,#txtMostrarTituloCalificar,#txtMostrarNombreTutorCalificar,# txtMostrarApellidoTutorCalificar")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscar();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			teg1.get(i).setEstatus(nombre+" "+apellido);
		}
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (servicioEstudiante.buscarEstudiantePorTeg(teg)
					.get(0)
					.getNombre()
					.toLowerCase()
					.contains(
							txtEstudianteCalificarDefensa.getValue()
									.toLowerCase())
					&&teg.getFecha()
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

		ltbReporteItemTeg.setModel(new ListModelList<Teg>(teg2));

	}
//permite mapear los datos a la vista calificar defensa
	@Listen("onDoubleClick = #ltbReporteItemTeg")
	public void mostrarDatosCatalogo() {
		if(ltbReporteItemTeg.getItemCount()!=0){
		Listitem listItem = ltbReporteItemTeg.getSelectedItem();
		if(listItem!=null){
		Teg teg = (Teg) listItem.getValue();
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		  FileSystemView filesys = FileSystemView.getFileSystemView();
		 JasperReport jasperReport;
		try {
			Connection con;
			
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/siteg","postgres","equipo2");
			Map p = new HashMap();
			List<Estudiante> estudiantes = servicioEstudiante.buscarEstudiantePorTeg(teg);
			List<String>estu = new ArrayList<String>();
			for (int i=0; i<estudiantes.size(); i++){
				String nombre= estudiantes.get(i).getNombre();
				String apellido =estudiantes.get(i).getApellido();
				estu.add(nombre+" "+apellido);
				
			}
			String tutor = teg.getTutor().getNombre() +" "+ teg.getTutor().getApellido();
			p.put("idteg", teg.getId());
			p.put("estudiantes",estu);
			p.put("tutor", tutor);
			p.put("tematica", teg.getTematica().getNombre());
			p.put("titulo", teg.getTitulo());
			jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResource("/reporte/RItemTeg.jasper"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p, con);
			JasperViewer.viewReport(jasperPrint,false);	
			con.close();
			//JasperExportManager.exportReportToPdfFile(jasperPrint, filesys.getHomeDirectory().toString()+"/RPrograma.pdf");
		  
		} catch (JRException | SQLException e) {
			System.out.println(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}
	}
}
