package modelo.compuesta.id;

import java.io.Serializable;

import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;

public class ProgramaRequisitoId implements Serializable {

	private Programa programa;
	private Requisito requisito;
	private Lapso lapso;
}
