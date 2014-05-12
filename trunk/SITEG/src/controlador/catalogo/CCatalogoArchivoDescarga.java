package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Archivo;

import org.springframework.stereotype.Controller;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/**
 * Controlador asociado a la vista catalogo archivo descarga que permite mostrar
 * los archivos de interes de gestion de trabajos especiales de grado
 * disponibles a traves de un listado
 */
@Controller
public class CCatalogoArchivoDescarga extends CGeneral {

	private long id = 0;
	private static boolean encontrado = true;
	private static String vistaRecibida;

	@Wire
	private Listbox ltbArchivoDescarga;
	@Wire
	private Textbox txtNombreMostrarArchivoDescarga;
	@Wire
	private Textbox txtDescripcionMostrarArchivoDescarga;
	@Wire
	private Textbox txtProgramaMostrarArchivoDescarga;
	@Wire
	private Window wdwCatalogoArchivoDescarga;

	/**
	 * Metodo heredado del Controlador CGeneral donde se se buscan todos los
	 * archivos disponibles de Descarga y se llena el listado del mismo en el
	 * componente lista de la vista.
	 */
	public void inicializar(Component comp) {
		List<Archivo> archivo = servicioArchivo.buscarActivos("Descarga");
		ltbArchivoDescarga.setModel(new ListModelList<Archivo>(archivo));

	}

	/**
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 * 
	 * @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/**
	 * Metodo que permite asignarle el valor de false a la variable booleana
	 * encontrado, para verificar si el usuario esta accediendo a traves del
	 * sistema o del protal web
	 */
	public void metodoApagar() {
		encontrado = false;
	}

	
	/**
	 * Metodo que permite asignarle el valor de true a la variable booleana
	 * encontrado, para verificar si el usuario esta accediendo a traves del
	 * sistema o del protal web
	 */
	public void metodoPrender() {
		encontrado = true;
	}

	/**
	 * Metodo que permite filtrar los archivos disponibles de Teg, mediante el
	 * componente de la lista, donde se podra visualizar el nombre, la
	 * descripcion y el programa de estos.
	 */
	@Listen("onChange = #txtNombreMostrarArchivoDescarga,#txtDescripcionMostrarArchivoDescarga,#txtProgramaMostrarArchivoDescarga")
	public void filtrarDatosCatalogo() {
		List<Archivo> archivo1 = servicioArchivo.buscarActivos("Descarga");
		List<Archivo> archivo2 = new ArrayList<Archivo>();

		for (Archivo archivo : archivo1) {
			if (archivo
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarArchivoDescarga.getValue()
									.toLowerCase())
					&& archivo
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarArchivoDescarga
											.getValue().toLowerCase())
					&& archivo
							.getPrograma()
							.getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarArchivoDescarga
											.getValue().toLowerCase())) {
				archivo2.add(archivo);
			}
		}

		ltbArchivoDescarga.setModel(new ListModelList<Archivo>(archivo2));

	}

	/**
	 * Metodo que permite dado al retorno de la variable booleana de encontrado,
	 * si es "true", se obtendra el objeto Archivo al realizar el evento doble
	 * clic sobre un item en especifico en la lista, y se extraera su id, para
	 * luego implementar el servicio de busqueda y cargar otro objeto Archivo la
	 * cual mediante la instruccion Filedownload.save podra ser descargado en
	 * una ubicacion que le indique el usuario. Sino, si la variable encontrado
	 * es igual a "false" el objeto extraido al seleccionar un item de la lista,
	 * sera mapeada con su id para ser enviada a la vista VSubirArchivoDescarga.
	 */
	@Listen("onDoubleClick = #ltbArchivoDescarga")
	public void descargarArchivo() {
		try {
			if (ltbArchivoDescarga.getItemCount() != 0) {
				if (encontrado == true) {
					Listitem listItem = ltbArchivoDescarga.getSelectedItem();
					Archivo archivo3 = (Archivo) listItem.getValue();
					Archivo archivo4 = servicioArchivo.buscarArchivo(archivo3
							.getId());
					Filedownload.save(archivo4.getContenidoDocumento(),
							archivo4.getTipoDocumento(), archivo4.getNombre());

				} else {
					Listitem listItem = ltbArchivoDescarga.getSelectedItem();
					Archivo archivo = (Archivo) listItem.getValue();
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", archivo.getId());
					String vista = "portal-web/VSubirArchivoDescarga";
					map.put("vista", vista);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					Executions.sendRedirect("/vistas/arbol.zul");
					wdwCatalogoArchivoDescarga.onClose();

				}
			}
		} catch (NullPointerException e) {

			System.out.println("NullPointerException");
		}
	}

}
