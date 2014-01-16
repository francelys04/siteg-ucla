package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.Cronograma;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Noticia;
import modelo.Programa;
import configuracion.GeneradorBeans;
import controlador.CConsultarEstatus;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.zkoss.spring.security.SecurityUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;
import servicio.SCronograma;
import servicio.SEstudiante;
import servicio.SLapso;
import servicio.SNoticia;
import servicio.SPrograma;

@Controller
public class CInicio extends CGeneral {
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SCronograma servicioCronograma = GeneradorBeans.getServicioCronograma();
	SNoticia servicioNoticia = GeneradorBeans.getServicioNoticia();
	@Wire
	private Treeitem fila3;
	@Wire
	private Treecell fila211;
	@Wire
	private Treecell fila2;
	@Wire
	private Treecell fila11;
	@Wire
	private Treecell fila1;
	@Wire
	private Treecell fila12;
	@Wire
	private Treecell fila112;
	@Wire
	private Treecell fila111;
	@Wire
	private Intbox cedulaEstatus;
	@Wire
	private Window wdwConsultarEstatusProyecto;
	@Wire
	private Window wdwCatalogoArchivo;
	@Wire
	private Button btnSolicitarTutor;
	@Wire
	private Button btnBiblioteca;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Listbox ltbCronograma;
	@Wire
	private Window wdwCrono;
	@Wire
	private Label lblOlvidoClave;	
	@Wire
	private Image imgNoticiaUno;
	@Wire
	private Image imgNoticiaDos;
	@Wire
	private Image imgNoticiaTres;
	@Wire
	private Button btnIniciar;
	@Wire
	private Button btnParar;
	List<BufferedImage> listaImagenes = null;
	int longitud;
	int index = 0;

	

	public static long idPrograma;


	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Programa> programa = servicioPrograma.buscarActivas();
		if (cmbPrograma != null) {
			cmbPrograma.setModel(new ListModelList<Programa>(programa));

		}
	
		
		if(imgNoticiaUno != null){
		imgNoticiaUno.setContent(getImagen());
		imgNoticiaDos.setContent(getImagen());
		imgNoticiaTres.setContent(getImagen());		
		Clients.evalJavaScript("setContent('"+getContentString()+"')");
		iniciarSlide ();
		}
	}
	//inician metodos para mostrar imagenes en el slide
	
	
	public void iniciarSlide () {
		int delay = 2;
		String comando = "iniciarSlideShow(" + delay*1000 + ")";
		Clients.evalJavaScript(comando);
	}
	
	
	private String getContentString() {
		StringBuilder sb = new StringBuilder();
		List lista = getListaImagenes();
		for (int i = 0; i < lista.size(); i++) {
			if (i > 0)
				sb.append(",");
			sb.append(lista.get(i));
		}
		return sb.toString();
	}
	private List<BufferedImage> getListaImagenes () {
		List<Noticia> noticia= servicioNoticia.buscarActivos();
		if (listaImagenes == null) {
			// modify here for dynamic assign images
			listaImagenes = new ArrayList<BufferedImage>();
			for (int i = 0; i <noticia.size(); i++)
				try {
					listaImagenes.add(ImageIO.read(new ByteArrayInputStream(noticia.get(i).getImagen())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		longitud = listaImagenes.size();
		return listaImagenes;
	}
	private BufferedImage getImagen () {
		BufferedImage imagen = getListaImagenes().get(index);
		
		index = (index+1) % longitud;
		return imagen;
	}
	
//fin de metodo del slide show
	@Listen("onSelect = #cmbPrograma")
	public void llenarCronograma() {
		idPrograma = Long.parseLong(cmbPrograma.getSelectedItem().getId());
		System.out.println(idPrograma);
		Lapso lapso = servicioLapso.buscarLapsoVigente();
		Programa programa = servicioPrograma.buscar(idPrograma);
		List<Cronograma> cronograma = servicioCronograma
				.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		ltbCronograma.setModel(new ListModelList<Cronograma>(cronograma));
		wdwCrono.setVisible(false);

	}

	@Listen("onClick = #btnSolicitarTutor")
	public void SolicitarTutor() {
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VSolicitarTutor.zul", null, null);
		window.doModal();
	}
	@Listen("onClick = #lblOlvidoClave")
	public void reiniciarClave() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VReinicioClave.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #mitQuienesSomos")
	public void QuienesSomos() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VQuienesSomos.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #mitContactanos")
	public void contactanos() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VContactanos.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #btnBiblioteca")
	public void BibliotecaVirtual() {
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArchivo.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #btnDatos")
	public void datos() {
		// Agarrar datos del usuario
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.toString());
	}

	@Listen("onClick = #btnInformacionInteres")
	public void InformacionInteres(Event e) {
		Window wdwInformacionInteres = (Window) Executions.createComponents(
				"/vistas/portal-web/VInformacionInteres.zul", null, null);
		wdwInformacionInteres.doModal();
		CCatalogoArchivoDescarga cata = new controlador.CCatalogoArchivoDescarga();
		cata.metodoPrender();
	}

	@Listen("onClick = #btnConsultarEstatus")
	public void ventanaEmergente(Event e) {
		String cedula = Integer.toString(cedulaEstatus.getValue());

		if (cedula != "") {
			if (servicioEstudiante.buscarEstudiante(cedula) != null) {

				cedulaEstatus.setValue(null);
				CConsultarEstatus consultarestatus = new CConsultarEstatus();
				consultarestatus.recibirCedula(cedula);
			} else {

				Messagebox
						.show("Estudiante no autorizado para realizar un Trabajo Especial de Grado",
								"Informaci�n", Messagebox.OK,
								Messagebox.INFORMATION);
			
			}

		}

	}

}
