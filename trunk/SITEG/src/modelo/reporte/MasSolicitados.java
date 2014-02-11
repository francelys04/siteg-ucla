package modelo.reporte;

import modelo.Profesor;
import modelo.Tematica;

public class MasSolicitados {

	private long primerValor;
	private long segundoValor;
	private long tercerValor;
	private Profesor tutor;
	private Tematica tematica;
	public MasSolicitados(long primerValor, long segundoValor,
			long tercerValor, Profesor profesor, Tematica tematica) {
		super();
		this.primerValor = primerValor;
		this.segundoValor = segundoValor;
		this.tercerValor = tercerValor;
		this.tutor = profesor;
		this.tematica = tematica;
	}
	public MasSolicitados() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getPrimerValor() {
		return primerValor;
	}
	public void setPrimerValor(long primerValor) {
		this.primerValor = primerValor;
	}
	public long getSegundoValor() {
		return segundoValor;
	}
	public void setSegundoValor(long segundoValor) {
		this.segundoValor = segundoValor;
	}
	public long getTercerValor() {
		return tercerValor;
	}
	public void setTercerValor(long tercerValor) {
		this.tercerValor = tercerValor;
	}
	
	public Profesor getTutor() {
		return tutor;
	}
	public void setTutor(Profesor tutor) {
		this.tutor = tutor;
	}
	public Tematica getTematica() {
		return tematica;
	}
	public void setTematica(Tematica tematica) {
		this.tematica = tematica;
	}
	
	
}

