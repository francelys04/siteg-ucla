package configuracion;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import servicio.STegRequisito;

import servicio.SActividad;
import servicio.SArbol;
import servicio.SAreaInvestigacion;
import servicio.SCategoria;
import servicio.SCondicion;
import servicio.SCondicionPrograma;
import servicio.SCronograma;
import servicio.SDefensa;
import servicio.SEstudiante;
import servicio.SFactibilidad;
import servicio.SGrupo;
import servicio.SItem;
import servicio.SItemFactibilidad;
import servicio.SJurado;
import servicio.SLapso;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SProgramaItem;
import servicio.SProgramaRequisito;
import servicio.SRequisito;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.STipoJurado;
import servicio.SUsuario;
import servicio.SAvance;

public class GeneradorBeans implements ApplicationContextAware {

	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"/META-INF/ConfiguracionAplicacion.xml");

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		GeneradorBeans.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static SProfesor getServicioProfesor() {
		return applicationContext.getBean(SProfesor.class);
	}

	public static SCategoria getServicioCategoria() {
		return applicationContext.getBean(SCategoria.class);
	}

	public static SArbol getServicioArbol() {
		return applicationContext.getBean(SArbol.class);
	}

	public static SActividad getServicioActividad() {
		return applicationContext.getBean(SActividad.class);
	}

	public static SRequisito getServicioRequisito() {
		return applicationContext.getBean(SRequisito.class);
	}

	public static SItem getServicioItem() {
		return applicationContext.getBean(SItem.class);
	}

	public static SLapso getServicioLapso() {
		return applicationContext.getBean(SLapso.class);
	}

	public static SAreaInvestigacion getServicioArea() {
		return applicationContext.getBean(SAreaInvestigacion.class);
	}

	public static STematica getSTematica() {
		return applicationContext.getBean(STematica.class);
	}

	public static SEstudiante getServicioEstudiante() {
		return applicationContext.getBean(SEstudiante.class);
	}

	public static SPrograma getServicioPrograma() {
		return applicationContext.getBean(SPrograma.class);
	}
	
	public static STipoJurado getServicioTipoJurado() {
   		return applicationContext.getBean(STipoJurado.class);
   	}
	
	public static SUsuario getServicioUsuario(){
		return applicationContext.getBean(SUsuario.class);
	}
	
	public static SGrupo getServicioGrupo(){
		return applicationContext.getBean(SGrupo.class);
	}
	
	public static SProgramaArea getServicioProgramaArea(){
		return applicationContext.getBean(SProgramaArea.class);
	}
	
	public static SProgramaRequisito getServicioProgramaRequisito(){
		return applicationContext.getBean(SProgramaRequisito.class);
	}
	
	public static SProgramaItem getServicioProgramaItem(){
		return applicationContext.getBean(SProgramaItem.class);
	}
	public static SCondicionPrograma getServicioCondicionPrograma(){
		return applicationContext.getBean(SCondicionPrograma.class);
	}
	
	public static SCondicion getServicioCondicion(){
		return applicationContext.getBean(SCondicion.class);
	}
	
	public static SCronograma getServicioCronograma(){
		return applicationContext.getBean(SCronograma.class);
	}
	
	public static STeg getServicioTeg(){
		return applicationContext.getBean(STeg.class);
	}

	public static STegRequisito getServicioTegRequisito() {
		// TODO Auto-generated method stub
		return applicationContext.getBean(STegRequisito.class);
	}
	
	public static SAvance getServicioAvance(){
		return applicationContext.getBean(SAvance.class);
	}
	
	public static SSolicitudTutoria getServicioTutoria(){
		return applicationContext.getBean(SSolicitudTutoria.class);
	}
	
	public static SFactibilidad getServicioFactibilidad(){
		return applicationContext.getBean(SFactibilidad.class);
	}
	
	public static SItemFactibilidad getServicioItemFactibilidad(){
		return applicationContext.getBean(SItemFactibilidad.class);
	}
	
	public static SDefensa getServicioDefensa(){
		return applicationContext.getBean(SDefensa.class);
	}
	
	public static SJurado getServicioJurado(){
		return applicationContext.getBean(SJurado.class);
	}
}
