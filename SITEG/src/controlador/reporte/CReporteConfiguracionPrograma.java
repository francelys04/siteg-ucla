package controlador.reporte;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Lapso;
import modelo.Programa;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import servicio.SLapso;
import servicio.SPrograma;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CReporteConfiguracionPrograma extends CGeneral {
	
	@Wire
	private Combobox cmbConfiguracionPrograma;
	@Wire
	private Combobox  cmbConfiguracionLapso;
	@Wire
	private Window wdwReporteConfigurar;
	private Connection com;
	
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	@Override
	public
	void inicializar(Component comp) {
		List<Programa> programas = servicioPrograma.buscarActivas();
		List<Lapso> lapsos = servicioLapso.buscarActivos();
		
		cmbConfiguracionLapso.setModel(new ListModelList<Lapso>(lapsos));
		cmbConfiguracionPrograma.setModel(new ListModelList<Programa>(
				programas));
		
	}
	
	@Listen("onClick = #btnGenerarReporteConfiguracion")
	 public void GenerarReporte() throws SQLException{
		try{
			Class.forName("org.postgresql.Driver");
			com = DriverManager.getConnection("jdbc:postgresql://localhost:5432/siteg","postgres", "equipo2");
			
		}catch (ClassNotFoundException ei){
			ei.printStackTrace();
			
		}
		
		try{
			 FileSystemView filesys = FileSystemView.getFileSystemView();
			 String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/ReporteConfiguracionPrograma.jasper";
				 String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
			 
			Map p = new HashMap();
			
			
			 p.put("programa", Long.parseLong(cmbConfiguracionPrograma.getSelectedItem().getId()));
		    
		     p.put("lapso",Long.parseLong(cmbConfiguracionLapso.getSelectedItem().getId()));	
			
		     p.put("nombreprograma", cmbConfiguracionPrograma.getValue());
		     p.put("nombrelapso", cmbConfiguracionLapso.getValue());
		     p.put("logoUcla", reporteImage + "logo ucla.png");
			 p.put("logoCE", reporteImage + "logo CE.png");
			 p.put("logoSiteg", reporteImage + "logo.png");
		     
		    
			   
				 
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
		    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,p,com);
		    JasperViewer.viewReport(jasperPrint,false);
		     
		     com.close();
		     cancelar();
 
		}catch(JRException e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Listen("onClick = #btnCancelarReporteConfiguracion")
	public void cancelar() throws JRException {

		cmbConfiguracionPrograma.setValue("");
		cmbConfiguracionLapso.setValue("");
		
		

		
	}
	

}
