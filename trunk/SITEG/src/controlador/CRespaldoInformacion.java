package controlador;

import java.io.IOException;
import java.util.Date;

import javax.swing.JFileChooser;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@Controller
public class CRespaldoInformacion extends CGeneral {

	@Wire
	private Button btnRespaldar;
	@Wire
	private Button btnSalir;
	@Wire
	private Textbox txtRuta;
	@Wire
	private Radio rbLocal;
	@Wire
	private Radio rbDispositivo;
	@Wire
	private Window wdwRespaldoInformacion;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

	}

	@Listen("onClick = #btnRespaldar")
	public void respaldarInformacion() {
		respaldarBD();

	}

	@Listen("onClick = #btnSalir")
	public void salir() {
		wdwRespaldoInformacion.onClose();

	}

	/**
	 * Metodo que respalda la informacion contenida en el Sistema
	 * 
	 */
	private void respaldarBD() {
		String ruta;
		String fecha = new Date().toLocaleString();
		String fecha1 = "(" + fecha.substring(0, 2) + "-"
				+ fecha.substring(3, 5) + "-" + fecha.substring(6, 10)
				+ ")";
		String nombre = "SITEG" + fecha1 + ".sql";
		if (rbLocal.isChecked()) {
			ruta = obtenerDirectorio() + "SITEG/respaldos/";
			System.out.println("ruta" + ruta);
			
		} else {
			ruta = this.txtRuta.getText();
		}
		try {
			ruta+="/";
			System.out.println(ruta);
			Runtime.getRuntime().exec(
					"pg_dump -i -h localhost -p 5432 -U postgres -F c -v -f "
							+ ruta + nombre + " siteg");
			Executions.getCurrent().sendRedirect("/respaldos/" + nombre,
					"_blank");
			Messagebox.show("El Respaldo se ha realizado con exito");
		} catch (IOException e) {

		}

	}
	/**
	 * Metodo que selecciona la ruta
	 * 
	 */
	@Listen("onClick = #btnSleccionar")
	public void seleccionarRuta() {

		JFileChooser chooser = new JFileChooser();
		// Titulo que llevara la ventana
		chooser.setDialogTitle("Seleccione...");

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		chooser.showOpenDialog(null);

		// Retornando el directorio destino directorio
		if (chooser.getSelectedFile() != null) {
			txtRuta.setValue(chooser.getSelectedFile().getPath());
		}
	}

}
