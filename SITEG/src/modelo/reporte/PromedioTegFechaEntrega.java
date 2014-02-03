package modelo.reporte;

import modelo.Teg;

public class PromedioTegFechaEntrega {

	
	private Teg teg;
	private long contadorMenores;
	private long contadorIguales;
	private long contadorMayores;
	
	public PromedioTegFechaEntrega() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PromedioTegFechaEntrega(Teg teg, long contadorMenores,
			long contadorIguales, long contadorMayores) {
		super();
		this.teg = teg;
		this.contadorMenores = contadorMenores;
		this.contadorIguales = contadorIguales;
		this.contadorMayores = contadorMayores;
	}

	public Teg getTeg() {
		return teg;
	}

	public void setTeg(Teg teg) {
		this.teg = teg;
	}

	public long getContadorMenores() {
		return contadorMenores;
	}

	public void setContadorMenores(long contadorMenores) {
		this.contadorMenores = contadorMenores;
	}

	public long getContadorIguales() {
		return contadorIguales;
	}

	public void setContadorIguales(long contadorIguales) {
		this.contadorIguales = contadorIguales;
	}

	public long getContadorMayores() {
		return contadorMayores;
	}

	public void setContadorMayores(long contadorMayores) {
		this.contadorMayores = contadorMayores;
	}

	
	
}

