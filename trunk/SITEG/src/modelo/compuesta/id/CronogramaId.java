package modelo.compuesta.id;

import java.io.Serializable;

import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;

public class CronogramaId implements Serializable {

	private Programa programa;
	private Actividad actividad;
	private Lapso lapso;
}
