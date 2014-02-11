package modelo.reporte;

import modelo.Teg;
import modelo.TegEstatus;

public class PromedioTiempoTeg {

	
	private String nombreEstatus;
	private long contadorDia13;
	private long contadorDia46;
	private long contadorDia7;
	
	public PromedioTiempoTeg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PromedioTiempoTeg(String nombreEstatus, long contadorDia13,
			long contadorDia46, long contadorDia7) {
		super();
		this.nombreEstatus = nombreEstatus;
		this.contadorDia13 = contadorDia13;
		this.contadorDia46 = contadorDia46;
		this.contadorDia7 = contadorDia7;
	}

	public String getNombreEstatus() {
		return nombreEstatus;
	}

	public void setNombreEstatus(String nombreEstatus) {
		this.nombreEstatus = nombreEstatus;
	}

	public long getContadorDia13() {
		return contadorDia13;
	}

	public void setContadorDia13(long contadorDia13) {
		this.contadorDia13 = contadorDia13;
	}

	public long getContadorDia46() {
		return contadorDia46;
	}

	public void setContadorDia46(long contadorDia46) {
		this.contadorDia46 = contadorDia46;
	}

	public long getContadorDia7() {
		return contadorDia7;
	}

	public void setContadorDia7(long contadorDia7) {
		this.contadorDia7 = contadorDia7;
	}
}

