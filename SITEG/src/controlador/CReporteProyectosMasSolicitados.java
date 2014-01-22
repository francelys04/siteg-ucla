package controlador;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Programa;
import modelo.ProgramaArea;
import modelo.SolicitudTutoria;
import modelo.Teg;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SAreaInvestigacion;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;

@Controller
public class CReporteProyectosMasSolicitados extends CGeneral {
	
	
	STeg servicioTeg= GeneradorBeans.getServicioTeg();
	SProgramaArea servicioProgramaArea= GeneradorBeans.getServicioProgramaArea();
	SPrograma servicioPrograma=GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea=GeneradorBeans.getServicioArea();
	
	Estudiante estudiante;//=ObtenerUsuarioEstudiante();
	
	/**********  Proyectos Solicitados  **************/
	
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbEstatus;
	

	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas=servicioPrograma.buscarActivas();
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
	
		
		
		
	}

	@Listen("onClick = #btnGenerarProyectosSolicitados")
	public void generarProyectosSolicitados() throws JRException {
		
		String estatus=cmbEstatus.getValue();
		System.out.println("es"+estatus);
		String idArea=cmbArea.getSelectedItem().getId();
		AreaInvestigacion area=servicioArea.buscarArea(Long.parseLong(idArea));
		
		
		Date fechaInicio= new Date();
		Date fechaFin= new Date();
		
		fechaInicio=dtbFechaInicio.getValue();
		fechaFin=dtbFechaFin.getValue();
	System.out.println("inicio:"+fechaInicio);
		System.out.println("fin:"+fechaFin);
		
		List<Teg> tegs= servicioTeg.buscarUltimasTematicas(estatus, area,fechaInicio,fechaFin);
		

		
		if(tegs.size()==0){
			
			Messagebox.show(
					"No se encuentra proyectos para este periodo",
					"Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
			
			
		}
		else{
		FileSystemView filesys = FileSystemView.getFileSystemView();
		
		Map p = new HashMap();
		
		
		p.put("titulo", "UNIVERSIDAD CENTROCCIDENTA LISANDRO ALVARADO");
		p.put("programaNombre",cmbPrograma.getValue());
		
		
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResource("/reporte/RProyectosSolicitados.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p, new JRBeanCollectionDataSource(tegs));
		JasperExportManager.exportReportToPdfFile(jasperPrint, filesys.getHomeDirectory().toString()+"/nuevo.pdf"); 
		
		Messagebox.show(
				"Se ha generado exitosamente el reporte",
				"Informacion", Messagebox.OK,
				Messagebox.INFORMATION);
		
		
		}
	}
	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() throws JRException {
		
		String idPrograma= cmbPrograma.getSelectedItem().getId();
		System.out.println("malo"+idPrograma);
		Programa programa= servicioPrograma.buscar(Long.parseLong(idPrograma));
		System.out.println("malo"+programa);
		List<AreaInvestigacion> programaAreas= servicioProgramaArea.buscarAreasDePrograma(programa); 
		System.out.println("malo"+programaAreas);
		
		cmbArea.setModel(new ListModelList<AreaInvestigacion>(programaAreas));
		
	}
	
	



}
