package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JFileChooser;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Controlador asociado a la vista de respald de informacion, que permite
 * realizar el respaldo o restauracion de la base de datos
 */
@Controller
public class CRespaldoInformacion extends CGeneral {

	private static final long serialVersionUID = -986552569263113746L;
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
	private Radio rbWindows;
	@Wire
	private Radio rbLinux;
	@Wire
	private Window wdwRespaldoInformacion;
	@Wire
	private Groupbox gbSistema;
	@Wire
	private Groupbox gbDispositivo;
	@Wire
	private Button btnRestaurar;
	private static String ruta;
	boolean encontrad = true;
	private static String extension;

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metodo que permite seleccionar en que sistema operativo se realizara el
	 * respaldo de la informacion
	 */
	@Listen("onClick = #btnRespaldar")
	public void respaldarInformacion() {
		if (rbLinux.isChecked()) {
			respaldarBD();
		} else if (rbWindows.isChecked()) {
			ejecutarComandos();
		}

	}

	/**
	 * Metodo que permite cerrar la pantalla actualizando los cambios realizados
	 */
	@Listen("onClick = #btnSalir")
	public void salir() {
		wdwRespaldoInformacion.onClose();
	}

	@Listen("onClick = #btnRestaurar")
	public void restaurar() {
		if (rbLinux.isChecked()) {
			restaurarBD("pg_restore");
		} else if (rbWindows.isChecked()) {
			restaurarBD("C:\\Program Files\\PostgreSQL\\9.3\\bin//\\pg_restore.exe");
		}
	}

	/***
	 * Metodo que respalda la informacion contenida en el Sistema
	 * 
	 */
	private void respaldarBD() {
		if (rbLocal.isChecked()) {
			if (txtRuta.getText().equals("")) {
				Messagebox.show("Debe indicar la ruta", "Error", Messagebox.OK,
						Messagebox.ERROR);
			} else {
				ruta = this.txtRuta.getText();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy" + "_"
						+ "hhmmss");
				Calendar cal = Calendar.getInstance(); // hoy
				String valor = sdf.format(cal.getTime());
				String nombre = "SITEG" + valor + ".backup";
				try {
					ruta += "/";
					System.out.println(ruta);
					System.out.println("entro");
					Runtime.getRuntime().exec(
							"pg_dump -i -h localhost -p 5432 -U postgres -F c -v -f "
									+ ruta + nombre + " siteg");

					Messagebox.show("Respaldo realizado exitosamente",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
					cancelar();
				} catch (IOException e) {
					Messagebox.show("No se pudo realizar el respaldo", "Error",
							Messagebox.OK, Messagebox.ERROR);
					cancelar();
				}

			}
		}
	}

	/**
	 * Metodo que permite seleccionar la ruta del archivo
	 */
	@Listen("onClick = #btnSleccionar")
	public void seleccionar() {
		if (rbLocal.isChecked()) {
			seleccionarRuta();
		} else if (rbDispositivo.isChecked()) {
			seleccionarArchivo();
		}

	}

	/***
	 * Metodo que selecciona la ruta para la carpeta
	 */
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

	/***
	 * Metodo que permite respaldar la base de datos en windows
	 */
	public void ejecutarComandos() {
		if (txtRuta.getText().equals("")) {
			Messagebox.show("Debe indicar la ruta", "Error", Messagebox.OK,
					Messagebox.ERROR);
		} else {
			try {
				ruta = this.txtRuta.getText();
				ruta += "\\";
				System.out.println(ruta);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy" + "_"
						+ "hhmmss");
				Calendar cal = Calendar.getInstance(); // hoy
				String valor = sdf.format(cal.getTime());
				String nombre = "SITEG" + valor + ".backup";
				Runtime r = Runtime.getRuntime();

				Process p;
				ProcessBuilder pb;

				r = Runtime.getRuntime();
				List lista = new ArrayList();
				lista.add("C:\\Program Files\\PostgreSQL\\9.3\\bin//\\pg_dump.exe");
				lista.add("-i");
				lista.add("-h");
				lista.add("localhost");
				lista.add("-p");
				lista.add("5432");
				lista.add("-U");
				lista.add("postgres");
				lista.add("-F");
				lista.add("c");
				lista.add("-b");
				lista.add("-v");
				lista.add("-f");
				lista.add(ruta + nombre);
				lista.add("siteg");

				pb = new ProcessBuilder(lista);
				pb.environment().put("PGPASSWORD", ("equipo2"));
				pb.redirectErrorStream(true);

				p = pb.start();
				Messagebox.show("Respaldo en proceso", "Informacion",
						Messagebox.OK, Messagebox.INFORMATION);
				cancelar();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Messagebox.show("No se pudo realizar el respaldo", "Error",
						Messagebox.OK, Messagebox.ERROR);
				cancelar();
			}

		}

	}

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	public void cancelar() {
		rbDispositivo.setChecked(false);
		rbLocal.setChecked(false);
		rbLinux.setChecked(false);
		rbWindows.setChecked(false);
		txtRuta.setValue("");
		gbDispositivo.setVisible(false);
		gbSistema.setVisible(false);
		btnRespaldar.setVisible(false);
		btnRestaurar.setVisible(false);

	}

	/***
	 * Metodo que permite seleccionar el archivo a restaurar
	 */
	public void seleccionarArchivo() {

		JFileChooser chooser = new JFileChooser();
		// Titulo que llevara la ventana
		chooser.setDialogTitle("Seleccione...");

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showOpenDialog(null);

		// Si seleccionamos algunn archivo retornaremos su ubicacion
		if (chooser.getSelectedFile() != null) {
			extension = chooser.getSelectedFile().getName();
			txtRuta.setValue(chooser.getSelectedFile().getPath());
		}

	}

	/***
	 * Metodo que permite restaurar la base de datos
	 */
	public void restaurarBD(String rutapg) {
		if ((txtRuta.getValue().equals(""))) {
			Messagebox.show("Debe seleccionar un archivo", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else if (!extension.contains("backup")) {
			Messagebox.show("Debe seleccionar un archivo de tipo .backup",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			try {
				borrarEsquema("public");
				crearEsquema("public");
				Runtime r = Runtime.getRuntime();
				String archivoBD = "";
				archivoBD = txtRuta.getValue();
				Process p;
				ProcessBuilder pb;
				r = Runtime.getRuntime();
				List lista = new ArrayList();
				lista.add(rutapg);
				lista.add("-i");
				lista.add("-h");
				lista.add("localhost");
				lista.add("-p");
				lista.add("5432");
				lista.add("-U");
				lista.add("postgres");
				lista.add("-d");
				lista.add("siteg");
				lista.add("-v");
				lista.add(archivoBD);

				pb = new ProcessBuilder(lista);
				pb.environment().put("PGPASSWORD", ("equipo2"));
				pb.redirectErrorStream(true);
				p = pb.start();
				Messagebox.show("Restauracion en proceso", "Informacion",
						Messagebox.OK, Messagebox.INFORMATION);
				cancelar();
			} catch (Exception e) {
				Messagebox.show("No se pudo realizar la restauracion", "Error",
						Messagebox.OK, Messagebox.ERROR);
				cancelar();
			}
		}
	}

	/***
	 * Metodo que permite borrar los esquemas de la base de datos
	 */
	public void borrarEsquema(String nombreEsquema) {
		String driver = "org.postgresql.Driver";
		String connectString = "jdbc:postgresql://localhost:5432/siteg";
		String user = "postgres";
		String password = "equipo2";
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(connectString, user,
					password);
			Statement stmt = con.createStatement();

			// Borrando el esquema

			int count = stmt.executeUpdate("DROP SCHEMA " + nombreEsquema
					+ " CASCADE");
			stmt.close();
			con.close();
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}

	/***
	 * Metodo que permite crear los esquemas de la base de datos
	 */
	public void crearEsquema(String nombreEsquema) {
		String driver = "org.postgresql.Driver";
		String connectString = "jdbc:postgresql://localhost:5432/siteg";
		String user = "postgres";
		String password = "equipo2";
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(connectString, user,
					password);
			Statement stmt = con.createStatement();

			// Borrando el esquema

			int count = stmt.executeUpdate("CREATE SCHEMA " + nombreEsquema
					+ "");
			stmt.close();
			con.close();
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}

}
