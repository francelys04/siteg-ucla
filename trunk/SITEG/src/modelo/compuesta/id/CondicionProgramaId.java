package modelo.compuesta.id;

import java.io.Serializable;

import modelo.Condicion;
import modelo.Lapso;
import modelo.Programa;

public class CondicionProgramaId implements Serializable {

	private Condicion condicion;
	private Programa programa;
	private Lapso lapso;
}
