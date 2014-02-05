package controlador.reporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Programa;
import modelo.Tematica;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter;

import servicio.SPrograma;
import configuracion.GeneradorBeans;
import controlador.CGeneral;


@Controller
public class CReporteSolicitudes extends CGeneral {
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	
	@Wire
	private Datebox dtbInicioReporteSolicitud;
	@Wire
	private Datebox dtbFinReporteSolicitud;
	@Wire
	private Combobox cmbProgramaReporteSolicitud;
	@Wire
	private Combobox cmbAreaReporteSolicitud;
	@Wire
	private Combobox cmbTematicaReporteSolicitud;
	@Wire
	private Combobox cmbEstatusReporteSolicitud;
	long idTematica = 0;
	private String[] estatusSolicitud = {"Aceptada", "Rechazada","Por Revisar"};
	private Connection com;
	@Override
	public
	void inicializar(Component comp) {
		cmbEstatusReporteSolicitud.setModel(new ListModelList<String>(
				estatusSolicitud));
		List<Programa> programas = servicioPrograma.buscarActivas();
		cmbProgramaReporteSolicitud.setModel(new ListModelList<Programa>(
				programas));
		
	}
	@Listen("onSelect = #cmbProgramaReporteSolicitud")
	public void buscarArea(){
		List<AreaInvestigacion> areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
			.buscar(Long.parseLong(cmbProgramaReporteSolicitud
					.getSelectedItem().getId())));
		cmbAreaReporteSolicitud
		.setModel(new ListModelList<AreaInvestigacion>(areas));
	}
	@Listen("onSelect = #cmbAreaReporteSolicitud")
	public void seleccionarTematica() {
		cmbTematicaReporteSolicitud.setValue("");
		List<Tematica> tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(Long.parseLong(cmbAreaReporteSolicitud
						.getSelectedItem().getId())));
		cmbTematicaReporteSolicitud.setModel(new ListModelList<Tematica>(
				tematicas));
	}
	@Listen("onSelect = #cmbTematicaReporteSolicitud")
	public void tomarIdTematica(){
		idTematica =Long.parseLong(cmbTematicaReporteSolicitud.getSelectedItem().getId());
	}
	
	
	@Listen("onClick = #btnGenerarReporteSolicitud")
	 public void GenerarReporte() throws SQLException{
		try{
			Class.forName("org.postgresql.Driver");
			com = DriverManager.getConnection("jdbc:postgresql://localhost:5432/siteg","postgres", "equipo2");
			
		}catch (ClassNotFoundException ei){
			ei.printStackTrace();
			
		}
		
		try{
			 FileSystemView filesys = FileSystemView.getFileSystemView();
			Map p = new HashMap();
			
			
			 p.put("programa", Long.parseLong(cmbProgramaReporteSolicitud.getSelectedItem().getId()));
			 java.util.Date date = dtbInicioReporteSolicitud.getValue(); 
			 java.util.Date date1 = dtbFinReporteSolicitud.getValue();
			 java.text.SimpleDateFormat dtbInicioReporteSolicitud=new java.text.SimpleDateFormat("yyyy/MM/dd");
			 java.text.SimpleDateFormat dtbFinReporteSolicitud=new java.text.SimpleDateFormat("yyyy/MM/dd");
				String	fechaInicio= dtbInicioReporteSolicitud.format(date);
				String  fechaFin = dtbFinReporteSolicitud.format(date1);
		   System.out.println( fechaInicio);
		   System.out.println( fechaFin);
		     p.put("nombreprograma", cmbProgramaReporteSolicitud.getValue());
		    p.put("fechainicio",fechaInicio);
		    p.put("fechafin", fechaFin);
		    p.put("tematica", Long.parseLong(cmbTematicaReporteSolicitud.getSelectedItem().getId()));
		    p.put("estatus", cmbEstatusReporteSolicitud.getValue());
			 JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResource("/reporte/ReporteSolicitud.jasper"));
			
		     JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,p,com);
		    
		     JasperViewer.viewReport(jasperPrint,false);
		     
		     com.close();
		     cancelar();

		}catch(JRException e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Listen("onClick = #btnSalirReporteSolicitud")
	public void cancelar() throws JRException {

		cmbProgramaReporteSolicitud.setValue("");
		cmbAreaReporteSolicitud.setValue("");
		
		cmbTematicaReporteSolicitud.setValue("");
		dtbInicioReporteSolicitud.setValue(new Date());
		dtbFinReporteSolicitud.setValue(new Date());

		
	}
	

}
