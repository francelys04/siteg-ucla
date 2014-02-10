package modelo.reporte;

import modelo.Teg;
import modelo.TegEstatus;

public class PromedioTiempoTeg {

	
	private String nombreEstatus;
	private float contadorDia13;
	private float contadorDia46;
	private float contadorDia7;
	
	public PromedioTiempoTeg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PromedioTiempoTeg(String nombreEstatus, float contadorDia13,
			float contadorDia46, float contadorDia7) {
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

	public float getContadorDia13() {
		return contadorDia13;
	}

	public void setContadorDia13(float contadorDia13) {
		this.contadorDia13 = contadorDia13;
	}

	public float getContadorDia46() {
		return contadorDia46;
	}

	public void setContadorDia46(float contadorDia46) {
		this.contadorDia46 = contadorDia46;
	}

	public float getContadorDia7() {
		return contadorDia7;
	}

	public void setContadorDia7(float contadorDia7) {
		this.contadorDia7 = contadorDia7;
	}

	

	

	





	

	
	
}

