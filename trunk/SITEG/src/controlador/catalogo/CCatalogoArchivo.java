package controlador.catalogo;

import java.util.ArrayList;
import java.util.List;

import modelo.Archivo;

import org.springframework.stereotype.Controller;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CCatalogoArchivo extends CGeneral {

	private long id = 0;
	private static String vistaRecibida;

	@Wire
	private Listbox ltbArchivo;
	@Wire
	private Textbox txtNombreMostrarArchivo;
	@Wire
	private Textbox txtDescripcionMostrarArchivo;
	@Wire
	private Textbox txtProgramaMostrarArchivo;
	@Wire
	private Window wdwCatalogoArchivo;

	/*
	 * Metodo heredado del Controlador CGeneral donde se se buscan todos los
	 * archivos disponibles de Teg y se llena el listado del mismo en el
	 * componente lista de la vista.
	 */
	public void inicializar(Component comp) {

		List<Archivo> archivo = servicioArchivo.buscarActivos("Teg");
		ltbArchivo.setModel(new ListModelList<Archivo>(archivo));

	}

	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/*
	 * Metodo que permite filtrar los archivos disponibles del Teg, mediante el
	 * componente de la lista, donde se podra visualizar el nombre, la
	 * descripcion y el programa de estos.
	 */
	@Listen("onChange = #txtNombreMostrarArchivo,#txtDescripcionMostrarArchivo,#txtProgramaMostrarArchivo")
	public void filtrarDatosCatalogo() {
		List<Archivo> archivo1 = servicioArchivo.buscarActivos("Teg");
		List<Archivo> archivo2 = new ArrayList<Archivo>();

		for (Archivo archivo : archivo1) {
			if (archivo.getNombre().toLowerCase()
					.contains(txtNombreMostrarArchivo.getValue().toLowerCase())
					&& archivo
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarArchivo.getValue()
											.toLowerCase())
					&& archivo
							.getPrograma()
							.getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarArchivo.getValue()
											.toLowerCase())) {
				archivo2.add(archivo);
			}
		}

		ltbArchivo.setModel(new ListModelList<Archivo>(archivo2));

	}

	/*
	 * Metodo que permite obtener el objeto Archivo al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su
	 * id, donde para luego implementar el servicio de busqueda y cargar otro
	 * objeto Archivo la cual mediante la instruccion Filedownload.save podra
	 * ser descargado en una ubicacion que le indique el usuario.
	 */
	@Listen("onDoubleClick = #ltbArchivo")
	public void descargarArchivo() {
		if (ltbArchivo.getItemCount() != 0) {
			Listitem listItem = ltbArchivo.getSelectedItem();
			Archivo archivo3 = (Archivo) listItem.getValue();
			Archivo archivo4 = servicioArchivo.buscarArchivo(archivo3.getId());
			Filedownload.save(archivo4.getContenidoDocumento(),
					archivo4.getTipoDocumento(), archivo4.getNombre());
		}
	}

}
