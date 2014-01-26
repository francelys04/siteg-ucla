/**
 * 
 */
package configuracion;

import java.net.URL;

/**
 * Clase de Utilidades del Sistema
 * 
 * 
 */
public class UtilidadSiteg {

	/**
	 * 
	 * @return El directorio actual de trabajo
	 */
	public static String obtenerDirectorio() {
		URL rutaURL = UtilidadSiteg.class.getProtectionDomain()
				.getCodeSource().getLocation();
		String ruta = rutaURL.getPath();
		return "/"+ruta.substring(1, ruta.indexOf("SITEG"));
	}

}
