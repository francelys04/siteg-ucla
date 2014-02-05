package modelo.compuesta.id;

import java.io.Serializable;

import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Programa;

public class ProgramaAreaId implements Serializable {

	private Programa programa;
	private AreaInvestigacion area;
	private Lapso lapso;
}
