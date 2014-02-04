package controlador;

import java.util.*;

import javax.swing.filechooser.FileSystemView;

import modelo.ItemEvaluacion;
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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SItem;

@Controller
public class CReporteItemTipo extends CGeneral {

	SItem servicioItem = GeneradorBeans.getServicioItem();
	@Wire
	private Window wdwReporteItemTipo;
	List<ItemEvaluacion> items = new ArrayList<ItemEvaluacion>();

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		items = servicioItem.buscarItemsActivos();
	}

	@Listen("onClick = #btnGenerarReporteItemTipo")
	 public void GenerarCronograma() throws JRException{
		
		FileSystemView filesys = FileSystemView.getFileSystemView();
		items = servicioItem.buscarItemsActivos();
		Map p = new HashMap();
		p.put("titulo", "UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
		+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
		+ "DIRECCION DE PROGRAMA");
		JasperReport jasperReport = (JasperReport) JRLoader
		.loadObject(getClass().getResource(
		"/reporte/ReporteItemTipo.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(
		jasperReport, p, new JRBeanCollectionDataSource(items));
		JasperExportManager.exportReportToPdfFile(jasperPrint, filesys
		.getHomeDirectory().toString() + "/ReporteItemTipo.pdf");
		Messagebox.show(
		"Se ha generado exitosamente el reporte",
		"Informacion", Messagebox.OK,
		Messagebox.INFORMATION);

	}
	
	@Listen("onClick = #btnCancelarReporteItemTipo")
	public void Salir(){
		wdwReporteItemTipo.onClose();
	}
		
}