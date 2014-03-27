package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.EnlaceInteres;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Noticia;
import modelo.Programa;
import modelo.compuesta.Cronograma;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoArchivoDescarga;
import controlador.catalogo.CCatalogoAreaInvestigacion;

/*
 * Controlador que permite activar las funcionalidades del portal web; desde
 * la apertura de ventanas, como el llenado del cronograma
 */
@Controller
public class CInicio extends CGeneral {

	private static final long serialVersionUID = 6255548740994670757L;
	List<BufferedImage> listaImagenes = null;
	int longitud;
	int index = 0;
	public static long idPrograma;
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
	private Image imgNoticiaCuatro;
	@Wire
	private Image img1;
	@Wire
	private Image img2;
	@Wire
	private Image img3;
	@Wire
	private Image img4;
	@Wire
	private Image img5;
	@Wire
	private Image img6;
	@Wire
	private Image img7;
	private static String url1;
	private static String url2;
	private static String url3;
	private static String url4;
	private static String url5;
	private static String url6;
	private static String url7;

	/*
	 * Metodo heredado del Controlador CGeneral se llenan los campos y combos
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programa = servicioPrograma.buscarActivas();
		if (cmbPrograma != null) {
			cmbPrograma.setModel(new ListModelList<Programa>(programa));

		}
		if (imgNoticiaUno != null) {
			imgNoticiaUno.setContent(getImagen());
			imgNoticiaDos.setContent(getImagen());
			imgNoticiaTres.setContent(getImagen());
			imgNoticiaCuatro.setContent(getImagen());
		}
		if (img1 != null) {
			List<EnlaceInteres> enlace = servicioEnlace.buscarActivos();
			url1 = enlace.get(0).getUrl();
			url2 = enlace.get(1).getUrl();
			url3 = enlace.get(2).getUrl();
			url4 = enlace.get(3).getUrl();
			url5 = enlace.get(4).getUrl();
			url6 = enlace.get(5).getUrl();
			url7 = enlace.get(6).getUrl();
			try {
				img1.setContent(ImageIO.read(new ByteArrayInputStream(enlace
						.get(0).getImagen())));
				img2.setContent(ImageIO.read(new ByteArrayInputStream(enlace
						.get(1).getImagen())));
				img3.setContent(ImageIO.read(new ByteArrayInputStream(enlace
						.get(2).getImagen())));
				img4.setContent(ImageIO.read(new ByteArrayInputStream(enlace
						.get(3).getImagen())));
				img5.setContent(ImageIO.read(new ByteArrayInputStream(enlace
						.get(4).getImagen())));
				img6.setContent(ImageIO.read(new ByteArrayInputStream(enlace
						.get(5).getImagen())));
				img7.setContent(ImageIO.read(new ByteArrayInputStream(enlace
						.get(6).getImagen())));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * Metodos que permite llenar enviar a una pagina de segun la catidad de
	 * enlaces
	 */
	

	@Listen("onClick = #img1")
	public void enlaceUno() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect(url1);
	}

	@Listen("onClick = #img2")
	public void enlaceDos() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect(url2);
	}

	@Listen("onClick = #img3")
	public void enlaceTres() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect(url3);
	}
	@Listen("onClick = #img4")
	public void enlaceCuatro() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect(url4);
	}
	@Listen("onClick = #img5")
	public void enlaceCinco() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect(url5);
	}
	@Listen("onClick = #img6")
	public void enlaceSeis() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect(url6);
	}
	@Listen("onClick = #img7")
	public void enlaceSiete() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect(url7);
	}

	/*
	 * Metodo que permite llenar la lista de imagenes correspondientes a las
	 * noticias y esta lista a su vez es retornada
	 */
	private List<BufferedImage> getListaImagenes() {
		List<Noticia> noticia = servicioNoticia.buscarActivos();
		if (listaImagenes == null) {
			listaImagenes = new ArrayList<BufferedImage>();
			for (int i = 0; i < noticia.size(); i++)
				try {
					listaImagenes.add(ImageIO.read(new ByteArrayInputStream(
							noticia.get(i).getImagen())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		longitud = listaImagenes.size();
		return listaImagenes;
	}

	/*
	 * Metodo que permite tomar la imagen en la posicion "index" de la lista de
	 * imagenes para las noticias y la retorna
	 */
	private BufferedImage getImagen() {
		BufferedImage imagen = getListaImagenes().get(index);
		index = (index + 1) % longitud;
		return imagen;
	}

	/*
	 * Metodo que permite buscar el cronograma academico de determinado programa
	 * en el lapso actual
	 */
	@Listen("onSelect = #cmbPrograma")
	public void llenarCronograma() {
		idPrograma = Long.parseLong(cmbPrograma.getSelectedItem().getId());
		Lapso lapso = servicioLapso.buscarLapsoVigente();
		Programa programa = servicioPrograma.buscar(idPrograma);
		List<Cronograma> cronograma = servicioCronograma
				.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		ltbCronograma.setModel(new ListModelList<Cronograma>(cronograma));
		wdwCrono.setVisible(false);
	}

	/* Metodo que permite abrir la vista de solicitar tutor */
	@Listen("onClick = #btnSolicitarTutor")
	public void SolicitarTutor() {
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VSolicitarTutor.zul", null, null);
		window.doModal();
	}

	/* Metodo que permite abrir la vista de reinicio de contrasenia */
	@Listen("onClick = #lblOlvidoClave")
	public void reiniciarClave() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VReinicioClave.zul", null, null);
		window.doModal();
	}

	/*
	 * Metodo que permite abrir la vista de informacion del "Quienes Somos" de
	 * SITEG
	 */
	@Listen("onClick = #mitQuienesSomos")
	public void QuienesSomos() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VQuienesSomos.zul", null, null);
		window.doModal();
	}

	/* Metodo que permite abrir la vista de "Quienes Somos" de SITEG */
	@Listen("onClick = #mitContactanos")
	public void contactanos() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VContactanos.zul", null, null);
		window.doModal();
	}
	@Listen("onClick = #mitInicio")
	public void inicio() {
		Execution exec = Executions.getCurrent();
		exec.sendRedirect("http://localhost:8080/SITEG/VPrincipal.zul");
	}

	/*
	 * Metodo que permite abrir la vista de la biblioteca virtual de TEG
	 */
	@Listen("onClick = #btnBiblioteca")
	public void BibliotecaVirtual() {
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArchivo.zul", null, null);
		window.doModal();
	}

	/*
	 * Metodo que permite abrir la vista de informacion de interes sobre la
	 * gestion de trabajos especiales de grado
	 */
	@Listen("onClick = #btnInformacionInteres")
	public void InformacionInteres(Event e) {
		Window wdwInformacionInteres = (Window) Executions.createComponents(
				"/vistas/portal-web/VInformacionInteres.zul", null, null);
		wdwInformacionInteres.doModal();
		CCatalogoArchivoDescarga cata = new CCatalogoArchivoDescarga();
		cata.metodoPrender();
		CCatalogoAreaInvestigacion areas = new CCatalogoAreaInvestigacion();
		areas.metodoApagar();
	}

	/* Metodo que permite abrir la vista de consultar estatus del TEG */
	@Listen("onClick = #btnConsultarEstatus")
	public void ventanaEmergente(Event e) {

		if (cedulaEstatus.getValue() != null) {

			String cedula = Integer.toString(cedulaEstatus.getValue());

			if (cedula != "") {
				if (servicioEstudiante.buscarEstudiante(cedula) != null) {

					Estudiante estudiante = servicioEstudiante
							.buscarEstudiante(cedula);

					if (servicioSolicitudTutoria.buscarSolicitud(estudiante)
							.size() != 0
							|| servicioTeg.buscarTegPorEstudiante(estudiante)
									.size() != 0) {

						cedulaEstatus.setValue(null);
						CConsultarEstatus consultarestatus = new CConsultarEstatus();
						consultarestatus.recibirCedula(cedula);

					} else {

						Messagebox
								.show("Estudiante apto para realizar un Trabajo Especial de Grado",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
						cedulaEstatus.setValue(null);

					}
				} else {

					Messagebox
							.show("Estudiante no apto para realizar un Trabajo Especial de Grado",
									"Error", Messagebox.OK, Messagebox.ERROR);
					cedulaEstatus.setValue(null);

				}

			}
		} else {
			Messagebox.show(
					"Introduzca su numero de cedula para realizar la consulta",
					"Error", Messagebox.OK, Messagebox.ERROR);
		}

	}

}
