package modelo.compuesta.id;

import java.io.Serializable;

import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;

public class ProgramaItemId implements Serializable{

	private Programa programa;
	private ItemEvaluacion item;
	private Lapso lapso;
}
